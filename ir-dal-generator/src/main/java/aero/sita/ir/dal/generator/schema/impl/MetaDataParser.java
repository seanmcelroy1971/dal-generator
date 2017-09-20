package aero.sita.ir.dal.generator.schema.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import aero.sita.ir.dal.generator.Constants;
import aero.sita.ir.dal.generator.DalGeneratorException;
import aero.sita.ir.dal.generator.cli.CLIDelegate;
import aero.sita.ir.dal.generator.dto.Column;
import aero.sita.ir.dal.generator.dto.Table;
import aero.sita.ir.dal.generator.schema.IHandler;
import aero.sita.ir.dal.generator.schema.IParser;

/**
 * The Class MetaDataParser.
 */
public class MetaDataParser implements IParser {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(MetaDataParser.class);

    /** The Constant TABLE_TYPES. */
    private static final String[] TABLE_TYPES = {"TABLE"};

    /** The Constant TABLE_NAME_COL. */
    private static final String TABLE_NAME_COL = "TABLE_NAME";

    /** The Constant COLUMN_NAME_COL. */
    private static final String COLUMN_NAME_COL = "COLUMN_NAME";

    /** The Constant DATA_TYPE_COL. */
    private static final String DATA_TYPE_COL = "DATA_TYPE";

    /** The Constant TYPE_NAME_COL. */
    private static final String TYPE_NAME_COL = "TYPE_NAME";

    /** The Constant DELETED_TABLE_NAME_PREFIX. */
    private static final String DELETED_TABLE_NAME_PREFIX = "BIN$";

    /** The Constant DATABASE_CLOSE_ERROR. */
    private static final String DATABASE_CLOSE_ERROR = "Error closing database connection";

    /** The data source. */
    private BasicDataSource dataSource;

    /** The handler. */
    private IHandler handler;

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    /**
     * Instantiates a new meta data parser.
     */
    public MetaDataParser() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public BasicDataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Sets the data source.
     *
     * @param dataSource the new data source
     */
    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the handler.
     *
     * @return the handler
     */
    public IHandler getHandler() {
        return this.handler;
    }

    /**
     * Sets the handler.
     *
     * @param handler the new handler
     */
    public void setHandler(IHandler handler) {
        this.handler = handler;
    }


    /* (non-Javadoc)
     * @see aero.sita.ir.dal.schema.IParser#parse()
     */
    public List<Table> parse() throws DalGeneratorException {
        LOGGER.info(Constants.ENTERING);

        /**
         * The database connection;
         */
        Connection connection = null;

        try {
            /**
             * Initialise the DB connection
             */
            connection = getDataSource().getConnection();

            /**
             * Parse the database meta data and extract the
             * table data
             */
            parseTableData(connection.getMetaData());

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            /**
             * Close the database connection
             */
            closeConnection(connection);
        }

        /**
         * Return the list of tables
         */
        LOGGER.info(Constants.EXITING);
        return getHandler().getTables();
    }



    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    /**
     * Gets the schema name.
     *
     * @return the schema name
     */
    private String getSchemaName() {
        return getDataSource().getUsername().toUpperCase();
    }

    /**
     * Parses the table data.
     *
     * @param databaseMetaData the database meta data
     * @throws SQLException the sQL exception
     * @throws DalGeneratorException
     */
    private void parseTableData(DatabaseMetaData databaseMetaData) throws SQLException, DalGeneratorException {

        /**
         * Get all the tables
         */
        ResultSet resultSet = databaseMetaData.getTables(null, getSchemaName(), null, TABLE_TYPES);

        /**
         * Iterate through the tables and log
         * the name of each
         */
        while(resultSet.next()) {
            /**
             * Get the table name
             */
            String tableName = resultSet.getString(TABLE_NAME_COL);

            /**
             * One feature of Oracle 10g+ is the recyclebin,
             * which allows you to recover dropped tables.
             * With recyclebin, any tables you drop do not actually get deleted.
             * Instead, Oracle renames the table and its associated objects to a system-generated
             * name that begins with BIN$.
             * We do not wnat to create DAL objects for dropped tables
             * so we check that the tables is does not start with BIN$
             */
            if(!isDeletedTable(tableName)) {
                /**
                 * Create table instance
                 */
                Table table = new Table(tableName);

                /**
                 * Set the primary key
                 */
                table.getPrimaryKey().addAll(getPrimaryKey(databaseMetaData, getSchemaName(), tableName));

                /**
                 * Add table to handler
                 */
                getHandler().addTable(table);

                /**
                 * Parse the field data, for this
                 * table
                 */
                parseColumnData(databaseMetaData, table);
            }
        }
    }

    /**
     * Gets the primary key column.
     *
     * @param databaseMetaData the database meta data
     * @param schema the schema
     * @param table the table
     * @return the primary key column
     * @throws SQLException the sQL exception
     */
    private List<String> getPrimaryKey(DatabaseMetaData databaseMetaData, String schema, String table) throws SQLException, DalGeneratorException {

        /**
         * The column name to return
         */
        List<String> pkColumnNames = new ArrayList<String>();

        /**
         * Get the primay kesy for this table
         */
        ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, schema, table);

        /**
         * Iterate through the primary keys and log
         * the name of each
         */
        while(resultSet.next()) {
            pkColumnNames.add(resultSet.getString(COLUMN_NAME_COL));
        }

        /**
         * Return the pk
         */
        return pkColumnNames;
    }

    /**
     * Parses the column data.
     *
     * @param databaseMetaData the database meta data
     * @param table the table
     * @throws SQLException the sQL exception
     */
    private void parseColumnData(DatabaseMetaData databaseMetaData, Table table) throws SQLException {

        /**
         * Get all the columns for this tabls
         */
        ResultSet resultSet = databaseMetaData.getColumns(null, getSchemaName(), table.getName(), null);

        /**
         * Iterate through the tables and log
         * the name of each
         */
        while(resultSet.next()) {
            /**
             * Get the column name
             */
            String columnName = resultSet.getString(COLUMN_NAME_COL);

            /**
             * Get the column type
             */
            int type = resultSet.getInt(DATA_TYPE_COL);

            /**
             * Get the column type name
             */
            String typeName = resultSet.getString(TYPE_NAME_COL);

            /**
             * Create field instance
             */
            Column column = new Column();
            column.setName(columnName);
            column.setTable(table);
            column.setType(type);
            column.setTypeName(typeName);
            column.setPrimaryKey(table.getPrimaryKey().contains(columnName));

            /**
             * Add column to table
             */
            table.addColumn(column);
        }

    }


    /**
     * Removes the colum prefix.
     *
     * @return true, if successful
     */
    private boolean removeColumPrefix() {
        return (System.getProperty(CLIDelegate.REMOVE_COLUMN_PREFIX) != null);
    }

//    /**
//     * Gets the column name.
//     *
//     * @param resultSet the result set
//     * @return the column name
//     * @throws SQLException the sQL exception
//     */
//    private String getColumnName(ResultSet resultSet) throws SQLException {
//       /**
//        * Get the column name
//        */
//       String columnName = resultSet.getString(COLUMN_NAME_COL);
//
//       /**
//        * Check if we should remove the column prefix
//        */
//       if(removeColumPrefix()) {
//           columnName = StringUtils.substringAfter(columnName, "_");
//       }
//
//       return columnName;
//    }

    /**
     * Checks if is deleted table.
     *
     * @param tableName the table name
     * @return true, if is deleted table
     */
    private boolean isDeletedTable(String tableName) {
        /**
         * One feature of Oracle 10g+ is the recyclebin,
         * which allows you to recover dropped tables.
         * With recyclebin, any tables you drop do not actually get deleted.
         * Instead, Oracle renames the table and its associated objects to a system-generated
         * name that begins with BIN$.
         * We do not wnat to create DAL objects for dropped tables
         * so we check that the tables is does not start with BIN$
         */
        return StringUtils.startsWith(tableName, DELETED_TABLE_NAME_PREFIX);
    }

    /**
     * Close connection.
     *
     * @param connection the connection
     */
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(String.format("%s: %s", DATABASE_CLOSE_ERROR, e.getMessage()));
        }
    }
}

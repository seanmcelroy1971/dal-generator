package aero.sita.ir.dal.generator.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import aero.sita.ir.dal.generator.dto.Column;
import aero.sita.ir.dal.generator.dto.Table;
import aero.sita.ir.dal.generator.util.FileGenUtil;
import aero.sita.ir.dal.generator.util.SqlTypeMapper;

public class DaoImplGenerator {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(DaoImplGenerator.class);

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    private DaoImplGenerator() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    public static void generateDAO(File outputDir, Table table, String dalPackageName, String daoImplPackageName, String daoIntPackageName, String dtoPackageName) {

        //if(!FileGenUtil.isLinkTable(table)) {

            FileOutputStream fio = null;

            try {
                /**
                 * Create the absolute output dir, which is the output dir supplied
                 * at the command line, plus the directory structure that maps to
                 * the package name
                 */
                File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, daoImplPackageName);
                FileUtils.forceMkdir(absOutputDir);

                /**
                 * Generate the class file
                 */
                File classFile = getClassFile(absOutputDir, table);

                /**
                 * Open the file for writing
                 */
                fio = FileUtils.openOutputStream(classFile);

                /**
                 * Write file header;
                 */
                FileGenUtil.addFileHeader(fio);

                /**
                 * Write the package
                 */
                FileGenUtil.addPackageDeclaration(fio, daoImplPackageName);

                /**
                 * Add imports
                 */
                addImports(fio, table, dalPackageName, daoImplPackageName, daoIntPackageName, dtoPackageName);

                /**
                 * Add class declaration
                 */
                addClassDeclaration(fio, table, daoImplPackageName);

                /**
                 * Add public members section
                 */
                addPublicMembersSection(fio, table, daoImplPackageName);

                /**
                 * Add private static members section
                 */
                addPriateMembersSection(fio, table, daoImplPackageName);

                /**
                 * Add static initialisers section
                 */
                addStaticInitialisersSection(fio, table, daoImplPackageName);

                /**
                 * Add constructors section
                 */
                addConstructorsSection(fio, table, daoImplPackageName);

                /**
                 * Add public methods section
                 */
                addPublicMethodsSection(fio, table, daoImplPackageName);

                /**
                 * Add protected methods section
                 */
                addProtectedMethodsSection(fio, table, daoImplPackageName);

                /**
                 * Add private static methods section
                 */
                addPrivateMethodsSection(fio, table, daoImplPackageName);

                /**
                 * Add closing brace
                 */
                FileGenUtil.addClosingBrace(fio);

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            } finally {
                IOUtils.closeQuietly(fio);
            }
        }
   //}

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    /**
     * Adds the imports.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addImports(FileOutputStream fio, Table table, String dalPackageName, String daoImplPackageName, String daoIntPackageName, String dtoPackageName) throws IOException {

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addImport(fio, "java.util.List");
        FileGenUtil.addImport(fio, "java.sql.ResultSet");
        FileGenUtil.addImport(fio, "java.sql.SQLException");
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addImport(fio, "org.springframework.jdbc.core.RowMapper");
        FileGenUtil.addImport(fio, "org.springframework.jdbc.core.namedparam.MapSqlParameterSource");
        FileGenUtil.addImport(fio, "org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport");
        FileGenUtil.addImport(fio, "org.springframework.jdbc.core.simple.SimpleJdbcInsert");

        if(!table.hasCompositePrimaryKey()) {
            FileGenUtil.addImport(fio, "org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer");
        }

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addImport(fio, generateAbsDAOInterface(table, daoIntPackageName));
        FileGenUtil.addImport(fio, generateAbsDTOName(table, dtoPackageName));
        FileGenUtil.addImport(fio, generateAbsDALExceptionName(dalPackageName));

        if (table.hasCompositePrimaryKey()) {
            FileGenUtil.addImport(fio, generateAbsIDName(table, dtoPackageName));
        }
    }

    /**
     * Generate dto import.
     *
     * @param table the table
     * @param dtoPackageName the dto package name
     * @return the string
     */
    private static String generateAbsDTOName(Table table, String dtoPackageName) {
        return String.format("%s.%sDTO", dtoPackageName, table.getFormattedName());
    }

    /**
     * Generate abs dal exception name.
     *
     * @param dalPackage the dal package
     * @return the string
     */
    private static String generateAbsDALExceptionName(String dalPackage) {
        return String.format("%s.DALException", dalPackage);
    }

    /**
     * Generate abs id name.
     *
     * @param table the table
     * @param dtoPackageName the dto package name
     * @return the string
     */
    private static String generateAbsIDName(Table table, String dtoPackageName) {
        return String.format("%s.%sID", dtoPackageName, table.getFormattedName());
    }

    /**
     * Generate dao interface import.
     *
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @return the string
     */
    private static String generateAbsDAOInterface(Table table, String daoImplPackageName) {
        return String.format("%s.I%s", daoImplPackageName, table.getDAOName());
    }

    /**
     * Adds the public members section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPublicMembersSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addPublicMembersSectionHeader(fio);
    }

    /**
     * Adds the priate members section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPriateMembersSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        /**
         * Add section header
         */
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addPrivateMembersSectionHeader(fio);

        /**
         * Add column constants
         */
        addTableConstant(fio, table, daoImplPackageName);

        /**
         * Add column constants
         */
        addColumnConstants(fio, table, daoImplPackageName);

        /**
         * Add sequence incrementer
         */
        addSequenceIncrementer(fio, table, daoImplPackageName);

        /**
         * Add Find All Query
         */
        addFindAllQuery(fio, table, daoImplPackageName);

        /**
         * Add Find By Id Query
         */
        addFindByIdQuery(fio, table, daoImplPackageName);

        /**
         * Add Delete By Id Query
         */
        addDeleteByIdQuery(fio, table, daoImplPackageName);

        /**
         * Add update query
         */
        if(!table.isLinkTable()) {
            addUpdateQuery(fio, table, daoImplPackageName);
        }
    }

    /**
     * Adds the table constant.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addTableConstant(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        String columnConstantDec = String.format("    private static final String TABLE_NAME = \"%s\";", table.getName());
        FileGenUtil.writeLine(columnConstantDec, fio);
    }

    /**
     * Adds the column constants.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addColumnConstants(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        for (Column column : table.getColumns()) {
            /**
             * Write an empty linae
             */
            FileGenUtil.writeEmpytLines(1, fio);

            /**
             * Write column constants
             */
            String columnConstantDec = String.format("    private static final String %s_COL = \"%s\";", column.getName(), column.getName());
            FileGenUtil.writeLine(columnConstantDec, fio);
        }
    }

    /**
     * Adds the sequence incrementer.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSequenceIncrementer(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        if (!table.hasCompositePrimaryKey()) {
            FileGenUtil.writeEmpytLines(1, fio);
            FileGenUtil.writeLine("    private OracleSequenceMaxValueIncrementer sequenceIncrementer;", fio);
        }
    }

    /**
     * Adds the find all query.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addFindAllQuery(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    private static final String FIND_ALL_QUERY = ", fio);
        FileGenUtil.writeLine("       \" select " + generateSelectList(table) + "\" + ", fio);
        FileGenUtil.writeLine("       \"   from " + table.getName() + "\";", fio);
    }

    /**
     * Adds the find by id query.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addFindByIdQuery(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    private static final String FIND_BY_ID_QUERY = ", fio);
        FileGenUtil.writeLine("       \" select " + generateSelectList(table) + "\" + ", fio);
        FileGenUtil.writeLine("       \"   from " + table.getName() + "\" + ", fio);
        FileGenUtil.writeLine("       \"  where " + generatePkWhereClause(table) + "\";", fio);
    }

    /**
     * Adds the delete by id query.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addDeleteByIdQuery(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        String deleteQiery = String.format("    private static final String DELETE_BY_ID_QUERY = \"delete from %s where %s\";", table.getName(), generatePkWhereClause(table));
        FileGenUtil.writeLine(deleteQiery, fio);
    }

    /**
     * Adds the update query.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addUpdateQuery(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    private static final String UPDATE_QUERY = ", fio);
        FileGenUtil.writeLine("       \" update " + table.getName() + " \" + ", fio);
        FileGenUtil.writeLine("       \"    set " + generateUpdateClause(table) + " \" + ", fio);
        FileGenUtil.writeLine("       \"  where " + generatePkWhereClause(table) + " \";", fio);
    }

    /**
     * Adds the static initialisers section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addStaticInitialisersSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addStaticInitialisersSectionHeader(fio);
    }

    /**
     * Adds the constructors section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addConstructorsSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addConstructorsSectionHeader(fio);

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    public " + table.getFormattedName() + "DAO() {", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the public methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPublicMethodsSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addPublicMethodsSectionHeader(fio);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add sequence incrementors
         */
        if (!table.hasCompositePrimaryKey()) {
            addSequenceIncrementerAccessors(fio, table, daoImplPackageName);
            FileGenUtil.writeEmpytLines(1, fio);
        }

        /**
         * Add find by id method
         */
        addFindByIdMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add delete by id method
         */
        addDeleteByIdMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add delete method
         */
        addDeleteMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add delete by id method
         */
        addFindAllMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add save method
         */
        addSaveMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add save or update method
         */
        addSaveOrUpdateMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add update method
         */
        addUpdateMethod(fio, table, daoImplPackageName);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the protected methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addProtectedMethodsSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addProtectedMethodsSectionHeader(fio);
    }

    /**
     * Adds the private static methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPrivateMethodsSection(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addPrivateMethodsSectionHeader(fio);

        addGetRowMapperMethod(fio, table, daoImplPackageName);
    }

    /**
     * Adds the sequence incrementer accessors.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSequenceIncrementerAccessors(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {

        /**
         * Add get method
         */
        FileGenUtil.writeLine("    public OracleSequenceMaxValueIncrementer getSequenceIncrementer() {", fio);
        FileGenUtil.writeLine("        return sequenceIncrementer;", fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Add set method
         */
        FileGenUtil.writeLine("    public void setSequenceIncrementer(OracleSequenceMaxValueIncrementer sequenceIncrementer) { ", fio);
        FileGenUtil.writeLine("        this.sequenceIncrementer = sequenceIncrementer;", fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the find by id method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addFindByIdMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    public %s findById(%s) {", table.getDTOName(), generateIDParameters(table));

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine("        return getSimpleJdbcTemplate().queryForObject(FIND_BY_ID_QUERY, getRowMapper(), " + generateIDQueryParameters(table) + ");", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the delete by id method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addDeleteByIdMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    public void deleteById(%s) {", generateIDParameters(table));

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine("        getSimpleJdbcTemplate().update(DELETE_BY_ID_QUERY, " + generateIDQueryParameters(table) + ");", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the delete method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addDeleteMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    public void delete(%s %s) {", table.getDTOName(), table.getCamelCase());

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine("        deleteById(" + generateDeleteMethodParameters(table) + ");", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the find all method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addFindAllMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    public List<%s> findAll() {", table.getDTOName());

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine("        return getSimpleJdbcTemplate().query(FIND_ALL_QUERY, getRowMapper());", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the save method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSaveMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    public %s save(%s %s) {", table.getDTOName(), table.getDTOName(), table.getCamelCase());

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeEmpytLines(1, fio);

        /**
         * Get the next id, if required
         */
        if (addSequenceIncrementer(table)) {
            FileGenUtil.writeLine("        //Get the next ID value from the sequence", fio);
            FileGenUtil.writeLine("        Long id = getSequenceIncrementer().nextLongValue();", fio);
            FileGenUtil.writeEmpytLines(1, fio);
        }

        FileGenUtil.writeLine("        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(getDataSource());", fio);
        FileGenUtil.writeLine("        simpleJdbcInsert.withTableName(TABLE_NAME);", fio);
        FileGenUtil.writeLine("        simpleJdbcInsert.usingColumns(" + generateColumList(table) + ");", fio);
        FileGenUtil.writeEmpytLines(1, fio);

        generateMapSqlParameterSource(fio, table, daoImplPackageName);
        FileGenUtil.writeLine("        simpleJdbcInsert.execute(sqlParameters);", fio);

        FileGenUtil.writeEmpytLines(1, fio);
        addSetIdCalls(fio, table, daoImplPackageName);

        FileGenUtil.writeLine("        return " + table.getCamelCase() + ";", fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the sequence incrementer.
     *
     * @param table the table
     * @return true, if successful
     */
    private static boolean addSequenceIncrementer(Table table) {

        boolean result = false;

        if(table.hasPrimaryKey() && !table.hasCompositePrimaryKey()) {
            for (Column column : table.getColumns()) {
                if(column.isPrimaryKey()) {
                    result = isLong(column);
                }
            }
        }

        return result;
    }

    /**
     * Checks if is long primary key.
     *
     * @param column the column
     * @return true, if is long primary key
     */
    private static boolean isLong(Column column) {
        return "Long".equals(SqlTypeMapper.getJavaType(column.getType()));
    }

    /**
     * Adds the save or update method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSaveOrUpdateMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        String methodSignature = String.format("    public %s saveOrUpdate(%s %s) {", table.getDTOName(), table.getDTOName(), table.getCamelCase());
        String saveLine = String.format("            updated%s = save(%s);", table.getFormattedName(), table.getCamelCase());
        String updateLine = String.format("            updated%s = update(%s);", table.getFormattedName(), table.getCamelCase());

        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);

        if(table.isLinkTable()) {
            FileGenUtil.writeLine("        throw new java.lang.UnsupportedOperationException(\"Method Not Implemented\");",fio);
        } else {
            FileGenUtil.writeLine("        " + table.getDTOName() + " updated" + table.getFormattedName() + ";", fio);
            FileGenUtil.writeEmpytLines(1, fio);
            generateSaveOrUpdateIfStatement(fio, table, daoImplPackageName);
            FileGenUtil.writeLine(saveLine, fio);
            FileGenUtil.writeLine("        } else {", fio);
            FileGenUtil.writeLine(updateLine, fio);
            FileGenUtil.writeLine("        }", fio);
            FileGenUtil.writeEmpytLines(1, fio);
            FileGenUtil.writeLine("        return updated" + table.getFormattedName() + ";", fio);
        }

        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the update method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addUpdateMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        String methodSignature = String.format("    public %s update(%s %s) {", table.getDTOName(), table.getDTOName(), table.getCamelCase());

        String body = String.format("        getSimpleJdbcTemplate().update(UPDATE_QUERY,%s,%s);", generateUpdateQueryString(table),generateGetIdPart(table));
        FileGenUtil.writeLine("    @Override", fio);
        FileGenUtil.writeLine(methodSignature, fio);

        if(table.isLinkTable()) {
            FileGenUtil.writeLine("        throw new java.lang.UnsupportedOperationException(\"Method Not Implemented\");",fio);
        } else {
            FileGenUtil.writeLine(body, fio);
            FileGenUtil.writeLine("        return " + table.getCamelCase() + ";", fio);
        }


        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Generate if statement.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void generateSaveOrUpdateIfStatement(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String str = null;

        if (table.hasCompositePrimaryKey()) {
            str = String.format("        if(%s.get%sID() == null) {", table.getCamelCase(), table.getFormattedName());
        } else {
            for (Column column : table.getColumns()) {
                if (column.isPrimaryKey() && isLong(column)) {
                    str = String.format("        if(%s.get%s() == 0) {", table.getCamelCase(), column.getFormattedName());
                } else {
                    str = String.format("        if(%s.get%s() == null) {", table.getCamelCase(), column.getFormattedName());
                }
                break;
            }
        }

        FileGenUtil.writeLine(str, fio);

    }

    /**
     * Adds the sql parameters.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void generateMapSqlParameterSource(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeLine("        MapSqlParameterSource sqlParameters = new MapSqlParameterSource();", fio);

        if (table.hasCompositePrimaryKey()) {
            generateMapSqlParameterSourceUsingCompositeID(fio, table);
        } else {
            generateMapSqlParameterSourceUsingSimpleID(fio, table);
        }
    }

    /**
     * Generate map sql parameter source using simple id.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void generateMapSqlParameterSourceUsingSimpleID(FileOutputStream fio, Table table) throws IOException {
        for (Column column : table.getColumns()) {

            String str = null;

            if (column.isPrimaryKey() && isLong(column)) {
                str = String.format("        sqlParameters.addValue(%s_COL,id);", column.getName());
            }  else {
                str = String.format("        sqlParameters.addValue(%s_COL,%s.%s());", column.getName(), table.getCamelCase(), FileGenUtil.generateGetterName(column));
            }
            FileGenUtil.writeLine(str, fio);
        }
    }

    /**
     * Generate map sql parameter source using composite id.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void generateMapSqlParameterSourceUsingCompositeID(FileOutputStream fio, Table table) throws IOException {
        for (Column column : table.getColumns()) {

            String str = null;

            if (column.isPrimaryKey()) {
                str = String.format("        sqlParameters.addValue(%s_COL,%s.get%sID().get%s());", column.getName(), table.getCamelCase(), table.getFormattedName(), column.getFormattedName());
            } else {
                str = String.format("        sqlParameters.addValue(%s_COL,%s.%s());", column.getName(), table.getCamelCase(), FileGenUtil.generateGetterName(column));
            }
            FileGenUtil.writeLine(str, fio);
        }
    }

    /**
     * Adds the set id calls.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSetIdCalls(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        if (!table.hasCompositePrimaryKey()) {
            for (Column column : table.getColumns()) {
                if (column.isPrimaryKey()) {
                    if(isLong(column)) {
                        String str = String.format("        %s.set%s(id);", table.getCamelCase(), column.getFormattedName());
                        FileGenUtil.writeLine(str, fio);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Adds the get row mapper method.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     */
    private static void addGetRowMapperMethod(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {

        String methodSignature = String.format("    private RowMapper<%s> getRowMapper() {", table.getDTOName());
        String classHeader = String.format("        RowMapper<%s> rowMapper = new RowMapper<%s>() {", table.getDTOName(), table.getDTOName());
        String mapRowMethodSignature = String.format("            public %s mapRow(ResultSet resultSet, int rowNum) throws SQLException {", table.getDTOName());
        String typeDeclaration = String.format("                %s %s = new %s();", table.getDTOName(), table.getCamelCase(), table.getDTOName());
        String returnLine = String.format("                return %s;", table.getCamelCase());

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine(classHeader, fio);
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine(mapRowMethodSignature, fio);
        FileGenUtil.writeEmpytLines(1, fio);

        if (table.hasCompositePrimaryKey()) {
            addIDClass(fio, table);
        }

        FileGenUtil.writeEmpytLines(1, fio);
        /**
         * Add DTO type declaration
         */
        FileGenUtil.writeLine(typeDeclaration, fio);

        addSetMethods(fio, table);

        FileGenUtil.writeLine(returnLine, fio);

        FileGenUtil.writeLine("            }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("        };", fio);
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("        return rowMapper;", fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the id class.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addIDClass(FileOutputStream fio, Table table) throws IOException {
        /**
         * Add ID Class as this table has a composite ID
         */
        String idClassDef = String.format("                %sID %sID = new %sID();", table.getFormattedName(), table.getCamelCase(), table.getFormattedName());
        FileGenUtil.writeLine(idClassDef, fio);

        for (Column column : table.getColumns()) {
            if(column.isPrimaryKey()) {
                String setMethodDesc = String.format("                %sID.set%s(resultSet.%s(%s)); ",
                        table.getCamelCase(),
                        column.getFormattedName(),
                        SqlTypeMapper.getResultSetGetMethod(column.getType()),
                        column.getName() + "_COL");
                FileGenUtil.writeLine(setMethodDesc, fio);
            }
        }
    }

    /**
     * Adds the set methods.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSetMethods(FileOutputStream fio, Table table) throws IOException {

        if (table.hasCompositePrimaryKey()) {
            addSetMethodsForCompositeID(fio, table);
        } else {
            addSetMethodsForSimpleID(fio, table);
        }
    }

    private static void addSetMethodsForSimpleID(FileOutputStream fio, Table table) throws IOException {
        for (Column column : table.getColumns()) {
            String setMethodDesc = String.format("                %s.set%s(resultSet.%s(%s)); ", table.getCamelCase(), column.getFormattedName(), SqlTypeMapper.getResultSetGetMethod(column.getType()), column.getName() + "_COL");
            FileGenUtil.writeLine(setMethodDesc, fio);
        }
    }

    /**
     * Adds the set methods for composite id.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSetMethodsForCompositeID(FileOutputStream fio, Table table) throws IOException {

        String setIdMethod =  String.format("                %s.set%sID(%sID);",table.getCamelCase(),table.getFormattedName(),table.getCamelCase());
        FileGenUtil.writeLine(setIdMethod, fio);

        /**
         * Add set methods for non-primary key columns
         */
        for (Column column : table.getColumns()) {
            if (!column.isPrimaryKey()) {
                String setMethodDesc = String.format("                %s.set%s(resultSet.%s(%s)); ",
                        table.getCamelCase(),
                        column.getFormattedName(),
                        SqlTypeMapper.getResultSetGetMethod(column.getType()),
                        column.getName() + "_COL");
                FileGenUtil.writeLine(setMethodDesc, fio);
            }
        }
    }

    /**
     * Generate update clause.
     *
     * @param table the table
     * @return the string
     */
    private static String generateUpdateClause(Table table) {
        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder updateClause = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            Column column = table.getColumns().get(index);

            /**
             * Don't add primay keys to update statement
             */
            if (!column.isPrimaryKey()) {

                /**
                 * Add coma if necessary
                 */
                if ((index > 0) && (updateClause.length() > 0)) {
                    updateClause.append(", ");
                }

                /**
                 * Append column name
                 */
                updateClause.append(column.getName() + " = ?");
            }
        }

        return updateClause.toString();
    }

    /**
     * Generate up stringate method parameters.
     *
     * @param table the table
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String generateUpdateQueryString(Table table) throws IOException {
        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder parameters = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            Column column = table.getColumns().get(index);

            /**
             * Don't add primay keys to update statement
             */
            if (!column.isPrimaryKey()) {

                /**
                 * Add coma if necessary
                 */
                if ((index > 0) && (parameters.length() > 0)) {
                    parameters.append(",");
                }

                /**
                 *
                 */
                String str = String.format("%s.get%s()", table.getCamelCase(), column.getFormattedName());
                parameters.append(str);
            }
        }

        return parameters.toString();
    }

    /**
     * Generate id for where clause.
     *
     * @param table the table
     * @return the string
     */
    private static final String generateGetIdPart(Table table) throws IOException {

        String idString = "";

        if(table.hasCompositePrimaryKey()) {
            idString = generateGetIDPartForCompoisteID(table);
        } else {
            idString = generateGetIDPartForSimpleID(table);
        }

        return idString;
    }

    /**
     * Generate get id part for compoiste id.
     *
     * @param table the table
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static final String generateGetIDPartForCompoisteID(Table table) throws IOException {
        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder parameters = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            Column column = table.getColumns().get(index);

            /**
             * Don't add primay keys to update statement
             */
            if (column.isPrimaryKey()) {

                /**
                 * Add coma if necessary
                 */
                if ((index > 0) && (parameters.length() > 0)) {
                    parameters.append(",");
                }

                /**
                 *
                 */
                String str = String.format("%s.get%sID().get%s()", table.getCamelCase(), table.getFormattedName(), column.getFormattedName());
                parameters.append(str);
            }
        }

        return parameters.toString();
    }

    /**
     * Generate get id part for simple id.
     *
     * @param table the table
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static final String generateGetIDPartForSimpleID(Table table) throws IOException {
        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder parameters = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            Column column = table.getColumns().get(index);

            /**
             * Don't add primay keys to update statement
             */
            if (column.isPrimaryKey()) {

                /**
                 * Add coma if necessary
                 */
                if ((index > 0) && (parameters.length() > 0)) {
                    parameters.append(",");
                }

                /**
                 *
                 */
                String str = String.format("%s.get%s()", table.getCamelCase(), column.getFormattedName());
                parameters.append(str);
            }
        }

        return parameters.toString();
    }


    /**
     * Generate select list.
     *
     * @param table the table
     * @return the string
     */
    private static String generateSelectList(Table table) {

        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder columns = new StringBuilder();

        /**
         * Iterate through the list of columns and add name to list
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Add comma if necessary
             */
            if (index > 0) {
                columns.append(", ");
            }

            /**
             * Get the column name
             */
            String columnName = table.getColumns().get(index).getName();

            /**
             * Add column name to list
             */
            columns.append(columnName);

        }

        return columns.toString();
    }


    /**
     * Generate colum list.
     *
     * @param table the table
     * @return the string
     */
    private static String generateColumList(Table table) {

        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder columns = new StringBuilder();

        /**
         * Iterate through the list of columns and add name to list
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Add comma if necessary
             */
            if (index > 0) {
                columns.append(", ");
            }

            /**
             * Get the column name
             */
            String columnName = table.getColumns().get(index).getName();

            /**
             * Add column name to list
             */
            columns.append(columnName + "_COL");

        }

        return columns.toString();
    }

    /**
     * Generate method parameters.
     *
     * @param table the table
     * @return the string
     */
    private static String generateIDParameters(Table table) {
        String params = null;

        if (table.hasCompositePrimaryKey()) {
            params = generateCompositeIDParam(table);
        } else {
            params = generateSimpleIDParam(table);
        }

        return params;
    }

    /**
     * Generate simple id param.
     *
     * @param table the table
     * @return the string
     */
    private static String generateSimpleIDParam(Table table) {
        /**
         * The param to return
         */
        String param = null;

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Get primary key name
             */
            Column column = table.getColumns().get(index);

            if (column.isPrimaryKey()) {
                param = String.format("%s %s", SqlTypeMapper.getJavaType(column.getType()), column.getCamelCase());
                break;
            }
        }

        return param;
    }

    /**
     * Generate composite id param.
     *
     * @param table the table
     * @return the string
     */
    private static String generateCompositeIDParam(Table table) {
        return String.format("%sID %sID", table.getFormattedName(), table.getCamelCase());
    }

    /**
     * Generate delete by id query parameters.
     *
     * @param table the table
     * @return the string
     */
    private static String generateIDQueryParameters(Table table) {
        String params = null;

        if (table.hasCompositePrimaryKey()) {
            params = generateCompositeIDQueryParam(table);
        } else {
            params = generateSimpleIDQueryParam(table);
        }

        return params;
    }

    private static String generateSimpleIDQueryParam(Table table) {
        /**
         * The param to return
         */
        String param = null;

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Get primary key name
             */
            Column column = table.getColumns().get(index);

            if (column.isPrimaryKey()) {
                param = column.getCamelCase();
                break;
            }
        }

        return param;
    }

    /**
     * Generate composite id param.
     *
     * @param table the table
     * @return the string
     */
    private static String generateCompositeIDQueryParam(Table table) {

        /**
         * The param string to return
         */
        StringBuilder param = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Get primary key name
             */
            Column column = table.getColumns().get(index);

            if (column.isPrimaryKey()) {
                if (index > 0) {
                    param.append(", ");
                }

                /**
                 * Append column name
                 */
                String classAndMethod = String.format("%sID.get%s()", table.getCamelCase(), column.getFormattedName());
                param.append(classAndMethod);
            }
        }

        return param.toString();
    }

    /**
     * Generate delete method parameters.
     *
     * @param table the table
     * @return the string
     */
    private static String generateDeleteMethodParameters(Table table) {
        String params = null;

        if (table.hasCompositePrimaryKey()) {
            params = generateDeleteMethodParametersUsingCompositeId(table);
        } else {
            params = generateDeleteMethodParametersUsingSimpleId(table);
        }

        return params;
    }

    /**
     * Generate delete method parameters using simple id.
     *
     * @param table the table
     * @return the string
     */
    private static String generateDeleteMethodParametersUsingSimpleId(Table table) {
        /**
         * The param to return
         */
        String param = null;

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            /**
             * Get primary key name
             */
            Column column = table.getColumns().get(index);

            if (column.isPrimaryKey()) {

                /**
                 * Append column name
                 */
                param = String.format("%s.get%s()", table.getCamelCase(), column.getFormattedName());
                break;
            }
        }

        return param;
    }

    /**
     * Generate delete method parameters using composite id.
     *
     * @param table the table
     * @return the string
     */
    private static String generateDeleteMethodParametersUsingCompositeId(Table table) {
        return String.format("%s.get%sID()", table.getCamelCase(), table.getFormattedName());
    }

    /**
     * Gets the class file.
     *
     * @param outputDir the output dir
     * @param table the table
     * @return the class file
     */
    public static File getClassFile(File outputDir, Table table) {
        /**
         * Build the file name
         */
        String fileName = String.format("%s%s%s.java", outputDir.getAbsolutePath(), File.separator, table.getDAOName());

        /**
         * Return as file object
         */
        return new File(fileName);
    }

    /**
     * Adds the clas declaration.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addClassDeclaration(FileOutputStream fio, Table table, String daoImplPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine(String.format("public class %s extends SimpleJdbcDaoSupport implements I%s {", table.getDAOName(), table.getDAOName()), fio);
    }

    /**
     * Generate pk where clause.
     *
     * @param table the table
     * @return the string
     */
    private static String generatePkWhereClause(Table table) {
        /**
         * Use a StringBuild to build the column list
         */
        StringBuilder whereClause = new StringBuilder();

        /**
         * Iterate through the list of keys and add name to clause
         */
        for (int index = 0; index < table.getColumns().size(); index++) {

            Column column = table.getColumns().get(index);

            if (column.isPrimaryKey()) {
                /**
                 * Add AND if necessary
                 */
                if (index > 0) {
                    whereClause.append(" AND ");
                }

                whereClause.append(table.getColumns().get(index).getName() + " = ?");
            }
        }

        return whereClause.toString();
    }

}

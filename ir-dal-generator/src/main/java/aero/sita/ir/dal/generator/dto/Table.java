package aero.sita.ir.dal.generator.dto;

import java.util.ArrayList;
import java.util.List;

import aero.sita.ir.dal.generator.util.FileGenUtil;

/**
 * The Class Table.
 */
public class Table {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    /** The name. */
    private String name;

    /** The primary key. */
    private List<String> primaryKey = new ArrayList<String>();

    /** The columns. */
    private List<Column> columns = new ArrayList<Column>();

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    /**
     * Instantiates a new table.
     */
    public Table() {
    }

    /**
     * Instantiates a new table.
     * 
     * @param name the name
     */
    public Table(String name) {
        setName(name);
    }

    // ===========================================
    // Public Methods
    // ===========================================

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the formatted name.
     * 
     * @return the formatted name
     */
    public String getFormattedName() {
        return FileGenUtil.capitalise(getName());
    }

    /**
     * Gets the dTO name.
     * 
     * @return the dTO name
     */
    public String getDTOName() {
        return getFormattedName() + "DTO";
    }

    /**
     * Gets the dAO name.
     *
     * @return the dAO name
     */
    public String getDAOName() {
        return getFormattedName() + "DAO";
    }
    
    /**
     * Gets the dAO interface name.
     *
     * @return the dAO interface name
     */
    public String getDAOInterfaceName() {
        return "I" + getFormattedName() + "DAO";
    }


    /**
     * Camel case.
     *
     * @return the string
     */
    public String getCamelCase() {
        return FileGenUtil.camelCase(getName());
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the primary key.
     * 
     * @return the primary key
     */
    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets the primary key.
     * 
     * @param primaryKey the new primary key
     */
    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Adds the primary key.
     * 
     * @param key the key
     */
    public void addPrimaryKey(String key) {
        getPrimaryKey().add(key);
    }

    /**
     * Gets the columns.
     * 
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Adds the column.
     * 
     * @param column the column
     */
    public void addColumn(Column column) {
        getColumns().add(column);
    }

    /**
     * Checks for composite primary key.
     *
     * @return true, if successful
     */
    public boolean hasCompositePrimaryKey() {
        return (getPrimaryKey().size() > 1);
    }
    
    /**
     * Checks for primary key.
     *
     * @return true, if successful
     */
    public boolean hasPrimaryKey() {
        return (getPrimaryKey().size() > 0);
    }
    
    /**
     * Checks if is link table.
     *
     * @return true, if is link table
     */
    public boolean isLinkTable() {
        return (getColumns().size() == getPrimaryKey().size());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Table other = (Table) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================
}

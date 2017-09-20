package aero.sita.ir.dal.generator.dto;

import org.apache.commons.lang.StringUtils;

import aero.sita.ir.dal.generator.cli.CLIDelegate;
import aero.sita.ir.dal.generator.util.FileGenUtil;

/**
 * The Class Field.
 */
public class Column {

	// ===========================================
	// Public Members
	// ===========================================

	// ===========================================
	// Private Members
	// ===========================================

	/** The name. */
	private String name;

	/** The prefix. */
	private String prefix;

    /** The type. */
	private int type;

	/** The type name. */
	private String typeName;

	/** The size. */
	private int size;

	/** The table. */
	private Table table;

	/** The primary key. */
	private boolean primaryKey;

	// ===========================================
	// Static initialisers
	// ===========================================

	// ===========================================
	// Constructors
	// ===========================================

    /**
	 * Instantiates a new field.
	 */
	public Column() {
	}

	/**
	 * Instantiates a new field.
	 *
	 * @param name the name
	 * @param type the type
	 * @param size the size
	 * @param table the table
	 */
	public Column(String name, int type, int size, Table table) {
		setName(name);
		setType(type);
		setSize(size);
		setTable(table);
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
	 * Gets the name as property.
	 *
	 * @return the name as property
	 */
	public String getNameAsProperty() {
	    String name = getName();

	    if((System.getProperty(CLIDelegate.REMOVE_COLUMN_PREFIX) != null) &&
	        name.contains("_")) {
	        name = StringUtils.substringAfter(name, "_");
	    }

	    return name;
	}

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
	public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the formatted name.
     *
     * @return the formatted name
     */
    public String getFormattedName() {
        return FileGenUtil.capitalise(getNameAsProperty());
    }

    /**
     * Gets the formatted name.
     *
     * @return the formatted name
     */
    public String getCamelCase() {
        return FileGenUtil.camelCase(getNameAsProperty());
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
     * Sets the prefix.
     *
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the type name.
	 *
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Sets the type name.
	 *
	 * @param typeName the new type name
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Sets the table.
	 *
	 * @param table the new table
	 */
	public void setTable(Table table) {
		this.table = table;
	}

    /**
     * Checks if is primary key.
     *
     * @return true, if is primary key
     */
	public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets the primary key.
     *
     * @param primaryKey the new primary key
     */
	public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Column other = (Column) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
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

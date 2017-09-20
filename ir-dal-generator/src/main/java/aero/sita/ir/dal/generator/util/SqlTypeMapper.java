package aero.sita.ir.dal.generator.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class SqlTypeMapper.
 */
public class SqlTypeMapper {


	// ===========================================
	// Public Members
	// ===========================================

	// ===========================================
	// Private Members
	// ===========================================

	/** The types. */
	private static Map<Integer, String> types = new HashMap<Integer, String>();
	
	/** The get methods. */
	private static Map<Integer, String> getMethods = new HashMap<Integer, String>();
	
	// ===========================================
	// Static initialisers
	// ===========================================
	static {	
		types.put(Types.CHAR, "String");
		types.put(Types.VARCHAR, "String");
		types.put(Types.LONGVARCHAR, "String");
		types.put(Types.NUMERIC,"Long");
		types.put(Types.DECIMAL,"Long");
		types.put(Types.BIT,"Boolean");
		types.put(Types.TINYINT,"Integer");
		types.put(Types.SMALLINT,"Integer");
		types.put(Types.INTEGER,"Integer");
		types.put(Types.BIGINT,"Long");
		types.put(Types.REAL,"Float");
		types.put(Types.FLOAT,"Double");
		types.put(Types.DOUBLE,"Double");
		types.put(Types.BINARY,"byte[]");
		types.put(Types.VARBINARY,"byte[]");
		types.put(Types.LONGVARBINARY,"byte[]");
		types.put(Types.DATE,"java.util.Date");
		types.put(Types.TIME,"java.util.Date");
		types.put(Types.TIMESTAMP,"java.util.Date");
		types.put(Types.CLOB,"Clob");
		types.put(Types.BLOB,"java.sql.Blob");
		types.put(Types.ARRAY,"Array");		
			
		getMethods.put(Types.CHAR, "getString");
		getMethods.put(Types.VARCHAR, "getString");
		getMethods.put(Types.LONGVARCHAR, "getString");
		getMethods.put(Types.NUMERIC,"getLong");
		getMethods.put(Types.DECIMAL,"getLong");
		getMethods.put(Types.BIT,"getBoolean");
		getMethods.put(Types.TINYINT,"getInteger");
		getMethods.put(Types.SMALLINT,"getInteger");
		getMethods.put(Types.INTEGER,"getInteger");
		getMethods.put(Types.BIGINT,"getLong");
		getMethods.put(Types.REAL,"getFloat");
		getMethods.put(Types.FLOAT,"getDouble");
		getMethods.put(Types.DOUBLE,"getDouble");
		getMethods.put(Types.BINARY,"getBytes");
		getMethods.put(Types.VARBINARY,"getBytes");
		getMethods.put(Types.LONGVARBINARY,"getBytes");
		getMethods.put(Types.DATE,"getDate");
		getMethods.put(Types.TIME,"getDate");
		getMethods.put(Types.TIMESTAMP,"getDate");
		getMethods.put(Types.CLOB,"getClob");
		getMethods.put(Types.BLOB,"getBlob");
		getMethods.put(Types.ARRAY,"getArray");	
	}
	// ===========================================
	// Constructors
	// ===========================================

	/**
	 * Instantiates a new sql type mapper.
	 */
	private SqlTypeMapper() {
	}

	// ===========================================
	// Public Methods
	// ===========================================

	/**
	 * Gets the type.
	 *
	 * @param type the type
	 * @return the type
	 */
	public static String getJavaType(Integer type) {
		return getTypes().get(type);
	}
	
	/**
	 * Gets the result set get method.
	 *
	 * @param type the type
	 * @return the result set get method
	 */
	public static String getResultSetGetMethod(Integer type) {
		return getGetMethods().get(type);		
	}
	
	// ===========================================
	// Protected Methods
	// ===========================================

	// ===========================================
	// Private Methods
	// ===========================================
	
	/**
	 * Gets the types.
	 *
	 * @return the types
	 */
	private static Map<Integer, String> getTypes() {
		return types;
	}

	/**
	 * Gets the gets the methods.
	 *
	 * @return the gets the methods
	 */
	private static Map<Integer, String> getGetMethods() {
		return getMethods;
	}

}

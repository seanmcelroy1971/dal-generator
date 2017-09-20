package aero.sita.ir.dal.generator.schema;

import java.util.List;

import aero.sita.ir.dal.generator.dto.Table;

/**
 * The Interface IHandler.
 */
public interface IHandler {
	
	/**
	 * Adds the table.
	 *
	 * @param table the table
	 */
	void addTable(Table table);
	
	/**
	 * Gets the tables.
	 *
	 * @return the tables
	 */
	List<Table> getTables();
}

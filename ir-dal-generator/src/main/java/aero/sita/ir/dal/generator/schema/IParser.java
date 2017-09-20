package aero.sita.ir.dal.generator.schema;

import java.util.List;

import aero.sita.ir.dal.generator.DalGeneratorException;
import aero.sita.ir.dal.generator.dto.Table;

/**
 * The Interface IParser.
 */
public interface IParser {
	
	/**
	 * Parses the.
	 *
	 * @return the list
	 */
	List<Table> parse() throws DalGeneratorException;
}

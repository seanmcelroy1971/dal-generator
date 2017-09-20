package aero.sita.ir.dal.generator.schema.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import aero.sita.ir.dal.generator.dto.Table;
import aero.sita.ir.dal.generator.schema.IHandler;

/**
 * The Class Handler.
 */
public class MetaDataHandler implements IHandler {

	// ===========================================
	// Public Members
	// ===========================================

	// ===========================================
	// Private Members
	// ===========================================

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(MetaDataHandler.class);
	
	/** The tables. */
	private List<Table> tables = new ArrayList<Table>();
	
	// ===========================================
	// Static initialisers
	// ===========================================

	// ===========================================
	// Constructors
	// ===========================================

	/**
	 * Instantiates a new handler.
	 */
	public MetaDataHandler() {
	}
	
	// ===========================================
	// Public Methods
	// ===========================================

	/* (non-Javadoc)
	 * @see aero.sita.ir.dal.generator.schema.IHandler#getTables()
	 */
	public List<Table> getTables() {
		return tables;
	}

	/* (non-Javadoc)
	 * @see aero.sita.ir.dal.generator.schema.IHandler#addTable(aero.sita.ir.dal.generator.dto.Table)
	 */
	public void addTable(Table table) {
		LOGGER.info("Processing table [" + table.getName() + "]");
		getTables().add(table);
	}

	// ===========================================
	// Protected Methods
	// ===========================================

	// ===========================================
	// Private Methods
	// ===========================================
	

}

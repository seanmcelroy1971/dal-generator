/*
 * This code contains copyright information which is the proprietary property
 * of SITA Advanced Travel Solutions. No part of this code may be reproduced,
 * stored or transmitted in any form without the prior written permission of
 * SITA Advanced Travel Solutions.
 *
 * Copyright SITA Advanced Travel Solutions 2001-2011
 * All rights reserved.
 */
package aero.sita.ir.dal.generator.cli;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The Class CLIDelegate.
 */
public class CLIDelegate {

	// ===========================================
	// Public Members
	// ===========================================

	/** The Constant HELP. */
	public static final String HELP = "help";

	/** The Constant HELP_DESC. */
	public static final String HELP_DESC = "Prints usage information";

    /** The Constant DAL_PACKAGE_NAME. */
    public static final String DAL_PACKAGE_NAME = "dalPackage";

    /** The Constant DAL_PACKAGE_NAME_DESC. */
    public static final String DAL_PACKAGE_NAME_DESC = "The DAL package name";

	/** The Constant DTO_PACKAGE_NAME. */
	public static final String DTO_PACKAGE_NAME = "dtoPackage";

	/** The Constant DTO_PACKAGE_NAME_DESC. */
	public static final String DTO_PACKAGE_NAME_DESC = "The DTO package name";

    /** The Constant OUTPUT_DIR. */
    public static final String OUTPUT_DIR = "outputDir";

    /** The Constant OUTPUT_DIR_DESC. */
    public static final String OUTPUT_DIR_DESC = "Location to save generated java classes";

    /** The Constant REMOVE_COLUMN_PREFIX. */
    public static final String REMOVE_COLUMN_PREFIX = "removeColumnPrefix";

    /** The Constant REMOVE_COLUMN_PREFIX_DESC. */
    public static final String REMOVE_COLUMN_PREFIX_DESC = "Instruct the application to remove column name prefixes.";

	// ===========================================
	// Private Members
	// ===========================================

	/** The Constant CMD_LINE. */
	private static final String CMD_LINE = "java -jar ir-dal-generator.jar <options>";

	/** The options. */
	private static Options options;

	/** The command line. */
	private static CommandLine commandLine;

	// ===========================================
	// Static initialisers
	// ===========================================

	// ===========================================
	// Constructors
	// ===========================================


    /**
	 * Instantiates a new CLI delegate.
	 *
	 */
	public CLIDelegate() {
	}

	// ===========================================
	// Public Methods
	// ===========================================

	public void parse(String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        setCommandLine(parser.parse( getOptions(), args));
	}


	/**
	 * Show usage.
	 */
	public void showUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( CMD_LINE, options );
	}

	/**
	 * Checks for option.
	 *
	 * @param option the option
	 * @return the boolean
	 */
	public Boolean hasOption(String option) {
	    return getCommandLine().hasOption(option);
	}

   /**
    * Gets the option value.
    *
    * @param option the option
    * @return the option value
    */
   public String getOptionValue(String option) {
        return getCommandLine().getOptionValue(option);
   }

   // ===========================================
   // Protected Methods
   // ===========================================

   // ===========================================
   // Private Methods
   // ===========================================

   /**
    * Sets the command line.
    *
    * @param commandLine the new command line
    */
   private void setCommandLine(CommandLine commandLine) {
        CLIDelegate.commandLine = commandLine;
   }

   /**
    * Gets the command line.
    *
    * @return the command line
    */
   private CommandLine getCommandLine() {
       return commandLine;
   }

	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	private Options getOptions() {
		if(options == null) {
			options = new Options();

			/**
			 * Create help option
			 */
			Option helpOption = new Option(HELP, false, HELP_DESC);

			/**
			 * Create dao impl package name option
			 */
			Option dalPackageNameOption = new Option(DAL_PACKAGE_NAME, true, DAL_PACKAGE_NAME_DESC);
			dalPackageNameOption.setArgName(DAL_PACKAGE_NAME);

			/**
			 * Create dto PackageName option
			 */
			Option dtoPackageNameOption = new Option(DTO_PACKAGE_NAME, true, DTO_PACKAGE_NAME_DESC);
			dtoPackageNameOption.setArgName(DTO_PACKAGE_NAME);

			/**
			 * Create password option
			 */
			Option outputDirOption = new Option(OUTPUT_DIR, true, OUTPUT_DIR_DESC);
			outputDirOption.setArgName(OUTPUT_DIR);

            /**
             * Create remove column prefix option
             */
			Option removeColumnPrefix = new Option(REMOVE_COLUMN_PREFIX, false, REMOVE_COLUMN_PREFIX_DESC);
			removeColumnPrefix.setArgName(REMOVE_COLUMN_PREFIX);
			removeColumnPrefix.setRequired(false);

			/**
			 * Add option groups to options
			 */
			options.addOption(helpOption);
			options.addOption(dalPackageNameOption);
			options.addOption(dtoPackageNameOption);
			options.addOption(outputDirOption);
			options.addOption(removeColumnPrefix);
		}
		return options;
	}
}

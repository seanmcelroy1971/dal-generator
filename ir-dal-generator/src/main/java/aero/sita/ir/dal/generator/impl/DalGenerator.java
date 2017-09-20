package aero.sita.ir.dal.generator.impl;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import aero.sita.ir.dal.generator.DalGeneratorException;
import aero.sita.ir.dal.generator.IDalGenerator;
import aero.sita.ir.dal.generator.cli.CLIDelegate;
import aero.sita.ir.dal.generator.dto.Table;
import aero.sita.ir.dal.generator.schema.IParser;

/**
 * The Class DalGenerator.
 */
public class DalGenerator implements IDalGenerator {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(DalGenerator.class);

    /** The Constant SPRING_CONFIG_FILE. */
    private static final String SPRING_CONFIG_FILE = "config/ir-da-generator.xml";

    /** The Constant LOG4J_CONFIG_FILE. */
    private static final String LOG4J_CONFIG_FILE = "config/log4j.xml";

    /** The Constant APP_CLASS_NAME. */
    private static final String APP_CLASS_NAME = "dalGenerator";

    /** The spring application context. */
    private static ApplicationContext ctx;

    /** The parser. */
    private IParser metaDataParser;

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    /**
     * Instantiates a new dal generator.
     */
    public DalGenerator() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        /**
         * Find log4j configuration file on class path
         */
        final URL resource = DalGenerator.class.getClassLoader().getResource(LOG4J_CONFIG_FILE);

        /**
         * Initialise log4j
         */
        DOMConfigurator.configure(resource);

        /**
         * Create a CLIDelegate
         */
        CLIDelegate cliDelegate = new CLIDelegate();

        try {

            /**
             * Parse the command line args
             */
            cliDelegate.parse(args);

            /**
             * Determine what to do next
             */
            if (cliDelegate.hasOption(CLIDelegate.HELP)) {
                /**
                 * Show usage
                 */
                cliDelegate.showUsage();
            } else {
                /**
                 * Check if we need to remove column prefixes.
                 */
                if(cliDelegate.hasOption(CLIDelegate.REMOVE_COLUMN_PREFIX)) {
                    System.setProperty(CLIDelegate.REMOVE_COLUMN_PREFIX, "yes");
                }

                String outputDir = cliDelegate.getOptionValue(CLIDelegate.OUTPUT_DIR);
                String dalPackageName = cliDelegate.getOptionValue(CLIDelegate.DAL_PACKAGE_NAME);
                String dtoPackageName = cliDelegate.getOptionValue(CLIDelegate.DTO_PACKAGE_NAME);

                IDalGenerator dalGenerator = (DalGenerator)getCtx().getBean(APP_CLASS_NAME);
                dalGenerator.execute(new File(outputDir), dalPackageName, dtoPackageName);
            }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            cliDelegate.showUsage();
        }
    }

    /**
     * Gets the meta data parser.
     *
     * @return the meta data parser
     */
    public IParser getMetaDataParser() {
        return this.metaDataParser;
    }

    /**
     * Sets the meta data parser.
     *
     * @param metaDataParser the new meta data parser
     */
    public void setMetaDataParser(IParser metaDataParser) {
        this.metaDataParser = metaDataParser;
    }


    /**
     * Execute.
     *
     * @param outputDir the output dir
     * @param daoImplPackageName the dao impl package name
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     */
    public void execute(File outputDir, String dalPackageName, String dtoPackageName) {
        try {
            /**
             * Parse the database meta data and create a
             * list of table objects
             */
            List<Table> tables = getMetaDataParser().parse();

            /**
             * Generate the DAL objects
             */
            generateDalObjects(outputDir, tables, dalPackageName, dtoPackageName);
        } catch (DalGeneratorException e) {
            LOGGER.error(e.getMessage());
        }
    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    /**
     * Gets the ctx.
     *
     * @return the ctx
     */
    private static ApplicationContext getCtx() {
        if(ctx == null) {
            ctx = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
        }
        return ctx;
    }

    /**
     * Generate dal objects.
     *
     * @param outputDir the output dir
     * @param tables the tables
     * @param daoImplPackageName the dao impl package name
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     */
    private void generateDalObjects(File outputDir, List<Table> tables, String dalPackageName, String dtoPackageName) {

        /**
         * Create relative package names
         */
        String daoInterfacePackage = String.format("%s.dao", dalPackageName);
        String daoImplPackage = String.format("%s.dao", dalPackageName);

        for(Table table : tables) {
            LOGGER.info("Generating DAL objects for table [" + table.getName() + "]");

            if("STATS".equals(table.getName())) {
                LOGGER.info(table.getName());
            }

            /**
             * Generate DTO's
             */
            DtoGenerator.generateDTO(outputDir, table, dtoPackageName);

            /**
             * Create the DAL Exception File
             */
            final URL dalExceptionTemplateURL = DalGenerator.class.getClassLoader().getResource("DALExceptionTemplate.txt");
            File dalExceptionTemplateFile = new File(dalExceptionTemplateURL.getFile());
            DaoInterfaceGenerator.createDALExceptionClass(outputDir, dalExceptionTemplateFile , dalPackageName);


            /**
             * Create the Generic DAO File
             */
            final URL genDAOTemplateURL = DalGenerator.class.getClassLoader().getResource("GenericDAOTemplate.txt");
            File genDAOTemplateFile = new File(genDAOTemplateURL.getFile());
            DaoInterfaceGenerator.createGenericDAOClass(outputDir, genDAOTemplateFile , dalPackageName, dalPackageName + ".dao");

            /**
             * Generated DAO Interfaces
             */
            DaoInterfaceGenerator.generateDaoInterface(outputDir, table, dalPackageName + ".dao", dtoPackageName);

            /**
             * If this table has a composite primary key
             * generate a DTO to hold the key values
             */
            if(table.hasCompositePrimaryKey()) {
                DtoGenerator.generateIDClass(outputDir, table, dtoPackageName);
            }

            /**
             * Generate DAO's
             */
            DaoImplGenerator.generateDAO(outputDir, table, dalPackageName, daoImplPackage, daoInterfacePackage, dtoPackageName);
        }
    }

}

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

public class DaoInterfaceGenerator {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    private static final Logger LOGGER = Logger.getLogger(DaoInterfaceGenerator.class);

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    private DaoInterfaceGenerator() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    /**
     * Generate dao interfaces.
     */
    public static void generateDaoInterface(File outputDir, Table table, String daoIntPackageName, String dtoPackageName) {
        if(!FileGenUtil.isLinkTable(table)) {

            FileOutputStream fio = null;

            try {
                /**
                 * Create the absolute output dir, which is the output dir supplied
                 * at the command line, plus the directory structure that maps to
                 * the package name
                 */
                File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, daoIntPackageName);
                FileUtils.forceMkdir(absOutputDir);

                /**
                 * Generate the interface file
                 */
                File classFile = getInterfaceFile(absOutputDir, table);

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
                FileGenUtil.addPackageDeclaration(fio, daoIntPackageName);

                /**
                 * Add imports
                 */
                addImports(fio, table, daoIntPackageName, dtoPackageName);

                /**
                 * Add Interface
                 */
                addInterface(fio, table, daoIntPackageName, dtoPackageName);

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            } finally {
                IOUtils.closeQuietly(fio);
            }
        }
    }

    /**
     * Creates the generic dao class.
     *
     * @param outputDir the output dir
     * @param genericDAOTemplate the generic dao template
     * @param daoIntPackageName the dao int package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void createGenericDAOClass(File outputDir, File genericDAOTemplate, String dalPackage, String daoIntPackageName) {

        FileOutputStream fio = null;

        try {
            /**
             * Create the absolute output dir, which is the output dir supplied
             * at the command line, plus the directory structure that maps to
             * the package name
             */
            File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, daoIntPackageName);
            FileUtils.forceMkdir(absOutputDir);

            /**
             * Generate the interface file
             */
            File daoFile = getGenericDAOFile(absOutputDir);

            /**
             * Open the file for writing
             */
            fio = FileUtils.openOutputStream(daoFile);

            /**
             * Write file header;
             */
            FileGenUtil.addFileHeader(fio);

            /**
             * Copy generaic dao, but add correct package name
             */
            FileGenUtil.addPackageDeclaration(fio, daoIntPackageName);
            FileGenUtil.writeEmpytLines(1, fio);

            /**
             * Add import statements
             */
            FileGenUtil.addImport(fio, dalPackage + ".DALException" );
            FileGenUtil.writeEmpytLines(1, fio);

            /**
             * Copy the file
             */
            for (String line : FileUtils.readLines(genericDAOTemplate)) {
                FileGenUtil.writeLine(line, fio);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(fio);
        }
    }

    /**
     * Creates the dal exception class.
     *
     * @param outputDir the output dir
     * @param genericDAOTemplate the generic dao template
     * @param daoIntPackageName the dao int package name
     */
    public static void createDALExceptionClass(File outputDir, File genericDAOTemplate, String daoIntPackageName) {

        FileOutputStream fio = null;

        try {
            /**
             * Create the absolute output dir, which is the output dir supplied
             * at the command line, plus the directory structure that maps to
             * the package name
             */
            File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, daoIntPackageName);
            FileUtils.forceMkdir(absOutputDir);

            /**
             * Generate the exception file
             */
            File dalExceptionFile = getDALExceptionFile(absOutputDir);

            /**
             * Open the file for writing
             */
            fio = FileUtils.openOutputStream(dalExceptionFile);

            /**
             * Write file header;
             */
            FileGenUtil.addFileHeader(fio);

            /**
             * Copy generaic dao, but add correct package name
             */
            FileGenUtil.addPackageDeclaration(fio, daoIntPackageName);
            FileGenUtil.writeEmpytLines(1, fio);

            /**
             * Copy the file
             */
            for (String line : FileUtils.readLines(genericDAOTemplate)) {
                FileGenUtil.writeLine(line, fio);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(fio);
        }
    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    /**
     * Gets the class file.
     *
     * @param outputDir the output dir
     * @param table the table
     * @return the class file
     */
    private static File getInterfaceFile(File outputDir, Table table) {
        /**
         * Build the file name
         */
        String fileName = String.format("%s%s%s.java", outputDir.getAbsolutePath(), File.separator, table.getDAOInterfaceName());

        /**
         * Return as file object
         */
        return new File(fileName);
    }

    /**
     * Gets the generic dao file.
     *
     * @param outputDir the output dir
     * @return the generic dao file
     */
    private static File getGenericDAOFile(File outputDir) {
        /**
         * Build the file name
         */
        String fileName = String.format("%s%sIGenericDAO.java", outputDir.getAbsolutePath(), File.separator);

        /**
         * Return as file object
         */
        return new File(fileName);
    }

    /**
     * Gets the dAL exception file.
     *
     * @param outputDir the output dir
     * @return the dAL exception file
     */
    private static File getDALExceptionFile(File outputDir) {
        /**
         * Build the file name
         */
        String fileName = String.format("%s%sDALException.java", outputDir.getAbsolutePath(), File.separator);

        /**
         * Return as file object
         */
        return new File(fileName);
    }

    /**
     * Adds the imports.
     *
     * @param fio the fio
     * @param table the table
     * @param daoImplPackageName the dao package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addImports(FileOutputStream fio, Table table, String daoIntPackageName, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addImport(fio, generateAbsDTOName(table, dtoPackageName));

        /**
         * Need to ad ID imports if table has composite key
         */
        if (table.hasCompositePrimaryKey()) {
            FileGenUtil.addImport(fio, generateAbsIDName(table, dtoPackageName));
        }
    }

    /**
     * Adds the interface.
     *
     * @param fio the fio
     * @param table the table
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addInterface(FileOutputStream fio, Table table, String daoIntPackageName, String dtoPackageName) throws IOException {
        addInterfaceDeclaration(fio, table, daoIntPackageName, dtoPackageName);
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("}", fio);
    }

    /**
     * Adds the interface declaration.
     *
     * @param fio the fio
     * @param table the table
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addInterfaceDeclaration(FileOutputStream fio, Table table, String daoIntPackageName, String dtoPackageName) throws IOException {

        if (table.hasCompositePrimaryKey()) {
            addInterfaceDeclarationUsingCompositeType(fio, table, daoIntPackageName, dtoPackageName);
        } else {
            addInterfaceDeclarationUsingSimpleType(fio, table, daoIntPackageName, dtoPackageName);
        }
    }

    /**
     * Adds the interface declaration using id type.
     *
     * @param fio the fio
     * @param table the table
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addInterfaceDeclarationUsingCompositeType(FileOutputStream fio, Table table, String daoIntPackageName, String dtoPackageName) throws IOException {
        String interfaceDeclaration = String.format("public interface %s extends IGenericDAO<%s, %sID> {", table.getDAOInterfaceName(), table.getDTOName(), table.getFormattedName());
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine(interfaceDeclaration, fio);
    }

    /**
     * Adds the interface declaration using simple type.
     *
     * @param fio the fio
     * @param table the table
     * @param daoIntPackageName the dao int package name
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addInterfaceDeclarationUsingSimpleType(FileOutputStream fio, Table table, String daoIntPackageName, String dtoPackageName) throws IOException {
        for (Column column : table.getColumns()) {
            if (column.isPrimaryKey()) {
                String interfaceDeclaration = String.format("public interface %s extends IGenericDAO<%s, %s> {", table.getDAOInterfaceName(), table.getDTOName(), SqlTypeMapper.getJavaType(column.getType()));
                FileGenUtil.writeEmpytLines(1, fio);
                FileGenUtil.writeLine(interfaceDeclaration, fio);
                break;
            }
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
     * Generate abs id name.
     *
     * @param table the table
     * @param dtoPackageName the dto package name
     * @return the string
     */
    private static String generateAbsIDName(Table table, String dtoPackageName) {
        return String.format("%s.%sID", dtoPackageName, table.getFormattedName());
    }

}

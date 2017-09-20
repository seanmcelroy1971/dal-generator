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

/**
 * The Class DtoGenerator.
 */
public class DtoGenerator {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(DtoGenerator.class);

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    /**
     * Instantiates a new dto generator.
     */
    private DtoGenerator() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    /**
     * Generate dt os.
     *
     * @param outputDir the output dir
     * @param table the table
     * @param dtoPackageName the dto package name
     */
    public static void generateDTO(File outputDir, Table table, String dtoPackageName) {

        if(!FileGenUtil.isLinkTable(table)) {
            FileOutputStream fio = null;

            try {
                /**
                 * Create the absolute output dir, which is the output dir supplied
                 * at the command line, plus the directory structure that maps
                 * to the package name
                 */
                File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, dtoPackageName);
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
                FileGenUtil.addPackageDeclaration(fio, dtoPackageName);

                /**
                 * Add import statements
                 */
                addImports(fio);

                /**
                 * Add class declaration
                 */
                addClassDeclaration(fio, table, dtoPackageName);

                /**
                 * Add public members section
                 */
                addPublicMembersSection(fio, table, dtoPackageName);

                /**
                 * Add private static members section
                 */
                addPriateMembersSection(fio, table, dtoPackageName);

                /**
                 * Add static initialisers section
                 */
                addStaticInitialisersSection(fio, table, dtoPackageName);

                /**
                 * Add constructors section
                 */
                addConstructorsSection(fio, table, dtoPackageName);

                /**
                 * Add public methods section
                 */
                addPublicMethodsSection(fio, table, dtoPackageName);

                /**
                 * Add protected methods section
                 */
                addProtectedMethodsSection(fio, table, dtoPackageName);

                /**
                 * Add private static methods section
                 */
                addPrivateMethodsSection(fio, table, dtoPackageName);

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
    }

    public static File generateIDClass(File outputDir, Table table, String dtoPackageName) {
        FileOutputStream fio = null;
        File classFile = null;

        try {
            /**
             * Create the absolute output dir, which is the output dir supplied
             * at the command line, plus the directory structure that maps
             * to the package name
             */
            File absOutputDir = FileGenUtil.getAbsoluteOutputDir(outputDir, dtoPackageName);
            FileUtils.forceMkdir(absOutputDir);

            /**
             * Generate the class file
             */
            classFile = getIDClassFile(absOutputDir, table);

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
            FileGenUtil.addPackageDeclaration(fio, dtoPackageName);

            /**
             * Add import statements
             */
            addImports(fio);

            /**
             * Add class declaration
             */
            addIDClassDeclaration(fio, table, dtoPackageName);

            /**
             * Add public members section
             */
            addPublicMembersSection(fio, table, dtoPackageName);

            /**
             * Add private static members section, for ID Class
             */
            addIDPriateMembersSection(fio, table, dtoPackageName);

            /**
             * Add static initialisers section
             */
            addStaticInitialisersSection(fio, table, dtoPackageName);

            /**
             * Add constructors section
             */
            addIDConstructorsSection(fio, table, dtoPackageName);

            /**
             * Add public methods section, for ID classes
             */
            addIDPublicMethodsSection(fio, table, dtoPackageName);

            /**
             * Add protected methods section
             */
            addProtectedMethodsSection(fio, table, dtoPackageName);

            /**
             * Add private static methods section
             */
            addPrivateMethodsSection(fio, table, dtoPackageName);

            /**
             * Add closing brace
             */
            FileGenUtil.addClosingBrace(fio);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(fio);
        }

        return classFile;
    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // private static Methods
    // ===========================================

    /**
     * Adds the imports.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addImports(FileOutputStream fio) throws IOException {

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.addImport(fio, "java.io.Serializable");
    }


    /**
     * Adds the public members section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPublicMembersSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPublicMembersSectionHeader(fio);
    }

    /**
     * Adds the priate members section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPriateMembersSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPrivateMembersSectionHeader(fio);

        /**
         * Add ID class, if composite primary key
         */
        if(table.hasCompositePrimaryKey()) {
            addPrivateMembersAndID(fio, table, dtoPackageName);
        } else {
            addAllPrivateMembers(fio, table, dtoPackageName);
        }

    }

    /**
     * Adds the private members with key.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPrivateMembersAndID(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {

        /**
         * Add ID CLass
         */
        FileGenUtil.writeEmpytLines(1,fio);
        String line = String.format("    private %sID %sID;", table.getFormattedName(), table.getCamelCase());
        FileGenUtil.writeLine(line, fio);

        /**
         * Iterate through each column
         */
        for(Column column : table.getColumns()) {

            if(!table.getPrimaryKey().contains(column.getName())) {

                /**
                 * Write an empty linae
                 */
                FileGenUtil.writeEmpytLines(1,fio);

                /**
                 * Format the line
                 */
                line = String.format("    private %s %s;", SqlTypeMapper.getJavaType(column.getType()), column.getCamelCase());

                /**
                 * Write to file
                 */
                FileGenUtil.writeLine(line, fio);
            }
        }
    }

    /**
     * Adds the all private members.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addAllPrivateMembers(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {

        /**
         * Iterate through each column and add
         * as a private variable
         */
        for(Column column : table.getColumns()) {

            /**
             * Write an empty linae
             */
            FileGenUtil.writeEmpytLines(1,fio);

            /**
             * Format the line
             */
            String line = String.format("    private %s %s;", SqlTypeMapper.getJavaType(column.getType()), column.getCamelCase());

            /**
             * Write to file
             */
            FileGenUtil.writeLine(line, fio);
        }
    }

    /**
     * Adds the priate members section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addIDPriateMembersSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPrivateMembersSectionHeader(fio);

        for(Column column : table.getColumns()) {

            if(table.getPrimaryKey().contains(column.getName())) {

                /**
                 * Write an empty linae
                 */
                FileGenUtil.writeEmpytLines(1,fio);

                /**
                 * Format the line
                 */
                String line = String.format("    private %s %s;", SqlTypeMapper.getJavaType(column.getType()), column.getCamelCase());

                /**
                 * Write to file
                 */
                FileGenUtil.writeLine(line, fio);
            }
        }
    }


    /**
     * Adds the static initialisers section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addStaticInitialisersSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addStaticInitialisersSectionHeader(fio);
    }

    /**
     * Adds the constructors section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addConstructorsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addConstructorsSectionHeader(fio);

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    public " + table.getFormattedName() + "DTO() {", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the id constructors section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addIDConstructorsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addConstructorsSectionHeader(fio);

        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("    public " + table.getFormattedName() + "ID() {", fio);
        FileGenUtil.writeLine("    }", fio);
    }

    /**
     * Adds the public methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPublicMethodsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPublicMethodsSectionHeader(fio);

        FileGenUtil.writeEmpytLines(1,fio);

        if(table.hasCompositePrimaryKey()) {
            addPublicMethodsAndIDAccessors(fio, table, dtoPackageName);
        } else {
            addAllPublicMethods(fio, table, dtoPackageName);
        }
    }

    /**
     * Adds the public methods and id accessors.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPublicMethodsAndIDAccessors(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {

        /**
         * Add getter and setter for id
         */
        addGetIDMethod(fio, table);
        addSetIDMethod(fio, table);

        /**
         * Add getters and setters, for not primay key properties
         */
        for(Column column : table.getColumns()) {

            if(!table.getPrimaryKey().contains(column.getName())) {
                /**
                 * Add set method
                 */
                addSetMethod(fio, column);

                /**
                 * Add get method
                 */
                addGetMethod(fio, column);
            }
        }

    }

    /**
     * Adds the all public methods.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addAllPublicMethods(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        /**
         * Add getters and setters
         */
        for(Column column : table.getColumns()) {
            /**
             * Add set method
             */
            addSetMethod(fio, column);

            /**
             * Add get method
             */
            addGetMethod(fio, column);
        }
    }

    /**
     * Adds the pk public methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addIDPublicMethodsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPublicMethodsSectionHeader(fio);

        FileGenUtil.writeEmpytLines(1,fio);

        /**
         * Add getters and setters
         */
        for(Column column : table.getColumns()) {

            if(table.getPrimaryKey().contains(column.getName())) {
                /**
                 * Add set method
                 */
                addSetMethod(fio, column);

                /**
                 * Add get method
                 */
                addGetMethod(fio, column);
            }
        }
    }

    /**
     * Adds the set method.
     *
     * @param fio the fio
     * @param column the column
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSetMethod(FileOutputStream fio, Column column) throws IOException {
        /**
         * Get set method param type
         */
        String type = SqlTypeMapper.getJavaType(column.getType());

        /**
         * Generate the set method name
         */
        String setMethodName = FileGenUtil.generateSetterName(column);

        /**
         * Generate method signature
         */
        String methodSignature = String.format("    public void %s(%s %s) {", setMethodName, type, column.getCamelCase());

        /**
         * Generate method body
         */
        String methodBody = String.format("        this.%s = %s;", column.getCamelCase(), column.getCamelCase());

        /**
         * Output method to file
         */
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine(methodBody, fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the get method.
     *
     * @param fio the fio
     * @param column the column
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addGetMethod(FileOutputStream fio, Column column) throws IOException {
        /**
         * Get set method param type
         */
        String type = SqlTypeMapper.getJavaType(column.getType());

        /**
         * Generate the get method name
         */
        String getMethodName = FileGenUtil.generateGetterName(column);

        /**
         * Generate method signature
         */
        String methodSignature = String.format("    public %s %s() {", type, getMethodName);

        /**
         * Generate method body
         */
        String methodBody = String.format("        return this.%s;", column.getCamelCase());

        /**
         * Output method to file
         */
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine(methodBody, fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the set id method.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSetIDMethod(FileOutputStream fio, Table table) throws IOException {

        /**
         * Generate method signature
         */
        String methodSignature = String.format("    public void set%sID(%sID %sID) {", table.getFormattedName(), table.getFormattedName(), table.getCamelCase());

        /**
         * Generate method body
         */
        String methodBody = String.format("        this.%sID = %sID;", table.getCamelCase(), table.getCamelCase());

        /**
         * Output method to file
         */
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine(methodBody, fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the get id method.
     *
     * @param fio the fio
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addGetIDMethod(FileOutputStream fio, Table table) throws IOException {

        /**
         * Generate method signature
         */
        String methodSignature = String.format("    public %sID get%sID() {", table.getFormattedName(), table.getFormattedName());

        /**
         * Generate method body
         */
        String methodBody = String.format("        return this.%sID;", table.getCamelCase());

        /**
         * Output method to file
         */
        FileGenUtil.writeLine(methodSignature, fio);
        FileGenUtil.writeLine(methodBody, fio);
        FileGenUtil.writeLine("    }", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Adds the protected methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addProtectedMethodsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addProtectedMethodsSectionHeader(fio);
    }

    /**
     * Adds the private static methods section.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addPrivateMethodsSection(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.addPrivateMethodsSectionHeader(fio);
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
        String fileName = String.format("%s%s%sDTO.java",
                                         outputDir.getAbsolutePath(),
                                         File.separator,
                                         table.getFormattedName());

        /**
         * Return as file object
         */
        return new File(fileName);
    }

    /**
     * Gets the pK class file.
     *
     * @param outputDir the output dir
     * @param table the table
     * @return the pK class file
     */
    public static File getIDClassFile(File outputDir, Table table) {

        /**
         * Build the file name
         */
        String fileName = String.format("%s%s%sID.java",
                                         outputDir.getAbsolutePath(),
                                         File.separator,
                                         table.getFormattedName());

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
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addClassDeclaration(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.writeLine(String.format("public class %sDTO implements Serializable {", table.getFormattedName()), fio);
    }

    /**
     * Adds the id class declaration.
     *
     * @param fio the fio
     * @param table the table
     * @param dtoPackageName the dto package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addIDClassDeclaration(FileOutputStream fio, Table table, String dtoPackageName) throws IOException {
        FileGenUtil.writeEmpytLines(1,fio);
        FileGenUtil.writeLine(String.format("public class %sID implements Serializable {", table.getFormattedName()), fio);
    }
}

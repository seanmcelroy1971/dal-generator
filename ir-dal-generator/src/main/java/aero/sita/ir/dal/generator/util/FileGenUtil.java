package aero.sita.ir.dal.generator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;

import aero.sita.ir.dal.generator.dto.Column;
import aero.sita.ir.dal.generator.dto.Table;

/**
 * The Class FileGenUtil.
 */
public class FileGenUtil {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    /**
     * Instantiates a new file gen util.
     */
    public FileGenUtil() {
    }

    // ===========================================
    // Public Methods
    // ===========================================

    public static boolean isLinkTable(Table table) {
        return (table.getPrimaryKey().size() == table.getColumns().size());
    }

    /**
     * Adds the file header.
     *
     * @param fio the fio
     * @param table the table
     * @param packageName the package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addFileHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.writeLine("/*", fio);
        FileGenUtil.writeLine(" * This code contains copyright information which is the proprietary property", fio);
        FileGenUtil.writeLine(" * of SITA Advanced Travel Solutions. No part of this code may be reproduced,", fio);
        FileGenUtil.writeLine(" * stored or transmitted in any form without the prior written permission of", fio);
        FileGenUtil.writeLine(" * SITA Advanced Travel Solutions.", fio);
        FileGenUtil.writeLine(" *", fio);
        FileGenUtil.writeLine(" * Copyright SITA Advanced Travel Solutions 2001-2011", fio);
        FileGenUtil.writeLine(" * All rights reserved.", fio);
        FileGenUtil.writeLine(" */    ", fio);
    }


    /**
     * Adds the package declaration.
     *
     * @param fio the fio
     * @param packageName the package name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addPackageDeclaration(FileOutputStream fio, String packageName) throws IOException {
        FileGenUtil.writeLine("package " + packageName + ";", fio);
    }

    /**
     * Adds the import.
     *
     * @param fio the fio
     * @param importDescription the import description
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addImport(FileOutputStream fio, String importDescription) throws IOException {
        FileGenUtil.writeLine("import " + importDescription + ";", fio);
    }

    /**
     * Adds the closing brace.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addClosingBrace(FileOutputStream fio) throws IOException {
        FileGenUtil.writeEmpytLines(1, fio);
        FileGenUtil.writeLine("}", fio);
        FileGenUtil.writeEmpytLines(1, fio);
    }

    /**
     * Gets the absolute output dir.
     *
     * @param baseDir the base dir
     * @param packageName the package name
     * @return the absolute output dir
     */
    public static File getAbsoluteOutputDir(File baseDir, String packageName) {
        String dirPath = String.format("%s%s%s", baseDir.getAbsolutePath(), File.separator, convertToDirName(packageName));
        return new File(dirPath);
    }

    /**
     * Generate setter name.
     *
     * @param column the column
     * @return the string
     */
    public static String generateSetterName(Column column) {
        return "set" + FileGenUtil.upperCaseFirst(column.getCamelCase());
    }

    /**
     * Generate getter name.
     *
     * @param column the column
     * @return the string
     */
    public static String generateGetterName(Column column) {
        return "get" + FileGenUtil.upperCaseFirst(column.getCamelCase());
    }

    /**
     * Capitalise.
     *
     * @param str the str
     */
    public static String capitalise(String str) {
        String myStr = WordUtils.capitalize(str.toLowerCase(), new char[] {'_'});
        return myStr.replaceAll("[ _]", "");
    }

    /**
     * Camel case.
     *
     * @param str the str
     * @return the string
     */
    public static String camelCase(String str) {
        char[] myStrArray = capitalise(str).toCharArray();
        myStrArray[0] = Character.toLowerCase(myStrArray[0]);
        return new String(myStrArray);
    }

    /**
     * Upper case first.
     *
     * @param str the str
     * @return the string
     */
    public static String upperCaseFirst(String str) {
        char[] myStrArray = str.toCharArray();
        myStrArray[0] = Character.toUpperCase(myStrArray[0]);
        return new String(myStrArray);
    }

    /**
     * Lower case first.
     *
     * @param str the str
     * @return the string
     */
    public static String lowerCaseFirst(String str) {
        char[] myStrArray = str.toCharArray();
        myStrArray[0] = Character.toLowerCase(myStrArray[0]);
        return new String(myStrArray);
    }

    /**
     * Write line.
     *
     * @param line the line
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeLine(String line, FileOutputStream fio) throws IOException {
        IOUtils.write(line, fio);
        IOUtils.write(IOUtils.LINE_SEPARATOR, fio);
    }

    /**
     * Write line.
     *
     * @param line the line
     * @param paddingLeft the padding left
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeLinePadLeft(String line, int paddingLeft, FileOutputStream fio) throws IOException {
        String formatString = "% " + paddingLeft + "s";
        String str = String.format(formatString, line);
        IOUtils.write(str, fio);
        IOUtils.write(IOUtils.LINE_SEPARATOR, fio);
    }

    /**
     * Write empyt lines.
     *
     * @param numLines the num lines
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeEmpytLines(int numLines, FileOutputStream fio) throws IOException {
        for (int index = 0; index < numLines; index++) {
            IOUtils.write(IOUtils.LINE_SEPARATOR, fio);
        }
    }

    /**
     * Adds the public members section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addPublicMembersSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Public Members", fio);
    }

    /**
     * Adds the private members section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addPrivateMembersSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Private Members", fio);
    }

    /**
     * Adds the static initialisers section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addStaticInitialisersSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Static Initialisers", fio);
    }

    /**
     * Adds the constructors section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addConstructorsSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Constructors", fio);
    }

    /**
     * Adds the public methods section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addPublicMethodsSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Public Methods", fio);
    }

    /**
     * Adds the protected methods section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addProtectedMethodsSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Protected Methods", fio);
    }

    /**
     * Adds the private methods section header.
     *
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void addPrivateMethodsSectionHeader(FileOutputStream fio) throws IOException {
        FileGenUtil.addSectionHeader("Private Methods", fio);
    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    /**
     * Adds the section header.
     *
     * @param title the title
     * @param fio the fio
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addSectionHeader(String title, FileOutputStream fio) throws IOException {
        FileGenUtil.writeLine("    // ===========================================", fio);
        FileGenUtil.writeLine("    // " + title, fio);
        FileGenUtil.writeLine("    // ===========================================", fio);
    }

    /**
     * Convert to dir name.
     *
     * @param packageName the package name
     * @return the string
     */
    private static String convertToDirName(String packageName) {
        return packageName.replace('.', File.separatorChar);
    }
}

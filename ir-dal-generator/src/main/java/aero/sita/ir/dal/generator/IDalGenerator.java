package aero.sita.ir.dal.generator;

import java.io.File;

/**
 * The Interface IDalGenerator.
 */
public interface IDalGenerator {

	/**
	 * Execute.
	 *
	 * @param outputDir the output dir
	 * @param dalPackageName the dal package name
	 * @param dtoPackageName the dto package name
	 */
	void execute(File outputDir, String dalPackageName, String dtoPackageName);
}

/**
 * 
 */
package rcb.file.trasnfer.scp.refactored.config;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Roopesh Chandra Bose
 *
 */
class ShellScriptFilter implements FileFilter {
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */

	@Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        }

        String extension = Utils.getExtension(file);
        if (null != extension) {
            if (extension.equals(Utils.script)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * The description of this filter
     * @return
     */
    public String getDescription() {
        return "Just Unix Shell Script Files";
    }
}
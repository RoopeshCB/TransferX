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
class XMLFilter implements FileFilter {
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */

	@Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return false;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.xml)) {
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
        return "Just XML Files";
    }
}
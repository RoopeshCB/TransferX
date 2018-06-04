package rcb.file.trasnfer.scp.refactored.config;

import java.io.File;

import rcb.file.trasnfer.scp.refactored.readers.Reader;

final public class LocalConfig {
	private final String inputpath;
	private final String returnpath;
	//private final String inputfilename[];

	public LocalConfig(final Reader reader) {
		assert reader!= null;
		this.inputpath = reader.getValue("inputpath");
		this.returnpath = reader.getValue("returnpath");
		/*		File folderInputpath = new File(this.inputpath);
		File[] files = folderInputpath.listFiles(new ShellScriptFilter());
		this.inputfilename = new String[files.length];
		initInputFileNames(files);
		 */	}

	/*	private void initInputFileNames(File[] files) {
		for (int i = 0; i < files.length; i++) {
			this.inputfilename[i] = null;
			this.inputfilename[i] = files[i].getAbsolutePath();
		}
	}*/

	@Override
	public final String toString() {
		return "Input File [" + this.inputpath + /*this.inputfilename +*/ "]\n" +
		       "Return Path [" + this.returnpath + "]";
	}

	public final String getInputPath() {
		return this.inputpath;
	}

	public final String getInputFile() {
		return this.inputpath.substring(this.inputpath.lastIndexOf(File.separator) + 1);
	}
	
	public final String getReturnPath() {
		return this.returnpath;
	}

	/*
	public final String[] getInputFilePath() {
		return inputfilename;
	}*/
}
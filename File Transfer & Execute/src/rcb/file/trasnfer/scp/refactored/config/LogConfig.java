package rcb.file.trasnfer.scp.refactored.config;

import java.io.File;

import rcb.file.trasnfer.scp.refactored.readers.Reader;

final public class LogConfig {
	private final String logpath;
	private final String logname;

	public LogConfig(final Reader reader) {
		assert reader!= null;
		this.logpath = reader.getValue("logpath");
		this.logname = reader.getValue("logname");
	}

	public final String toString() {
		return "Log File   : [" + this.logpath + File.separator + this.logname + "]";
	}

	public final String getLogPath() {
		return this.logpath;
	}

	public final String getLogName() {
		return this.logname;
	}

	public final String getLogfileName() {
		return this.logpath + File.separator + this.logname;
	}
}
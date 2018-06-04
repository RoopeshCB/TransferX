package rcb.file.trasnfer.scp.refactored.log;

import java.io.FileWriter;
import java.io.IOException;

import rcb.file.trasnfer.scp.refactored.config.Config;

final public class Log {
	private FileWriter fileWriter;
	private static Log log;

	private Log(Config config) {
		this.open(config.logConfig().getLogfileName());
	}

	public synchronized static final Log getLog(Config config) {
		if (null == log) {
			log = new Log(config);
		}
		return log;
	}

	public synchronized static final Log getLog() {
		if (null == log) {
			throw new NullPointerException("Log File is not Initilized !!!");
		}
		return log;
	}

	private final boolean isOpen() {
		if (null == this.fileWriter) {
			return false;
		}
		return true;
	}

	private synchronized final void open(String fileName) {
		try {
			fileWriter = new FileWriter(fileName, false);
		} catch (IOException e) {
		}
	}

	public synchronized final void log(String message) {
		if (!isOpen()) {
			System.out.println("Log file is not open...");
			return;
		}

		try {
			fileWriter.write(message + "\r\n");
			fileWriter.flush();
		} catch (IOException e) {
		}
	}

	public synchronized final void finalize() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
		}
	}
}
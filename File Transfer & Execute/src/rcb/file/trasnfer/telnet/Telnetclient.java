package rcb.file.trasnfer.telnet;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Telnetclient {
	public static void main(String args[]) {
		String resultLog = "";

		try {
			Properties config;
			String host, user, password, prompt, path, cmd;
			config = new Properties();
			config.load(new FileInputStream("C:\\Users\\roopeshb\\Code\\Java\\File Transfer\\src\\rcb\\file\\trasnfer\\telnet\\Config.properties"));
			host = config.getProperty("host");
			user = config.getProperty("user");
			password = config.getProperty("password");
			prompt = config.getProperty("prompt");
			path = config.getProperty("path");
			cmd = config.getProperty("cmd");
			Telnetclient telnetclient = new Telnetclient();
			resultLog = telnetclient.ReturnLog(host, user, password, prompt, path, cmd);
			telnetclient.WritetoFile("C:\\Users\\roopeshb\\Code\\Java\\File Transfer\\logs\\date.txt", resultLog);
		} catch (IOException ex) {
			System.err.println("Exception:" + ex);
		}

		System.out.println(resultLog);
	}

	public String ReturnLog(String host, String user, String pwd, String prompt, String path, String cmd) {
		String result_log;
		TelnetLoginCommand telnetLoginCommand = new TelnetLoginCommand();
		try {
			telnetLoginCommand.connect(host, 23);
			System.out.println("before login");
			telnetLoginCommand.loginResult(user, pwd);
			System.out.println("after login:");
			telnetLoginCommand.executeCommand(path, prompt);
			System.out.println("Executed Command 1 ");
			result_log = telnetLoginCommand.executeCommand(cmd, prompt);
			telnetLoginCommand.disconnect();
			return result_log;
		} catch (Exception e) {
			telnetLoginCommand.disconnect();
			return e.getMessage();
		}
	}

	public void WritetoFile(String filename, String writestring) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write(writestring);
		writer.close();
	}
}
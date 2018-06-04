package rcb.file.trasnfer.telnet;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

public class TelnetLoginCommand extends TelnetProtocolHandler {

	/** debugging level */
	private final static int debug = 0;

	protected ScriptHandler scriptHandler = new ScriptHandler();

	protected InputStream in;
	protected OutputStream out;
	protected Socket socket;
	/* protected String host = HyperCodeClient.hostname; */
	protected String host = "mar1adv";
	protected int port = 23;
	//protected Vector<?> script = new Vector();
	private String prompt = null;

	/** Connect the socket and open the connection. */
	public void connect(String host, int port) throws IOException {
		if(debug>0) System.err.println("Telnet: connect("+host+","+port+")");
		try {
			socket = new java.net.Socket(host, port);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			reset();
		} catch(Exception e) {
			System.err.println("Telnet: "+e);
			disconnect();
			throw ((IOException)e);
		}
	}

	/** Disconnect the socket and close the connection. */
	public void disconnect() {
		try {
			if(debug>0) System.err.println("Telnet: disconnect()");
			if (socket != null) socket.close();
		} catch (Exception e) { }
		socket = null;
	}

	public boolean isDisconnected() {
		return socket==null;
	}

	/** sent on IAC EOR (prompt terminator for remote access systems). */
	public void notifyEndOfRecord() { }

	/**
	* Login into remote host. This is a convenience method and only
	* works if the prompts are "login:" and "Password:".
	* @param user the user name
	* @param pwd the password
	*/
	public void login(String user, String pwd) throws IOException {
		waitfor("login:");		// throw output away
		send(user);
		waitfor("bcmadmintest's Password:");	// throw output away
		send(pwd);
	}

	public String loginResult(String user, String pwd) {
		try {
			login(user, pwd);
			return waitfor(" ");
		} catch (Exception e) { return ""; }
	}

	/**
	* Set the prompt for the send() method.
	*/

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	* Send a command to the remote host. A newline is appended and if
	* a prompt is set it will return the resulting data until the prompt
	* is encountered.
	* @param cmd the command
	* @return output of the command or null if no prompt is set
	*/
	public String send(String cmd) throws IOException {
		write((cmd+"\n").getBytes());
		if(prompt != null) return waitfor(prompt);
		return null;
	}

	public String executeCommand(String cmd, String cmdWait) {
		try {
			write((cmd+"\n").getBytes());
			if (cmdWait != null) return waitfor(cmdWait); else return "";
		} catch (Exception e) { return ""; }
	}

	/**
	* Wait for a string to come from the remote host and return all
	* that characters that are received until that happens (including
	* the string being waited for).
	*
	* @param match the string to look for
	* @return skipped characters
	*/
	public String waitfor(String match) throws IOException {
		scriptHandler.setup(match);
		byte[] b = new byte[8192];
		int n = 0;
		String ret = "";
		while(n >= 0) {
			n = read(b);
			if(n > 0) {
				if(debug > 0) System.err.print(new String(b, 0, n));
				ret += new String(b, 0, n);
				if(scriptHandler.match(b, n))
				return ret;
			}
		}
		return null; // should never happen
	}

	/**
	* Read data from the socket and use telnet negotiation before returning
	* the data read.
	* @param b the input buffer to read in
	* @return the amount of bytes read
	*/
	public int read(byte[] b) throws IOException {
		int n = negotiate(b);
		if (n>0) return n;
		while (n<=0) {
			do {
				n = negotiate(b);
				if (n>0) return n;
			} while (n==0);
			n = in.read(b);
			if (n<0) return n;
			inputfeed(b,n);
			n = negotiate(b);
		}
		return n;
	}

	/**
	* Write data to the socket.
	* @param b the buffer to be written
	*/
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	public String getTerminalType() {
		return "dumb";
	}

	public Dimension getWindowSize() {
		return new Dimension(80,24);
	}

	public void setLocalEcho(boolean echo) {
		if(debug > 0) System.err.println("local echo "+(echo ? "on" : "off"));
	}
}


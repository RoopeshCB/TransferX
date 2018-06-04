package rcb.file.trasnfer.scp.refactored.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "system")
@XmlAccessorType (XmlAccessType.FIELD)
public class RemoteSystem {
	private String machine;
	private String user;
	private String password;
	private boolean encrypted;
	private String path;
	private boolean execute;
	private String returnfile;

	public RemoteSystem(String name, String user, String password, boolean encrypted, String path, boolean execute, String returnfile) {
		this.encrypted = false;
		this.execute = false;
		this.machine = name;
		this.user = user;
		this.password = password;
		this.encrypted =encrypted;
		this.path = path;
		this.execute = execute;
		this.returnfile = returnfile;
	}

	public String getReturnfile() {
		return returnfile;
	}

	public void setReturnfile(String returnfile) {
		this.returnfile = returnfile;
	}

	public RemoteSystem() {

	}
	
	public String toString() {
		return "Machine : [" + machine + "] User : [" + user + "] Path : [" + path + "] Encrypted : [" + encrypted + "] Execute : [" + execute + "] Return File : [" + returnfile + "]";
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isExecute() {
		return execute;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}
}
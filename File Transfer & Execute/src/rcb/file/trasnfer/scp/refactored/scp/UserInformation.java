package rcb.file.trasnfer.scp.refactored.scp;

import com.jcraft.jsch.UserInfo;

/**
 * @author roopesh-chandra bose
 */
class UserInformation implements UserInfo {
	String passwd;

	public UserInformation(String password) {
		this.passwd = password;
	}

	public String getPassphrase() {
		return null;
	}

	public String getPassword() {
		return this.passwd;
	}

	public boolean promptPassword(String paramString) {
		System.out.println("promptPassword::" + paramString);
		return true;
	}

	public boolean promptPassphrase(String paramString) {
		System.out.println("promptPassphrase::" + paramString);
		return true;
	}

	public boolean promptYesNo(String paramString) {
		System.out.println("promptYesNo::" + paramString);
		return true;
	}

	public void showMessage(String paramString) {
		System.out.println("showMessage::" + paramString);
	}
}
package rcb.file.trasnfer.scp.refactored.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptor {
	private static String DEFAULT_ENCODE_KEY = "DEADPOOL2";
	private static String encodeKey = DEFAULT_ENCODE_KEY;

	public static String encrypt(String strClearText,String strKey) throws Exception{
		String strData="";

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted=cipher.doFinal(strClearText.getBytes());
			strData=new String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return strData;
	}

	public static String decrypt(String strEncrypted,String strKey) throws Exception{
		String strData="";

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted=cipher.doFinal(strEncrypted.getBytes());
			strData=new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return strData;
	}

	public static void main(String[] args) throws Exception {
		if ((1 < args.length) && (null != args[1])) {
			if (16 < args[1].length()) {
				throw new Exception("Cipher Key is Longer than 16 Chars");
			}
		}

		String plainText = args[0];
		encodeKey = ((1 < args.length) && (null != args[1])) ? args[1] : DEFAULT_ENCODE_KEY;

		String encryptedText = Cryptor.encrypt(plainText, encodeKey);
		String decryptedText = Cryptor.decrypt(encryptedText, encodeKey);

		System.out.println("Plain Text     : [" + plainText + "]");
		System.out.println("Encrypted Text : [" + encryptedText + "]");
		System.out.println("Decrypted Text : [" + decryptedText + "]");
	}
}
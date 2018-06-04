package rcb.file.trasnfer.scp.refactored.exceptions;

public class CryptoException extends Exception {
	private static final long serialVersionUID = -5188791002174270146L;

	public CryptoException(String string, Exception ex) {
		super(string, ex);
	}
}
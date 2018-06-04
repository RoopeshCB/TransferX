package rcb.file.trasnfer.scp.refactored.scp;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author roopesh-chandra bose
 */
public enum Achnowledgement {
	SUCCESS(0),
	ERROR(1),
	FATAL(2),
	C('C'),
	EOF(-1);

	private int ack;

	Achnowledgement(int value) {
		this.ack = value;
	}

	public static Achnowledgement check(InputStream in) {
		try {
			int readStatus = in.read();
			if (0 == readStatus) { return SUCCESS; }
			if (-1 == readStatus) { return EOF; }
			if (1 == readStatus || 2 == readStatus) {
				StringBuffer buffer = new StringBuffer();
				int readValue;
				do {
					readValue = in.read();
					buffer.append((char) readValue);
				} while('\n' != readValue);
				
				System.out.print(buffer.toString());
				if (readStatus == 1) { // error
					return ERROR;
				}
				if (readStatus == 2) { // fatal error
					return FATAL;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR;
		}
		return C;
	}

	public String toString() {
		return "Achnowledgement : " + this.ack;
	}
}
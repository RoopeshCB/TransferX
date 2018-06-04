package rcb.file.trasnfer.telnet;

import java.awt.Dimension;
import java.io.IOException;

@SuppressWarnings("unused")
public abstract class TelnetProtocolHandler {
	public final static String ID = "$Id: TelnetProtocolHandler.java,v 2.11 2001/01/01 12:30:16 leo Exp $";
	private final static int debug = 0;
	private byte[] tempbuf = new byte[0];
	private byte[] crlf = new byte[2];
	private byte[] cr = new byte[2];

	public TelnetProtocolHandler() {
		reset();
		crlf[0] = 13;
		crlf[1] = 10;
		cr[0] = 13;
		cr[1] = 0;
	}

	protected abstract String getTerminalType();

	protected abstract Dimension getWindowSize();

	protected abstract void setLocalEcho(boolean echo);

	protected abstract void notifyEndOfRecord();

	protected abstract void write(byte[] b) throws IOException;

	private static byte[] one = new byte[1];

	private void write(byte b) throws IOException {
		one[0] = b;
		write(one);
	}

	public void reset() {
		neg_state = 0;
		receivedDX = new byte[256];
		sentDX = new byte[256];
		receivedWX = new byte[256];
		sentWX = new byte[256];
	}

	private byte neg_state = 0;

	private final static byte STATE_DATA = 0;
	private final static byte STATE_IAC = 1;
	private final static byte STATE_IACSB = 2;
	private final static byte STATE_IACWILL = 3;
	private final static byte STATE_IACDO = 4;
	private final static byte STATE_IACWONT = 5;
	private final static byte STATE_IACDONT = 6;
	private final static byte STATE_IACSBIAC = 7;
	private final static byte STATE_IACSBDATA = 8;
	private final static byte STATE_IACSBDATAIAC = 9;

	private byte current_sb;

	private final static byte IAC = (byte) 255;
	private final static byte EOR = (byte) 239;
	private final static byte WILL = (byte) 251;
	private final static byte WONT = (byte) 252;
	private final static byte DO = (byte) 253;
	private final static byte DONT = (byte) 254;
	private final static byte SB = (byte) 250;
	private final static byte SE = (byte) 240;
	private final static byte TELOPT_ECHO = (byte) 1; /* echo on/off */
	private final static byte TELOPT_SGA = (byte) 3; /* supress go ahead */
	private final static byte TELOPT_EOR = (byte) 25; /* end of record */
	private final static byte TELOPT_NAWS = (byte) 31; /* NA-WindowSize */
	private final static byte TELOPT_TTYPE = (byte) 24; /* terminal type */

	private final static byte[] IACWILL = { IAC, WILL };
	private final static byte[] IACWONT = { IAC, WONT };
	private final static byte[] IACDO = { IAC, DO };
	private final static byte[] IACDONT = { IAC, DONT };
	private final static byte[] IACSB = { IAC, SB };
	private final static byte[] IACSE = { IAC, SE };

	private final static byte TELQUAL_IS = (byte) 0;
	private final static byte TELQUAL_SEND = (byte) 1;

	private byte[] receivedDX;
	private byte[] receivedWX;
	private byte[] sentDX;
	private byte[] sentWX;

	public void sendTelnetControl(byte code) throws IOException {
		byte[] b = new byte[2];
		b[0] = IAC;
		b[1] = code;
		write(b);
	}

	private void handle_sb(byte type, byte[] sbdata, int sbcount) throws IOException {
		if (debug > 1) {
			System.err.println("TelnetIO.handle_sb(" + type + ")");
		}
		switch (type) {
		case TELOPT_TTYPE:
			if (sbcount > 0 && sbdata[0] == TELQUAL_SEND) {
				write(IACSB);
				write(TELOPT_TTYPE);
				write(TELQUAL_IS);
				String ttype = getTerminalType();
				if (ttype == null) {
					ttype = "dumb";
				}
				write(ttype.getBytes());
				write(IACSE);
			}

		}
	}

	public void startup() throws IOException {
		byte sendbuf[] = new byte[3];
		sendbuf[0] = IAC;
		sendbuf[1] = DO;
		sendbuf[2] = TELOPT_SGA;
		write(sendbuf);
		sentDX[TELOPT_SGA] = DO;
	}

	public void transpose(byte[] buf) throws IOException {
		int i;

		byte[] nbuf, xbuf;
		int nbufptr = 0;
		nbuf = new byte[buf.length * 2];

		for (i = 0; i < buf.length; i++) {
			switch (buf[i]) {
			case IAC:
				nbuf[nbufptr++] = IAC;
				nbuf[nbufptr++] = IAC;
				break;
			case 10: // \n
				while (nbuf.length - nbufptr < crlf.length) {
					xbuf = new byte[nbuf.length * 2];
					System.arraycopy(nbuf, 0, xbuf, 0, nbufptr);
					nbuf = xbuf;
				}
				for (int j = 0; j < crlf.length; j++)
					nbuf[nbufptr++] = crlf[j];
				break;
			case 13: // \r
				while (nbuf.length - nbufptr < cr.length) {
					xbuf = new byte[nbuf.length * 2];
					System.arraycopy(nbuf, 0, xbuf, 0, nbufptr);
					nbuf = xbuf;
				}
				for (int j = 0; j < cr.length; j++)
					nbuf[nbufptr++] = cr[j];
				break;
			default:
				nbuf[nbufptr++] = buf[i];
				break;
			}
		}
		xbuf = new byte[nbufptr];
		System.arraycopy(nbuf, 0, xbuf, 0, nbufptr);
		write(xbuf);
	}

	public void setCRLF(String xcrlf) {
		crlf = xcrlf.getBytes();
	}

	public void setCR(String xcr) {
		cr = xcr.getBytes();
	}

	public int negotiate(byte nbuf[]) throws IOException {
		byte sbbuf[] = new byte[tempbuf.length];
		int count = tempbuf.length;
		byte[] buf = tempbuf;
		byte sendbuf[] = new byte[3];
		byte b, reply;
		int sbcount = 0;
		int boffset = 0, noffset = 0;
		boolean dobreak = false;

		if (count == 0) // buffer is empty.
			return -1;

		while (!dobreak && (boffset < count)) {
			b = buf[boffset++];
			if (b >= 128)
				b = (byte) ((int) b - 256);
			if (debug > 2) {
				Byte B = new Byte(b);
				System.err.print("byte: " + B.intValue() + " ");
			}
			switch (neg_state) {
			case STATE_DATA:
				if (b == IAC) {
					neg_state = STATE_IAC;
					dobreak = true; // leave the loop so we can sync.
				} else
					nbuf[noffset++] = b;
				break;
			case STATE_IAC:
				switch (b) {
				case IAC:
					if (debug > 2)
						System.err.print("IAC ");
					neg_state = STATE_DATA;
					nbuf[noffset++] = IAC;
					break;
				case WILL:
					if (debug > 2)
						System.err.print("WILL ");
					neg_state = STATE_IACWILL;
					break;
				case WONT:
					if (debug > 2)
						System.err.print("WONT ");
					neg_state = STATE_IACWONT;
					break;
				case DONT:
					if (debug > 2)
						System.err.print("DONT ");
					neg_state = STATE_IACDONT;
					break;
				case DO:
					if (debug > 2)
						System.err.print("DO ");
					neg_state = STATE_IACDO;
					break;
				case EOR:
					if (debug > 1)
						System.err.print("EOR ");
					notifyEndOfRecord();
					neg_state = STATE_DATA;
					break;
				case SB:
					if (debug > 2)
						System.err.print("SB ");
					neg_state = STATE_IACSB;
					sbcount = 0;
					break;
				default:
					if (debug > 2)
						System.err.print("<UNKNOWN " + b + " > ");
					neg_state = STATE_DATA;
					break;
				}
				break;
			case STATE_IACWILL:
				switch (b) {
				case TELOPT_ECHO:
					if (debug > 2)
						System.err.println("ECHO");
					reply = DO;
					setLocalEcho(false);
					break;
				case TELOPT_SGA:
					if (debug > 2)
						System.err.println("SGA");
					reply = DO;
					break;
				case TELOPT_EOR:
					if (debug > 2)
						System.err.println("EOR");
					reply = DO;
					break;
				default:
					if (debug > 2)
						System.err.println("<UNKNOWN," + b + ">");
					reply = DONT;
					break;
				}
				if (debug > 1)
					System.err.println("<" + b + ", WILL =" + WILL + ">");
				if (reply != sentDX[b + 128] || WILL != receivedWX[b + 128]) {
					sendbuf[0] = IAC;
					sendbuf[1] = reply;
					sendbuf[2] = b;
					write(sendbuf);
					sentDX[b + 128] = reply;
					receivedWX[b + 128] = WILL;
				}
				neg_state = STATE_DATA;
				break;
			case STATE_IACWONT:
				switch (b) {
				case TELOPT_ECHO:
					if (debug > 2)
						System.err.println("ECHO");
					setLocalEcho(true);
					reply = DONT;
					break;
				case TELOPT_SGA:
					if (debug > 2)
						System.err.println("SGA");
					reply = DONT;
					break;
				case TELOPT_EOR:
					if (debug > 2)
						System.err.println("EOR");
					reply = DONT;
					break;
				default:
					if (debug > 2)
						System.err.println("<UNKNOWN," + b + ">");
					reply = DONT;
					break;
				}
				if (reply != sentDX[b + 128] || WONT != receivedWX[b + 128]) {
					sendbuf[0] = IAC;
					sendbuf[1] = reply;
					sendbuf[2] = b;
					write(sendbuf);
					sentDX[b + 128] = reply;
					receivedWX[b + 128] = WILL;
				}
				neg_state = STATE_DATA;
				break;
			case STATE_IACDO:
				switch (b) {
				case TELOPT_ECHO:
					if (debug > 2)
						System.err.println("ECHO");
					reply = WILL;
					setLocalEcho(true);
					break;
				case TELOPT_SGA:
					if (debug > 2)
						System.err.println("SGA");
					reply = WILL;
					break;
				case TELOPT_TTYPE:
					if (debug > 2)
						System.err.println("TTYPE");
					reply = WILL;
					break;
				case TELOPT_NAWS:
					if (debug > 2)
						System.err.println("NAWS");
					Dimension size = getWindowSize();
					receivedDX[b] = DO;
					if (size == null) {
						// this shouldn't happen
						write(IAC);
						write(WONT);
						write(TELOPT_NAWS);
						reply = WONT;
						sentWX[b] = WONT;
						break;
					}
					reply = WILL;
					sentWX[b] = WILL;
					sendbuf[0] = IAC;
					sendbuf[1] = WILL;
					sendbuf[2] = TELOPT_NAWS;
					write(sendbuf);
					write(IAC);
					write(SB);
					write(TELOPT_NAWS);
					write((byte) (size.width >> 8));
					write((byte) (size.width & 0xff));
					write((byte) (size.height >> 8));
					write((byte) (size.height & 0xff));
					write(IAC);
					write(SE);
					break;
				default:
					if (debug > 2)
						System.err.println("<UNKNOWN," + b + ">");
					reply = WONT;
					break;
				}
				if (reply != sentWX[128 + b] || DO != receivedDX[128 + b]) {
					sendbuf[0] = IAC;
					sendbuf[1] = reply;
					sendbuf[2] = b;
					write(sendbuf);
					sentWX[b + 128] = reply;
					receivedDX[b + 128] = DO;
				}
				neg_state = STATE_DATA;
				break;
			case STATE_IACDONT:
				switch (b) {
				case TELOPT_ECHO:
					if (debug > 2)
						System.err.println("ECHO");
					reply = WONT;
					setLocalEcho(false);
					break;
				case TELOPT_SGA:
					if (debug > 2)
						System.err.println("SGA");
					reply = WONT;
					break;
				case TELOPT_NAWS:
					if (debug > 2)
						System.err.println("NAWS");
					reply = WONT;
					break;
				default:
					if (debug > 2)
						System.err.println("<UNKNOWN," + b + ">");
					reply = WONT;
					break;
				}
				if (reply != sentWX[b + 128] || DONT != receivedDX[b + 128]) {
					write(IAC);
					write(reply);
					write(b);
					sentWX[b + 128] = reply;
					receivedDX[b + 128] = DONT;
				}
				neg_state = STATE_DATA;
				break;
			case STATE_IACSBIAC:
				if (debug > 2)
					System.err.println("" + b + " ");
				if (b == IAC) {
					sbcount = 0;
					current_sb = b;
					neg_state = STATE_IACSBDATA;
				} else {
					System.err.println("(bad) " + b + " ");
					neg_state = STATE_DATA;
				}
				break;
			case STATE_IACSB:
				if (debug > 2)
					System.err.println("" + b + " ");
				switch (b) {
				case IAC:
					neg_state = STATE_IACSBIAC;
					break;
				default:
					current_sb = b;
					sbcount = 0;
					neg_state = STATE_IACSBDATA;
					break;
				}
				break;
			case STATE_IACSBDATA:
				if (debug > 2)
					System.err.println("" + b + " ");
				switch (b) {
				case IAC:
					neg_state = STATE_IACSBDATAIAC;
					break;
				default:
					sbbuf[sbcount++] = b;
					break;
				}
				break;
			case STATE_IACSBDATAIAC:
				if (debug > 2)
					System.err.println("" + b + " ");
				switch (b) {
				case IAC:
					neg_state = STATE_IACSBDATA;
					sbbuf[sbcount++] = IAC;
					break;
				case SE:
					handle_sb(current_sb, sbbuf, sbcount);
					current_sb = 0;
					neg_state = STATE_DATA;
					break;
				case SB:
					handle_sb(current_sb, sbbuf, sbcount);
					neg_state = STATE_IACSB;
					break;
				default:
					neg_state = STATE_DATA;
					break;
				}
				break;
			default:
				if (debug > 1)
					System.err.println("This should not happen: " + neg_state + " ");
				neg_state = STATE_DATA;
				break;
			}
		}
		// shrink tempbuf to new processed size.
		byte[] xb = new byte[count - boffset];
		System.arraycopy(tempbuf, boffset, xb, 0, count - boffset);
		tempbuf = xb;
		return noffset;
	}

	public void inputfeed(byte[] b, int len) {
		byte[] xb = new byte[tempbuf.length + len];

		System.arraycopy(tempbuf, 0, xb, 0, tempbuf.length);
		System.arraycopy(b, 0, xb, tempbuf.length, len);
		tempbuf = xb;
	}
}
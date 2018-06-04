package rcb.file.trasnfer.scp.refactored.scp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import rcb.file.trasnfer.scp.refactored.log.Log;

/**
 * @author roopesh-chandra bose
 */
public class ScpTo implements AutoCloseable {
	private String userName;
	private String password;
	private String hostname;

	private Session session;
	
	private Log log;

	public ScpTo(String userName, String password, String hostname) {
		this.userName = userName;
		this.password = password;
		this.hostname = hostname;
		
		this.log = Log.getLog();
	}

	public void connect() throws JSchException {
		JSch jsch = new JSch();
		session = jsch.getSession(this.userName, this.hostname, 22);
		session.setPassword(this.password);
		//session.setPassword(this.password);
		UserInfo userInfo = new UserInformation(this.password);
		session.setUserInfo(userInfo);
		session.connect();
	}

	public boolean put(String localFile, String remoteDirectory) {
		return this.put(localFile, remoteDirectory, localFile);
	}

	public boolean get(String localDirectory, String localFile, String remoteFile) {
		String command = "scp -f "+ remoteFile;
		//System.out.println(command);
		FileOutputStream fos=null;

		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			OutputStream outputStream = channel.getOutputStream();
			InputStream inputStream = channel.getInputStream();
			channel.connect();

			byte[] outStreamBuffer = new byte[1024];
			outStreamBuffer[0]=0;
			outputStream.write(outStreamBuffer, 0, 1);
			outputStream.flush();

			log.log("Getting Remote File : [" + remoteFile + "] Local File : [" + localFile + "] into Local Directory : [" + localDirectory + "]");

			while(true) {
				Achnowledgement achnowledgement = Achnowledgement.check(inputStream);
				//System.out.println("InputStream Opening : " + achnowledgement);
				if (Achnowledgement.C != achnowledgement) {
					break;
				}

				inputStream.read(outStreamBuffer, 0, 5);
				long filesize=0L;
				while(true) {
					if(inputStream.read(outStreamBuffer, 0, 1)<0) {
						break;
					}
					if(outStreamBuffer[0]==' ') break;
					filesize = filesize*10L + (long)(outStreamBuffer[0]-'0');
				}

/*				String file = null;

				for(int i=0;;i++) {
					inputStream.read(outStreamBuffer, i, 1);
					if(outStreamBuffer[i]==(byte)0x0a) {
						file=new String(outStreamBuffer, 0, i);
						break;
					}
				}*/

				//System.out.println("File : " + file);

				outStreamBuffer[0]=0; outputStream.write(outStreamBuffer, 0, 1); outputStream.flush();
				fos = new FileOutputStream(localDirectory + localFile);

				int foo;
				while(true) {
					if(outStreamBuffer.length<filesize) { 
						foo=outStreamBuffer.length;
					}
					else {
						foo=(int)filesize;
					}
					foo=inputStream.read(outStreamBuffer, 0, foo);
					if(foo<0) {
						break;
					}
					fos.write(outStreamBuffer, 0, foo);
					filesize-=foo;
					if(filesize==0L) break;
				}
				fos.close();
				fos=null;
				
				if(Achnowledgement.SUCCESS != Achnowledgement.check(inputStream)) {
					return false;
				}
				outStreamBuffer[0]=0;
				outputStream.write(outStreamBuffer, 0, 1);
				outputStream.flush();
			}
			inputStream.close();
			outputStream.close();
			channel.disconnect();
		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	public boolean exec(String remoteDirectory, String remoteFileName) {
		String command = 	"cd " + remoteDirectory + "; chmod +x " + 
							remoteDirectory + File.separator + remoteFileName + "; " + 
							remoteDirectory + File.separator + remoteFileName;
		log.log("Remote Execution At : [" + remoteDirectory + "] for Script : [" + remoteFileName + "]");
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			InputStream inputStream = channel.getInputStream();
			channel.connect();
			byte[] tmp=new byte[1024];

			while(true) {
				while(inputStream.available()>0) {
					int i=inputStream.read(tmp, 0, 1024);
					if(i<0)break;
					System.out.print(new String(tmp, 0, i));
				}
				if(channel.isClosed()){
					if(inputStream.available()>0) continue;
					log.log("Exit-Status from Execution : [" + channel.getExitStatus() + "]");
					break;
				}
				try{Thread.sleep(1000);}catch(Exception ee){}
			}
			inputStream.close();
			inputStream = null;
			channel.disconnect();
		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean put(String localFilePath, String remoteDirectory, String remoteFileName) {
		boolean ptimestamp = true;
		String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteDirectory + "/" + remoteFileName;

		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			OutputStream outputStream = channel.getOutputStream();
			//InputStream inputStream = channel.getInputStream();
			session.setConfig("StrictHostKeyChecking", "no");
			channel.connect();
			File _localFile = new File(localFilePath);

			if (ptimestamp) {
				command = "T " + (_localFile.lastModified() / 1000) + " 0";
				command += (" " + (_localFile.lastModified() / 1000) + " 0\n");
				outputStream.write(command.getBytes());
				outputStream.flush();
				//System.out.println("InputStream Opening : " + Achnowledgement.check(inputStream));
			}

			long filesize = _localFile.length();
			command = "C0644 " + filesize + " ";

			//System.out.println("0:" + localFilePath.lastIndexOf(File.separatorChar));
			if (localFilePath.lastIndexOf(File.separatorChar) > 0) {
				command += localFilePath.substring(localFilePath.lastIndexOf(File.separatorChar) + 1);
				//System.out.println("1:" + command);
			} else {
				command += localFilePath;
				//System.out.println("2:" + command);
			}
			command += "\n";
			outputStream.write(command.getBytes());
			outputStream.flush();
			//System.out.println("InputStream Opening : " + Achnowledgement.check(inputStream));

			FileInputStream fileInputStream = new FileInputStream(localFilePath);
			byte[] fileReadBuffer = new byte[1024];

			while (true) {
				int streamLength = fileInputStream.read(fileReadBuffer, 0, fileReadBuffer.length);
				if (streamLength <= 0) {
					break;
				}
				outputStream.write(fileReadBuffer, 0, streamLength);
				outputStream.flush();
			}
			fileInputStream.close();
			fileInputStream = null;
			fileReadBuffer[0] = 0;
			outputStream.write(fileReadBuffer, 0, 1);
			outputStream.flush();
			//System.out.println("InputStream Opening : " + Achnowledgement.check(inputStream));
			outputStream.close();
			channel.disconnect();
			return true;
		} catch (JSchException | IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void close() throws Exception {
		session.disconnect();
	}
}
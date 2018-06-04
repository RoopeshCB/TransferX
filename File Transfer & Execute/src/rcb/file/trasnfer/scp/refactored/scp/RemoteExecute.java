package rcb.file.trasnfer.scp.refactored.scp;

import java.io.File;

import rcb.file.trasnfer.scp.refactored.config.Config;
import rcb.file.trasnfer.scp.refactored.log.Log;
import rcb.file.trasnfer.scp.refactored.model.RemoteSystem;
import rcb.file.trasnfer.scp.refactored.scp.ScpTo;

/**
 * @author roopesh-chandra bose
 */
public class RemoteExecute {
	private RemoteSystem remoteSystem;
	private Config config;
	private Log log;

	public RemoteExecute(RemoteSystem remoteSystem, Config config) {
		this.remoteSystem = remoteSystem;
		this.config = config;
		this.log = Log.getLog();
	}

	public boolean execute() {
		ScpTo scpTo = new ScpTo(remoteSystem.getUser(), remoteSystem.getPassword(), remoteSystem.getMachine());
		try {
			scpTo.connect();
			scpTo.put(config.localConfig().getInputPath(), remoteSystem.getPath(), config.localConfig().getInputFile());
			log.log("Local File [" + config.localConfig().getInputFile() + "] Copied to Remote Folder : [" + remoteSystem.getPath() + "]");

			if (remoteSystem.isExecute()) {
				scpTo.exec(remoteSystem.getPath(), config.localConfig().getInputFile());
				log.log("Remote File : [" + config.localConfig().getInputFile() + "] Executed in Remote Directory : [" + remoteSystem.getPath() + "]");
				scpTo.get(config.localConfig().getReturnPath(), remoteSystem.getReturnfile(), remoteSystem.getPath() + File.separator + remoteSystem.getReturnfile());
				log.log("Remote File : [" + remoteSystem.getReturnfile() + "] Copied to Local Directory : [" + config.localConfig().getReturnPath() + "]");
			}
			scpTo.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
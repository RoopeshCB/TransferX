package rcb.file.trasnfer.scp.refactored.model;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import rcb.file.trasnfer.scp.refactored.config.Config;
import rcb.file.trasnfer.scp.refactored.log.Log;
import rcb.file.trasnfer.scp.refactored.scp.RemoteExecute;

@XmlRootElement (name = "remote")
@XmlAccessorType (XmlAccessType.FIELD)
public class RemoteSystems {
	@XmlElement (name = "system")
	private List<RemoteSystem> remoteSystems;
	private Log log;
	private Config config = null;

	public RemoteSystems() { 
		this.log = Log.getLog();
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<RemoteSystem> geRemotetSystems() {
		return remoteSystems;
	}

	public void setRemotetSystems(List<RemoteSystem> remoteSystems) {
		this.remoteSystems = remoteSystems;
		Log.getLog().log("Remote Systems Assigned...");
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Iterator <RemoteSystem>iterator = remoteSystems.iterator(); iterator.hasNext();) {
			builder.append((iterator.next()).toString() + "\n");
		}

		return builder.append("").toString();
	}

	public void execute() {
		assert (null != config);
		for (Iterator<RemoteSystem> iterator = remoteSystems.iterator(); iterator.hasNext();) {
			RemoteSystem remoteSystem = iterator.next();
			log.log("Processing Remote System : [" + remoteSystem + "]");
			RemoteExecute remoteExecute = new RemoteExecute(remoteSystem, config);
			remoteExecute.execute();
		}
	}
}
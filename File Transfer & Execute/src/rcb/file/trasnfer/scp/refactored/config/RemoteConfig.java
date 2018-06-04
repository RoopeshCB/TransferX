package rcb.file.trasnfer.scp.refactored.config;

import rcb.file.trasnfer.scp.refactored.readers.Reader;

public final class RemoteConfig {
	private String remoteSystemsConfigPath;
	
	public RemoteConfig(final Reader reader) {
		assert reader!= null;
		this.remoteSystemsConfigPath = reader.getValue("remote.list.path");
	}
	
	public String getRemoteSystemsConfigPath() {
		return this.remoteSystemsConfigPath;
	}
}
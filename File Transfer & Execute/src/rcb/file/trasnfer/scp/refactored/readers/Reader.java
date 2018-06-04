package rcb.file.trasnfer.scp.refactored.readers;

public interface Reader {
	public abstract String getValue(String nodeName);
	//public List<RemoteSystem> getRemoteSystems(String nodeName);
}
package rcb.file.trasnfer.scp.refactored.config;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import rcb.file.trasnfer.scp.refactored.readers.Reader;
import rcb.file.trasnfer.scp.refactored.readers.XMLReader;

public final class Config {
	private static final String DEFAULT_CONFIG_XML = "config.xml".intern();

	private static LogConfig        logConfig;
	private static LocalConfig   	localConfig;
	private static Config       	config;
	private static RemoteConfig     remoteConfig;

	private Config(final String configName) throws SAXException, IOException, ParserConfigurationException {
		Reader reader 	= new XMLReader((null==configName)?DEFAULT_CONFIG_XML:configName);
		logConfig       = new LogConfig(reader);
		localConfig 	= new LocalConfig(reader);
		remoteConfig    = new RemoteConfig(reader);
	}

	public final synchronized static Config getConfig(final String configName) throws SAXException, IOException, ParserConfigurationException {
		if ( null != config ) {
			return config;
		}

		config = new Config(configName);
		return config;
	}

	public final synchronized static Config getConfig() throws NullPointerException {
		if ( null != config ) {
			return config;
		}
		throw new NullPointerException("Configuration is NULL !!!");
	}

	public synchronized final LogConfig logConfig() {
		return logConfig;
	}

	public synchronized final LocalConfig localConfig() {
		return localConfig;
	}

	public synchronized final RemoteConfig remoteConfig() {
		return remoteConfig;
	}
}
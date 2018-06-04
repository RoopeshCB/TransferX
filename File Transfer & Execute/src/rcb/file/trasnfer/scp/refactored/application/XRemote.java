package rcb.file.trasnfer.scp.refactored.application;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import rcb.file.trasnfer.scp.refactored.commandline.CommandLine.Command;
import rcb.file.trasnfer.scp.refactored.commandline.CommandLine.Option;
import rcb.file.trasnfer.scp.refactored.config.Config;
import rcb.file.trasnfer.scp.refactored.log.Log;
import rcb.file.trasnfer.scp.refactored.model.RemoteSystems;

/**
 * @author Roopesh Chandra Bose
 */

@Command(name = "remote.transfer.application", sortOptions = false,
header = {
        "@|green xxxxx xxxxx  xxxxxxxx                                              xx                     |@",
        "@|green xxxxx xxxxx  xxxxxxxxx                                             xx                     |@",
        "@|green   xx   xx     xx    xxx                                            xx                     |@",
        "@|green    xx xx      xx     xx      xxxxx    xxx xxx xxxx     xxxxx     xxxxxxxxxx      xxxxx    |@",
        "@|green    xx xx      xx    xxx    xxxxxxxxx  xxxxxxxxxxxxx  xxxxxxxxx   xxxxxxxxxx    xxxxxxxxx  |@",
        "@|green     xxx       xxxxxxxx     xx     xx   xxx  xxx  xx  xx     xx     xx          xx     xx  |@",
        "@|green     xxx       xxxxxxx     xxxxxxxxxxx  xx   xx   xx xx       xx    xx         xxxxxxxxxxx |@",
        "@|green    xx xx      xx   xxx    xxxxxxxxxxx  xx   xx   xx xx       xx    xx         xxxxxxxxxxx |@",
        "@|green    xx xx      xx    xx    xx           xx   xx   xx xx       xx    xx         xx          |@",
        "@|green   xx   xx     xx    xxx    xx      xx  xx   xx   xx  xx     xx     xx     xx   xx      xx |@",
        "@|green xxxxx xxxxx  xxxxx   xxxx  xxxxxxxxxx xxxx  xxx  xx  xxxxxxxxx     xxxxxxxxx   xxxxxxxxxx |@",
        "@|green xxxxx xxxxx  xxxxx    xxx    xxxxxx   xxxx  xxx  xx    xxxxx        xxxxxx       xxxxxx   |@",
        ""},
//descriptionHeading = "@|bold %nDescription|@:%n",

description = {
        "",
        "XRemote usage help.", },
optionListHeading = "@|bold %nOptions|@:%n",
footer = {
        "",
        "Run without any options to see this help",
        ""})

public class XRemote {
	@Option(names = { "-v", "--verbose" }, description = "Be Verbose.")
	private boolean verbose = false;

	private static Config config;
	private static Log log;
	private static RemoteSystems remoteSystems;

	public static void main(String[] args) {
		if (args.length < 1) {
			showHelp(args);
			System.exit(50);
		}

		try {
			config = Config.getConfig(args[0]);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(100);
		}

		log = Log.getLog(config);
		log.log("Remote Transfer Application Started : [" + new Date() + "]");
		log.log("Logging Configuration Information...");
		log.log(config.localConfig().toString());
		log.log(config.logConfig().toString());

		try {
			log.log("Loading Remote Systems Information...");
			remoteSystems = (RemoteSystems) (JAXBContext.newInstance(RemoteSystems.class).createUnmarshaller()).unmarshal(new File(config.remoteConfig().getRemoteSystemsConfigPath()));
			remoteSystems.setConfig(config);
			log.log(remoteSystems.toString());
			remoteSystems.execute();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		log.log("Remote Transfer Application Finished : [" + new Date() + "]");
	}

	public static void showHelp(String[] args) {
		System.out.println("Help : ...");
	}
}
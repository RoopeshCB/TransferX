package rcb.file.trasnfer.scp.refactored.readers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author roopesh-chandra bose
 */
public final class XMLReader implements Reader {
	private final String   fileName;
	private final Document document;
	//private List<RemoteSystem> remoteSystemList = new RemoteSystemList();

	public final String getValue(final String nodeName) {
		return ((Element)(this.document.getElementsByTagName(nodeName)).item(0)).getFirstChild().getNodeValue().trim();
	}

//	public final List<RemoteSystem> getRemoteSystems(final String nodeName) {
//		System.out.println("1.0 : " + nodeName);
//		
//		NodeList nodeList = this.document.getElementsByTagName(nodeName);
//		if (nodeList == null) {
//			System.out.println("  1.1 : " + nodeList);
//			return null;
//		}
//
//		
//		NodeList items = nodeList.item(0).getChildNodes();
//		int length = items.getLength();
//
//		System.out.println("    1.2 : " + length + "/" + items.toString());
//
//		for (int i = 0; i < length; i++) {
//			System.out.println("      1.3 : " + i);
//			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				System.out.println("        1.4 : " + nodeList.item(i).getNodeType() + "/" + Node.ELEMENT_NODE);
//				Element element = (Element) nodeList.item(i);
//				System.out.println("          1.5 : " + element);
//				if (element.getNodeName().contains("system")) {
//                    String name = element.getElementsByTagName("name").item(0).getTextContent();
//                    String user = element.getElementsByTagName("user").item(0).getTextContent();
//                    String password = element.getElementsByTagName("password").item(0).getTextContent();
//                    String path = element.getElementsByTagName("path").item(0).getTextContent();
//                    String encrypted = element.getElementsByTagName("encrypted").item(0).getTextContent();
//                    String execute = element.getElementsByTagName("execute").item(0).getTextContent();
//
//                    remoteSystemList.add(new RemoteSystem(name, user, password, encrypted, path, execute));
//                }
//			}
//		}
//		return remoteSystemList;
//	}

	public XMLReader(final String fileName) throws SAXException, IOException, ParserConfigurationException {
		this.fileName = fileName;
		this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(this.fileName));
    }
}
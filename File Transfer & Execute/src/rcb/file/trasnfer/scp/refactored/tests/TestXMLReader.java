package rcb.file.trasnfer.scp.refactored.tests;

import org.junit.Before;
import org.junit.Test;

import rcb.file.trasnfer.scp.refactored.readers.XMLReader;

/**
 * @author roopesh-chandra bose
 */
public class TestXMLReader {
	private XMLReader reader;

	@Before
	public void setUp() throws Exception {
		reader = new XMLReader("C:\\Users\\roopeshb\\Code\\Java\\File Transfer\\config\\config.xml");
		System.out.println(reader.toString());
	}

	@Test
	public void testToString() {
		//reader.getRemoteSystems("remote");
		//System.out.println(reader.getRemoteSystems("remote").toString());
		//assertTrue(reader.getRemoteSystems("remote").toString().length() > 0);
	}
}
package enigma.test;

import enigma.Debug;
import junit.framework.TestCase;

/**
 * @author Mathias Kub <br />
 * <br />
 * ENIGMA_TEC 2010 <br />
 * technik[at]enigma-ausstellung.at <br />
 * http://enigma-ausstellung.at <br />
 * <br />
 * HTL Rennweg <br />
 * Rennweg 89b <br />
 * A-1030 Wien <br />
 * 
 */
public class DebugTest extends TestCase {

	private final String debugMessage = "DEBUG DUMMY MESSAGE";
	
	/**
	 * Testet das Ausgeben einer Debugmessage
	 */
	public void testPrint() {
		Debug.print(debugMessage);
	}

	/**
	 * ueberprueft ob Debug deaktiviert ist
	 */
	public void testIsDebug() {
		assertEquals(false,Debug.isDebug());
	}

	/**
	 * aktiviert Debug und ueberprueft dann ob es aktiviert ist
	 */
	public void testSetDebug() {
		Debug.setDebug(true);
		assertTrue(Debug.isDebug());
	}

	/**
	 * setzt den Dateinamen und ueberprueft dann ob er richtig gesetzt ist
	 */
	public void testSetFileName() {
		Debug.setFileName("network.properties");
		assertEquals("network.properties", Debug.getFileName());
	}

	/**
	 * setzt den Dateinamen und ueberprueft dann ob er richtig gesetzt ist
	 */
	public void testGetFileName() {
		Debug.setFileName("enigma.properties");
		assertEquals("enigma.properties", Debug.getFileName());
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Debug.setFileName("enigma.properties");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		Debug.setFileName("enigma.properties");
	}
}

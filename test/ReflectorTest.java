package enigma.test;

import junit.framework.TestCase;
import enigma.parts.Reflector;
import enigma.parts.Rotor;

/**
 * Klasse zum Testen der Funktonalitaet des Reflectors
 * 
 * @author Sebastian Chlan <br />
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
public class ReflectorTest extends TestCase {

	/**
	 * Default Konstruktor
	 * 
	 * @param name
	 */
	public ReflectorTest(String name) {
		super(name);
	}

	/**
	 * Main Methode, startet den JUnit Test
	 * 
	 * @param args
	 *            ignorierte Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(ReflectorTest.class);
	}

	/**
	 * Methode, welche einen Rotor erzeugt und diesen dann in mehreren Schritten
	 * testet: - Default-Konstruktor wird ausprobiert - String-Konstruktor wird
	 * ausprobiert - char[]-Konstruktor wird ausprobiert
	 * 
	 * @throws Exception
	 *             falls ein Rotor oder ein Reflector eine Exception wirft
	 */
	public void testReflector() throws Exception {
		Reflector ref = null;
		Rotor r1 = new Rotor();

		String refConfigString = "EJMZALYXVBWFCRQUONTSPIKHGD";
		char refConfigChar[] = refConfigString.toCharArray();
		String name = "testref";

		// Default Konstruktor Test start

		ref = new Reflector();
		r1.setNextMapper(ref);

		ref.setPrevMapper(r1);

		for (int i = 0; i < refConfigString.length(); i++) {
			assertEquals('A' + i, r1.encrypt(refConfigString.charAt(i)));
			/* Das aktuelle Zeichen refConfigString.charAt(i) wird überprüft ob 
			 * diese Zeichen verschluesselt 'A'+i ist
			 * */
		}

		// Default Konstruktor Test ende

		// String Konstruktor Test start

		ref = new Reflector(name, refConfigString);
		ref.setPrevMapper(r1);

		for (int i = 0; i < refConfigString.length(); i++) {
			assertEquals('A' + i, r1.encrypt(refConfigString.charAt(i)));
			/* Das aktuelle Zeichen refConfigString.charAt(i) wird überprüft ob 
			 * diese Zeichen verschluesselt 'A'+i ist
			 * */
		}

		// String Konstruktor Test ende

		// String Konstruktor Test start

		ref = new Reflector(name, refConfigChar);
		ref.setPrevMapper(r1);

		for (int i = 0; i < refConfigString.length(); i++) {
			assertEquals('A' + i, r1.encrypt(refConfigString.charAt(i)));
			/* Das aktuelle Zeichen refConfigString.charAt(i) wird überprüft ob 
			 * diese Zeichen verschluesselt 'A'+i ist
			 * */
		}

		// String Konstruktor Test ende

	}
}

package enigma.test;

import junit.framework.TestCase;
import enigma.Logic;
import enigma.Machine;

/**
 * @author Gerald Schreiber <br />
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
public class MachineTest extends TestCase {

	/**
	 * Default-Konstruktor der Klasse TestCase
	 * 
	 * @param name
	 *            Argument, fuer den Default-Konstruktor
	 */
	public MachineTest(String name) {
		super(name);
	}

	/**
	 * main-Methode, welche den JUnit-Test startet
	 * 
	 * @param args
	 *            Argumente, welche von der Kommandozeile uebernommen, aber
	 *            nicht beachtet werden
	 */
	public static void main(String args[]) {
		junit.swingui.TestRunner.run(Machine.class);
	}

	/**
	 * Methode, welche einen Rotor erzeugt und diesen dann in mehreren Schritten
	 * testet: - Default-Konstruktor wird ausprobiert - String-Konstruktor wird
	 * ausprobiert - char[]-Konstruktor wird ausprobiert
	 * 
	 * @throws Exception
	 *             falls der Rotor eine Exception wirft
	 */
	public void testMachine() throws Exception {
		String[][] availableMappers = { { "Pb" }, { "I", "II", "III" },
				{ "I", "II", "III" }, { "I", "II", "III" }, { "A" } };
		String[] configuration = { "Pb", "II", "III", "I", "A" };
		String name = "I";

		Logic logic = new Logic();
		Machine machine = new Machine(logic, name, availableMappers,
				configuration);

		assertEquals(name, machine.getMachineName());

		assertEquals(configuration.length, machine.getNumberOfMappers());

		assertEquals("Pb", machine.getAvailableMappersOnPosition(0)[0]);

		String message = "Hallo, das ist ein Test!";
		String substitutedMessage = logic.substitute(message);
		String encryptedMessage = machine.encrypt(substitutedMessage);

		machine.setMapper("Pb", 0);
		machine.setMapper("II", 1);
		machine.setMapper("III", 2);
		machine.setMapper("I", 3);
		machine.setMapper("A", 4);

		assertEquals(substitutedMessage, machine.encrypt(encryptedMessage));
	}
}

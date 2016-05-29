package enigma.test;

import junit.framework.TestCase;
import enigma.parts.Plugboard;
import enigma.parts.Reflector;
import enigma.parts.Rotor;

/**
 * @author Philip Woelfel <br />
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
public class MapperTest extends TestCase {

	/**
	 * Default-Konstruktor der Klasse TestCase
	 * 
	 * @param name
	 *            Argument, fuer den Default-Konstruktor
	 */
	public MapperTest(String name) {
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
		junit.swingui.TestRunner.run(Rotor.class);
	}

	/**
	 * Methode, welche einen Rotor erzeugt und diesen dann in mehreren
	 * Schritten testet:
	 * 	- Default-Konstruktor wird ausprobiert
	 * 	- String-Konstruktor wird ausprobiert
	 * 	- char[]-Konstruktor wird ausprobiert
	 * 
	 * @throws Exception
	 *             falls der Rotor eine Exception wirft
	 */
	public void testRotor() throws Exception {
		String setting =  "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
		String setting2 = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
		String setting3 = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
		String refl = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
		String alphabet =    "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//String alphabet = "OHELCPYBSURDZTAFXKINJWVQGM";
		char[] jump = {'Q'};
		char[] jump2 = {'E'};
		char[] jump3 = {'V'};
		
		Plugboard pb = new Plugboard();
		Rotor r1 = new Rotor("I", setting,jump);
		Rotor r2 = new Rotor("II", setting2, jump2);
		Rotor r3 = new Rotor("III", setting3, jump3);
		Reflector re = new Reflector("Reflector", refl);
		
		pb.setNextMapper(r1);
		r1.setNextMapper(r2);
		r2.setNextMapper(r3);
		r3.setNextMapper(re);
		
		re.setPrevMapper(r3);
		r3.setPrevMapper(r2);
		r2.setPrevMapper(r1);
		r1.setPrevMapper(pb);
		
		String enc="";
		for(int i=0;i<alphabet.length();i++){
			char c = alphabet.charAt(i);
			enc+=pb.encrypt(c);
		}
		
		System.out.print("Walzenstellung: ");
		System.out.print(r1.getCharPosition()+" ");
		System.out.print(r2.getCharPosition()+" ");
		System.out.print(r3.getCharPosition()+" ");
		System.out.println();
		
		r1.setPosition('A');
		r2.setPosition('A');
		r3.setPosition('A');
		
		String reenc="";
		for(int i=0;i<enc.length();i++){
			char c = enc.charAt(i);
			reenc+=pb.encrypt(c);
		}
		
		System.out.print("Walzenstellung: ");
		System.out.print(r1.getCharPosition()+" ");
		System.out.print(r2.getCharPosition()+" ");
		System.out.print(r3.getCharPosition()+" ");
		System.out.println();
		
		System.out.println(alphabet);
		System.out.println(enc);
		System.out.println(reenc);
		assertEquals(alphabet, reenc);
	}
}

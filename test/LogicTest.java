package enigma.test;

import java.io.IOException;

import enigmasim.Logic;
import enigmasim.LogicListener;

import junit.framework.TestCase;

/**
 * Test fuer die Logic Klasse
 * Hier wird die Logic Klasse mal so richtig auf Herz und Nieren
 * ueberprueft.
 * @author 4AN <br />
 * <br />
 * ENIGMA_TEC 2010 <br />
 * technik[at]enigma-ausstellung.at <br />
 * http://enigma-ausstellung.at <br />
 * <br />
 * HTL Rennweg <br />
 * Rennweg 89b <br />
 * A-1030 Wien <br />
 */

public class LogicTest extends TestCase implements LogicListener{
	String msg;


	public LogicTest(String name){
		super(name);
	}

	/**
	 * In dieser Methode wird die Klasse
	 * Logic ueberprueft
	 * @throws IOException
	 */
	public void testLogic() throws IOException{
		Logic logic = new Logic();
		assertTrue(logic.addLogicListener(this)); //Kann man erfolgreich einen Listener anmelden?
		
		/*
		 * Testen der Substitute Methode
		 */
		String[] messages = {"Hallo", "Ich bin eine Kleine Teekanne", "Hallo Welt", "äöüß", "Hölläü w123v<lßt", "Seasss Kub"};
		
		assertEquals(logic.substitute(messages[0]), "HALLO");
		assertEquals(logic.substitute(messages[1]), "ICHXBINXEINEXKLEINEXTEEKANNE");
		assertEquals(logic.substitute(messages[2]), "HALLOXWELT");
		assertEquals(logic.substitute(messages[3]), "AEOEUESS");
		assertEquals(logic.substitute(messages[4]), "HOELLAEUEXWEINSZWEIDREIVLSST");
		assertEquals(logic.substitute(messages[5]), "SEASSSXKUB");
		
		/*
		 * Ende der Substitute tests
		 */
		
		/*
		 * Testen der verfuegbaren Maschinen.
		 * Es wird ueberprueft ob die Namen stimmen
		 */
		String[] availableMachines = logic.getAllMachineNames();
		assertEquals(availableMachines[0], "Enigma 1");
		assertEquals(availableMachines[1], "Enigma M4");
		
		/*
		 * Ende der Tests ueber die verfuegbaren Maschinen
		 */
		
		/*
		 * Testen der getMachine(String) Methode
		 */
		assertTrue(logic.getMachine("Enigma 1") != null);
		assertTrue(logic.getMachine("Enigma M4") != null);
		assertTrue(logic.getMachine("Gibts nicht") == null);
		/*
		 *Ende des Testens der getMachine(String) Methode 
		 */
		
		assertTrue(logic.setMachine("Enigma 1")); //Kann man erfolgreich eine Maschine auswaehlen?
		
		logic.setMachine("Enigma 1");
		String positions[];
		boolean correct;
		/*
		 * Testen der Methode getAvailableMappersOnPosition(int) fuer Enigma1
		 */
		
		String[][] availableMappersEnigma1 = {{"Pb"},{"I", "II", "III"},{"I", "II", "III"}, {"I", "II", "III"}, {"A"}};
		correct = true;
		
		for(int i = 0; i < availableMappersEnigma1.length; i++){
			positions = logic.getAvailableMappersOnPosition(i);
			for(int j = 0; j < availableMappersEnigma1[i].length; j++){
				if(!(positions[j].equals(availableMappersEnigma1[i][j]))){
					correct = false;
					break;
				} //endif
			} //endfor
		} //endfor
		
		assertTrue(correct);
		
		/*
		 * Ende des Testes fuer die Methode getAvailableMappersOnPosition(int) fuer Enigma1
		 */
		
		/*
		 * Testen der Methode getAvailableMappersOnPosition(int) fuer Enigma1
		 */
		
		logic.setMachine("Enigma M4");
		String[][] availableMappersEnigmaM4 = {{"Pb"},
				{ "I", "II", "III", "IV", "V", "VI", "VII", "VIII"}, 
			   {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"}, 
			   {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
			   {"Beta", "Gamma"},
			   {"B", "C"}
			   }; 
		
		for(int i = 0; i < availableMappersEnigmaM4.length; i++){
			
			positions = logic.getAvailableMappersOnPosition(i);
			
			for(int j = 0; j < availableMappersEnigmaM4[i].length; j++){
				System.out.println("i:"+i+", j:"+j);
				assertEquals(positions[j],availableMappersEnigmaM4[i][j]);
			} //endfor
		} //endfor
				
		/*
		 * Ende des Testes fuer die Methode getAvailableMappersOnPosition(int) fuer Enigma1
		 */
		
		String[] texts = {"hallo world", "es grünt so grün", "1 little indian", "franz heißt die kanalie%/)%", "1 kleine teekanne"};
		String encryptet = "";
		/*
		 * Testen der encrypt Methode.
		 * Es wird versucht das String Array zu verschluesseln und entschluesseln.
		 * Zum Schluss muss(!) wenn man alles richtig gemacht hat wieder das selbe rauskommen
		 */

		String[] rotorConfigEnigma1 = {"Pb:ABCDEFGHIJKLMNOPQRSTUVWXYZ", "I:F", "II:Q", "III:A", "A:A"};

		for(int i = 0; i < texts.length; i++){
			texts[i] = logic.substitute(texts[i]);
			//Aufrufen der Verschluesselungsmethode
			encryptet = logic.encrypt(texts[i], rotorConfigEnigma1);
			assertEquals(texts[i], logic.encrypt(encryptet, rotorConfigEnigma1));
			
		}
		/*
		 * Ende des Testens der encrypt Methode
		 */
		
		/*
		 * Testen der encrypt Methode mit einer M4
		 */
		logic.setMachine("Enigma M4");
		
		String[] rotorConfigM4 = {"Pb:ABCDEFGHIJKLMNOPQRSTUVWXYZ", "I:X", "II:K", "III:J", "Beta:G", "C:A"};
		
		for(int i = 0; i < texts.length; i++){
			texts[i] = logic.substitute(texts[i]);
			encryptet = logic.encrypt(texts[i], rotorConfigM4);
			assertEquals(texts[i], logic.encrypt(encryptet, rotorConfigM4));
		}
		/*
		 * Ende des Testens der M4 verschluesselung
		 */
		
		/*
		 * Netzwerktest
		 */
		String[] networkMsgs = {"Hallo", "Keine Teekanne", "sdkfhklasdfh", "Oesterreich:Daenemark 2-1"};
		for(int i = 0; i < networkMsgs.length; i++){
			logic.sendMessage(networkMsgs[i]); //Aus dem Netzwerk raussenden
			assertEquals(msg, networkMsgs[i]);
		}
		
		/*
		 * Ende Netzwerktest
		 */
		
		
		
		assertTrue(logic.removeLogicListener(this)); //Kann man erfolgreich einen Listener abmelden?
	}

	public static void main(String args[]){
		junit.swingui.TestRunner.run(Logic.class);
	}

	/**
	 * Die empfangene Nachricht wird in die globale
	 * Variable msg gespeichert.
	 * 
	 * @param text der Text empfangen wird
	 */
	public void sendText(String text) {
		this.msg = text;
	}

	@Override
	public void updateRotorSettings(char[] rotorSetting) {
		// TODO Auto-generated method stub

	}

}

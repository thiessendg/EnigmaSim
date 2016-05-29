package enigma.test;
import java.io.IOException;
import java.net.InetAddress;

import junit.framework.TestCase;
import enigma.Network;
import enigma.NetworkListener;

/**
 * Diese Klasse dient zum Testen der Netzwerkklasse
 * @author Mario Heindl, Mina Toma <br />
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

public class NetworkTest extends TestCase implements NetworkListener {
	private String message;
	
	private static void sleep () {
		try{ Thread.sleep(2000); } catch(Exception e){}
	}
	
	public NetworkTest(String name){
		super(name);
	}
	
	/**
	 * In dieser Methode werden die einzelnen Funktionen der Netzwerkklasse
	 * ueberprueft.
	 * @author Mario Heindl, Mina Toma 
	 * @throws IOException
	 */
	
	/**
	 * Diese Methode testet die einzelnen Funktionen der Netzwerk Klasse
	 * Zunaechst wird ein Objekt der Klasse Network erzeugt.
	 * Anschliessend wird versucht es einer Liste hinzuzufuegen, Nachrichten zu senden und zu empfangen,
	 * sowie den Listener wieder entfernen.
	 * Die assertTrue Methode ueberprueft in der Klammer auf einen Boolean-Wert.
	 */
	public void testNetwork() throws IOException{
		InetAddress address = InetAddress.getByName("224.0.0.2");
		Network nt = new Network(address, 56438);
		sleep();
		
		assertTrue(nt.addNetworkListener(this)); //Kann man einen Listener erfolgreich hinzufuegen?
		
		try{ Thread.sleep(200); } catch(Exception e){}
		String s[] =  {"Hallo Welt", "Test2", "Rapid ist geil", "ENIGMAAAAAAAAAAA", "sdfjhasdklfjhskldfhasdfklhasdfklfdghkldjfghdjklfghsdklghdklfjghkldfjvndjklfngjksdfhvjkdbhfjkghdkfghdfkghklsdghdkgsdfjlghsdfjkghdjkghsdfjklghdjklghkdfjghkldfvhdfjklghjkldfghjksdflhfosdhfdjklasdfhkljshdfkjlshdfklshadrjkshkvjlhsdkfhsdkjfhadjkslfhsdkfhkasdjrhksdfhsdjklfhsjkldfhsdkjfhasdklfhksdfjhsdjklfhksdjlfhskdjfhjklasdfh"};

		for (String msg1 : s) {
			nt.sendText(msg1);
			sleep();
			assertEquals(message, msg1); //Sind der gesendete und empfangene String gleich?
		}		
		
		sleep();		
		assertTrue(nt.removeNetworkListener(this)); //Funktioniert das loeschen eines Listeners?
		
		
	}
	
	/**
	 * Die Main Methode ruft automatisch die JUnit-Tests auf. Man muss also sonst nichts mehr machen.
	 * Der in der Main-Methode ausgeführte Befehl gibt als Parameter an welche Klasse getestet werden 
	 * soll.
	 * @param args
	 */
	public static void main(String args[]){
		junit.swingui.TestRunner.run(Network.class);
	}

	/**
	 * Hier werden die Nachrichten empfangen. Diese werden zu Testzwecken in die Variable Message gespeichert.
	 * @param msg die empfangene Nachricht
	 */
	public void sendRecievedMessage(String msg) {
		this.message = msg;               
	}
}

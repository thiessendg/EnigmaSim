package enigmasim.sniffer;
import java.net.InetAddress;
import java.net.UnknownHostException;

import enigmasim.Network;

/**
 * @author Sebastian Chlan
 *
 * ENIGMA_TEC 2010
 * technik[at]enigma-ausstellung.at
 * http://enigma-ausstellung.at
 * 
 * HTL Rennweg 
 * Rennweg 89b 
 * A-1030 Wien 
 * 
 */
public class Sniffer {
	/**
	 * Die Main Methode erzeugt die GUI fuer den Sniffer. Weiters wird ein Network Objekt erzeugt 
	 * und die Sniffer GUI am NetworkListener angemeldet.
	 * @param args Werden ignoriet
	 */
	public static void main(String[] args) {
		GUISniffer gsniffer = new GUISniffer();
		Network net;
		try {
			net = new Network(InetAddress.getByName("224.0.0.2"), 2200);
			net.addNetworkListener(gsniffer);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
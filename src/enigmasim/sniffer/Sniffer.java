package enigmasim.sniffer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import enigmasim.Network;

/**
 * @author David Thiessen based on previous work by Sebastian Chlan
 *
 */
public class Sniffer {

    /**
     * The Main method generates the GUI for the sniffer. Furthermore, a network
     * object is created and the Sniffer GUI filed Network Listener.
     *
     * @param args cl args ignored
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

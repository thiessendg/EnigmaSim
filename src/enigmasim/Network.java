package enigmasim;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The class Network is responsible for sending and receiving messages over a 
 * multicast address and a specific port
 *
 * @author Mario Heindl, Mina Toma 
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
public class Network implements Runnable, LogicListener {

    private MulticastSocket multiSocket;
    private int port;
    private InetAddress multicastAddr;
    private ArrayList<NetworkListener> listeners = new ArrayList<>();

    /**
     * Constructor for the class Network The multicast socket is in the 
     * constructor creates ({@link #createMultiSocket ()}) and you transgress a 
     * multicast group (multicastAddr, port) at
     *
     * @param multicastAddr the multi cast address
     * @param port the port
     */
    public Network(InetAddress multicastAddr, int port) {
        this.port = port;
        this.multicastAddr = multicastAddr;
        createMultiSocket();
        new Thread(this).start();
    } //Constructor

    /**
     * Default constructor for the class Network. The default port is 2200. 
     * The default multicast address is 224.0.0.2.  Multicast socket is created 
     * in the constructor and you transgress a multicast group 
     * (multicastAddr, port) at
     */
    Network() {

        String localMulticastAddr = 
                getVariableFromFile("network.properties", "multicastAddr");
        String localPort = getVariableFromFile("network.properties", "port");

        this.port = Integer.parseInt(localPort);
        try {
            this.multicastAddr = InetAddress.getByName(localMulticastAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        createMultiSocket();
        new Thread(this).start();

    }

    /**
     * In the run() method, class waits for, addressed to the multicast address,
     * packet and sends it then subsequently to the registered Network Listeners
     * {@link #listeners}
     */
    @Override
    public void run() {
        try {
            byte[] buffer;
            while (true) {
                buffer = new byte[1600];
                DatagramPacket packet = 
                        new DatagramPacket(buffer, buffer.length);
                multiSocket.receive(packet);

                String message = 
                        new String(packet.getData(), 0, packet.getLength());
                if (Debug.isDebug()) {
                    System.out.println(message);
                }
                sendTextToListeners(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This public method is the Interface Logic listener implements the class 
     * {@link} Logic must send with this method the possibility messages over 
     * the network to the multicast address {@link #multicastAddr}
     *
     * @param text the text
     */
    @Override
    public void sendText(String text) {
        byte[] buffer = text.getBytes();
        DatagramPacket packet = 
                new DatagramPacket(buffer, buffer.length, multicastAddr, port);
        try {
            multiSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
	 * @see enigma.LogicListener#updateRotorSettings(char[])
     */
    @Override
    public void updateRotorSettings(char[] rotorSetting) {
    }

    /**
     * This public method is Network Listener can sign and thus further 
     * process, from the Network class, received messages
     *
     * @param listener the listener
     * @return <b>boolean</b>: success of the NetworkListener
     */
    public boolean addNetworkListener(NetworkListener listener) {
        return listeners.add(listener);
    }

    /**
     * This public method is Network Listener can unsubscribe
     *
     * @param listener the listener
     * @return <b>boolean</b>: Erfolg des Entfernens des NetworkListeners
     */
    /*
    public boolean removeNetworkListener(NetworkListener listener) {
        return listeners.remove(listener);
    }
    */
    
    /**
     * This private method is responsible for the sending of the, uebers 
     * network, received messages to registered <b>Network Listener</ b>.<br>
     * Here is the one implemented in the Network listeners, method 
     * {@link Network Listener # sendRecievedMessage (String)} called
     *
     * @param text the text
     */
    private void sendTextToListeners(String text) {
        listeners.stream().forEach((listener) -> 
                listener.sendRecievedMessage(text));
    }

    /**
     * This private method is responsible for creating the multicast sockets and
     * for join the Multicast group.
     */
    private void createMultiSocket() {
        try {
            multiSocket = new MulticastSocket(port);
            multiSocket.joinGroup(multicastAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read a variable from a Properties File
     *
     * @param fileName The properties file
     * @param variable The name of the variable
     * @return a String with the value of the variable
     */
    private String getVariableFromFile(String fileName, String variable) {
        Properties properties = new Properties();
        BufferedReader reader = null;
        try {
            reader = getBufferedReader(fileName);
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return properties.getProperty(variable);
    }

    /**
     * Is back for royalty BufferedReader, whether in a jar file or not.
     *
     * @param fileName the filename for the the BufferedReader is needed
     * @return the BufferedReader for the file
     */
    private BufferedReader getBufferedReader(String fileName) {
        BufferedReader in = null;
        try {
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream("resources/" + fileName), "UTF-8"));
        } catch (Exception e) {
            try {
                return new BufferedReader(new InputStreamReader(
                        Object.class.getResourceAsStream(
                                "/resources/" + fileName), "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
                System.exit(-1);
            } finally {
                closeStream(null);
            }
        }
        return in;
    }

    /**
     * closes a Closeable Object
     *
     * @param closeable the Object to close
     */
    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

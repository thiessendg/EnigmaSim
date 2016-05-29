package enigmasim;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Die Klasse Network ist zustaendig fuer das Empfangen und Versenden von
 * Nachrichten ueber eine Multicastadresse und einen bestimmten Port
 *
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
public class Network implements Runnable, LogicListener {

    private MulticastSocket multiSocket;
    private int port;
    private InetAddress multicastAddr;
    private ArrayList<NetworkListener> listeners = new ArrayList<>();

    /**
     * Konstruktor fuer die Klasse Network </br>
     * Der MulticastSocket wird in dem Konstruktor erzeugt
     * ({@link #createMultiSocket()}) und man tretet einer
     * Multicastgruppe(multicastAddr,port) bei
     *
     * @param multicastAddr
     * @param port
     */
    public Network(InetAddress multicastAddr, int port) {
        this.port = port;
        this.multicastAddr = multicastAddr;
        createMultiSocket();
        new Thread(this).start();
    } //Konstruktor

    /**
     * Standardkonstruktor fuer die Klasse Network <br>
     * Der Standardport ist 2200 <br>
     * Die Multicastadresse ist defaultmaessig 224.0.0.2 <br>
     * Der MulticastSocket wird in dem Konstruktor erzeugt und man tretet einer
     * Multicastgruppe(multicastAddr,port) bei
     */
    public Network() {

        String localMulticastAddr = getVariableFromFile("network.properties", "multicastAddr");
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
     * In der run()-Methode wartet die Klasse auf ein, an die Multicastadresse
     * adressiertes, Paket und schickt diese dann anschliessend an die
     * angemeldeten NetworkListeners {@link #listeners}
     */
    @Override
    public void run() {
        try {
            byte[] buffer;
            while (true) {
                buffer = new byte[1600];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multiSocket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
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
     * Diese oeffentliche Methode wird mit dem Interface LogicListener
     * implementiert <br>
     * Die Klasse {@link Logic} hat mit dieser Methode die Moeglichkeit
     * Nachrichten ueber das Netzwerk an die Multicastadresse
     * {@link #multicastAddr} zu senden
     *
     * @param text
     */
    @Override
    public void sendText(String text) {
        byte[] buffer = text.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, multicastAddr, port);
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
     * Mit dieser oeffentlichen Methode koennen sich NetworkListener anmelden
     * und somit, von der Klasse Network, empfangene Nachrichten
     * weiterverarbeiten
     *
     * @param listener
     * @return <b>boolean</b>: Erfolg des Hinzufuegens des NetworkListeners
     */
    public boolean addNetworkListener(NetworkListener listener) {
        return listeners.add(listener);
    }

    /**
     * Mit dieser oeffentlichen Methode koennen sich NetworkListener abmelden
     *
     * @param listener
     * @return <b>boolean</b>: Erfolg des Entfernens des NetworkListeners
     */
    public boolean removeNetworkListener(NetworkListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Diese private Methode ist fuer das Weitersenden der, uebers Netzwerks,
     * empfangenen Nachrichten an die angemeldeten <b>NetworkListener</b>
     * zustaendig. <br>
     * Dabei wird die, bei den NetworkListenern implementierte, Methode
     * {@link NetworkListener#sendRecievedMessage(String)} aufgerufen
     *
     * @param text
     */
    private void sendTextToListeners(String text) {
        listeners.stream().forEach((listener) -> {
            listener.sendRecievedMessage(text);
        });
    }

    /**
     * Diese private Methode ist fuer das Erstellen des MulticastSockets und
     * fuer das Beitreten der Muticastgruppe zustaendig
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
     * liest eine Variable aus einem Properties-File
     *
     * @param fileName das Properties-File aus dem die Variable gelesen wird
     * @param variable der Name der gesuchten Variable
     * @return ein String mit dem Wert der Variable
     */
    private String getVariableFromFile(String fileName, String variable) {
        Properties properties = new Properties();
        BufferedReader reader = null;
        try {
            reader = getBufferedReader(fileName);
            properties.load(reader);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return properties.getProperty(variable);
    }

    /**
     * Gibt den passenden BufferedReader zurueck, egal ob in eine Jar-File oder
     * nicht.
     *
     * @param fileName der Dateiname fuer den der BufferedReader gebraucht wird
     * @return der BufferedReader fuer das File
     */
    private BufferedReader getBufferedReader(String fileName) {
        BufferedReader in = null;
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream("resources/" + fileName), "UTF-8"));
        } catch (Exception e) {
            try {
                return new BufferedReader(new InputStreamReader(new Object().getClass().getResourceAsStream("/resources/" + fileName), "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
                System.exit(-1);
            } finally {
                closeStream(in);
            }
        }
        return in;
    }

    /**
     * schließt ein Closeable-Object
     *
     * @param closeable das zu schliessende Objekt
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

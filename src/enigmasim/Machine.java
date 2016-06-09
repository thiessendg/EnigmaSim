package enigmasim;

import java.util.ArrayList;

import enigmasim.parts.Mapper;
import enigmasim.parts.Plugboard;
import enigmasim.parts.Reflector;
import enigmasim.parts.Rotor;

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
public class Machine {

    private final Logic logic;
    private final String machineName;
    private final int maxRotors;
    private final String[][] availableMappers;
    private final ArrayList<Mapper> usedMappers;

    /**
     * Konstruktor, der die Machine erzeugt und alle eingebauten Walzen,
     * Reflektoren und das Steckbrett erzeugt
     *
     * @param logic Referenz auf die Logik-Klasse
     * @param machineName Name der Machine
     * @param availableMappers Mappers, welche fuer diese Machine vorhanden sind
     * @param configuration Konfiguration, mit welcher die Machine initialisiert
     * wird
     * @throws Exception falls die Machine nicht erzeugt werden konnte
     */
    public Machine(Logic logic, String machineName, String[][] availableMappers,
            String[] configuration) throws Exception {
        this.logic = logic;
        this.machineName = machineName;
        this.availableMappers = availableMappers;
        this.maxRotors = availableMappers.length;
        this.usedMappers = new ArrayList<>();

        // geht alle Elemente der zu setzenden Konfiguration durch
        for (int i = 0; i < configuration.length; i++) {
            if (isMapperAllowedOnPosition(configuration[i], i)
                    && !mapperAlreadyExists(configuration[i])) {
                // fuegt den neuen Mapper hinzu
                addMapperObject(configuration[i], i);
            } //end if
            else {
                // wirft eine Exception falls der Mapper nicht hinzugefuegt
                // werden konnte
                throw new Exception("Mapper with name " + configuration[i]
                        + " can't be used on position " + i
                        + " or already exists");
            } // end else
        } // end for
        setMapperConnections();
    } // end Konstruktor

    /**
     * Gibt den Namen der Machine zurueck
     *
     * @return Name der Machine
     */
    public String getMachineName() {
        return machineName;
    } // end Methode

    /**
     * Gibt alle fuer diese Position verfuegbaren Mappers zurueck
     *
     * @param position Position, nach der gefragt wird
     * @return String[] mit allen verfuegbaren Mappern
     */
    public String[] getAvailableMappersOnPosition(int position) {
        return availableMappers[position];
    } // end Methode

    /**
     * Setzt einen neuen Mapper an einer definierten Position ein
     *
     * @param name Name des neuen Mappers
     * @param position Position des neuen Mappers
     * @return false wenn der Mapper nicht gesetzt werden konnte
     * @throws Exception falls der neue Mapper nicht gesetzt werden konnte
     */
    boolean setMapper(String name, int position) throws Exception {
        if (mapperAlreadyExists(name)) {
            // exisitierenden Mapper hierher verschieben und aktuellen Mapper
            // dorthin verschieben
            int exisitingPosition = getPositionOfMapper(name);
            Mapper currentMapper = getMapper(position);
            if (isMapperAllowedOnPosition(currentMapper.getName(),
                    exisitingPosition)
                    && isMapperAllowedOnPosition(name, position)) {
                usedMappers.set(exisitingPosition, currentMapper);
                setMapperObject(name, position);
                setMapperConnections();
                return true;
            } //end if
            else {
                return false;
            } // end else
        } // end if
        else if (isMapperAllowedOnPosition(name, position)) {
            setMapperObject(name, position);
            setMapperConnections();
            return true;
        } // end else if
        return false;
    } // end Methode

    /**
     * Gibt eine Referenz auf einen Mapper zurueck
     *
     * @param position Position des Mappers
     * @return Referenz des Mappers an der uebergebenen Position
     */
    public Mapper getMapper(int position) {
        return usedMappers.get(position);
    } // end Methode

    /**
     * Gibt die Anzahl an setzbaren Mappers zurueck
     *
     * @return Anzahl der setzbaren Mapper
     */
    public int getNumberOfMappers() {
        return maxRotors;
    } // Methode

    /**
     * Verschluesselt die uebergebene Nachricht
     *
     * @param message Nachricht zum Entschluesseln
     * @return verschluesselte Nachricht
     */
    String encrypt(String message) {
        String encryptedMessage = "";
        for (int i = 0; i < message.length(); i++) {
            encryptedMessage += usedMappers.get(0).encrypt(message.charAt(i));
        } // end for
        return encryptedMessage;
    } // end Methode

    /**
     * Setzt die Verbindungen des Plugboards neu
     *
     * @param setting String mit den neuen Verbindungen
     * @throws Exception falls die Verbindungen nicht neu gesetzt werden konnten
     */
    void setPlugboardConnections(String setting) throws Exception {
        Plugboard pb = (Plugboard) usedMappers.get(0);
        pb.setConnections(setting);
    } // end Methode


    /**
     * Setzt die Startposition eines spezifischen Rotors
     *
     * @param position Position des Rotors
     * @param startPosition Startposition auf die der Rotor gesetzt werden soll
     */
    void setStartPosition(int position, char startPosition) {
        if (usedMappers.get(position) instanceof Rotor) {
            Rotor ro = (Rotor) usedMappers.get(position);
            ro.setPosition(startPosition);
        } // end if
    } // end Methode

    /**
     * Set the ring setting, Ringstellung, for the rotor
     *
     * @param position Position of the Rotor
     * @param ringoffset ringoffset the rimng offset for the rotor
     */
    void setRingSetting(int position, String ringoffset) {
        if (usedMappers.get(position) instanceof Rotor) {
            Rotor ro = (Rotor) usedMappers.get(position);
            ro.setRingOffset(ringoffset);
        } // end if
    } // end Methode
    
    /**
     * Ueberprüft, ob ein Mapper bereits exisitiert
     *
     * @param name Name, des Mappers
     * @return true, wenn der Mapper bereits exisitiert und false, wenn der
     * Mapper noch nicht exisitiert
     */
    private boolean mapperAlreadyExists(String name) {
        for (Mapper mapper : usedMappers) {
            if (name.equals(mapper.getName())) {
                return true;
            } // end if
        } // end for
        return false;
    } // end Methode

    /**
     * Gibt die aktuelle Konfiguration zurueck
     *
     * @return String[] mit der aktuellen Konfiguration
     */
    public String[] getCurrentConfiguration() {
        String[] config = new String[usedMappers.size()];
        for (int i = 0; i < usedMappers.size(); i++) {
            config[i] = usedMappers.get(i).getName();
        }
        return config;
    }

    /**
     * Ueberprüft, ob der Mapper an der uebergebenen Position erlaubt ist
     *
     * @param name Name des Mappers
     * @param position Position, an welcher der Mapper positioniert werden soll
     * @return true, wenn er positioniert werden kann und false, wenn er nicht
     * positioniert werden kann
     */
    private boolean isMapperAllowedOnPosition(String name, int position) {
        if (position >= 0 && position < availableMappers.length) {
            for (String availableMapper : availableMappers[position]) {
                if (name.equals(availableMapper)) {
                    return true;
                } // end if
            } // end for
        } // end if
        return false;
    } // end Methode

    /**
     * Fuegt einen neuen Mapper hinzu
     *
     * @param name Names des neuen Mappers
     * @param position Position des neuen Mappers
     * nicht moeglich
     * @throws Exception falls Mapper nicht erzeugt werden konnte
     */
    private void addMapperObject(String name, int position) throws Exception {
        String config = logic.getMapperConfig(name);
        String type = config.split(":")[0];
        String setting = config.split(":")[1];
        char[] chumpchars = config.split(":")[2].toCharArray();
        if (null != type) {
            switch (type) {
                case "Ro":
                    usedMappers.add(position, new Rotor(name, setting, chumpchars));
                    break;
                case "Re":
                    usedMappers.add(position, new Reflector(name, setting));
                    break;
                case "Pb":
                    usedMappers.add(position, new Plugboard(name, setting));
                    break;
                default:
                    break;
            }//end string switch
        }
    } // end Methode

    /**
     * Eretzt einen bestehenden Mapper
     *
     * @param name Names des Mappers
     * @param position Position des Mappers
     * nicht moeglich
     * @throws Exception falls Mapper nicht erzeugt werden konnte
     */
    private void setMapperObject(String name, int position) throws Exception {
        String config = logic.getMapperConfig(name);
        String type = config.split(":")[0];
        String setting = config.split(":")[1];
        char[] chumpchars = config.split(":")[2].toCharArray();
        if (null != type) {
            switch (type) {
                case "Ro":
                    usedMappers.set(position, new Rotor(name, setting, chumpchars));
                    break;
                case "Re":
                    usedMappers.set(position, new Reflector(name, setting));
                    break;
                case "Pb":
                    usedMappers.set(position, new Plugboard(name, setting));
                    break;
                default:
                    break;
            }//end string switch
        }
    } // end Methode

    /**
     * Setzt die Nachbarn jedes einzelnen Mappers
     */
    private void setMapperConnections() {
        for (int j = 1; j < usedMappers.size() - 1; j++) {
            // setzt den folgenden Rotor in der ArrayList als naechsten Nachbarn
            // dieses Rotors
            usedMappers.get(j).setNextMapper(usedMappers.get(j + 1));
            // setzt den vorherigen Rotor in der ArrayList als voherigen
            // Nachbarn dieses Rotors
            usedMappers.get(j).setPrevMapper(usedMappers.get(j - 1));
        } // end for
        // setzt den ersten Rotor in der ArrayList als naechsten Nachbarn des
        // Plugboards
        usedMappers.get(0).setNextMapper(usedMappers.get(1));
        // setzt den letzten Rotor in der ArrayList als vorherigen Nachbarn des
        // Reflectors
        usedMappers.get(usedMappers.size() - 1).setPrevMapper(
                usedMappers.get(usedMappers.size() - 2));
    } // end Methode

    /**
     * Gibt die Position eines Mappers zurueck
     *
     * @param name Name des Mappers
     * @return Position des Mappers
     */
    private int getPositionOfMapper(String name) {
        for (int i = 0; i < usedMappers.size(); i++) {
            if (name.equals(usedMappers.get(i).getName())) {
                return i;
            } // end if
        } // end for
        return -1;
    } // end Methode

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String new_line = System.getProperty("line.separator");
        StringBuilder str = new StringBuilder();
        str.append("Maschinenname: ").append(machineName).append(new_line);
        str.append("Maximale Walzenanzahl: ").append(maxRotors).append(new_line);
        str.append("Verfuegbare Walzen");
        for (int i = 0; i < availableMappers.length; i++) {
            str.append(new_line).append(i).append(". Walze:");
            for (String availableMapper : availableMappers[i]) {
                str.append(" ").append(availableMapper);
            } // end for
        } // end for
        str.append(new_line).append("Benutzte Walzen: ");
        for (Mapper usedMapper1 : usedMappers) {
            str.append(" ").append(usedMapper1.getName());
        } // end for
        str.append(new_line).append("Walzenstellung: ");
        for (Mapper usedMapper : usedMappers) {
            if (usedMapper instanceof Rotor) {
                Rotor rot = (Rotor) usedMapper;
                str.append(" ").append(rot.getCharPosition());
            } // end if
        } // end for
        return str.toString();
    } // end Method

    /**
     * Liefert alle Walzenpositionen in einem char[].
     *
     * @return - char[] mit allen Walzenpositionen
     */
    char[] getCurrentRotorPositions() {
        StringBuilder sb = new StringBuilder();
        usedMappers.stream().filter((m) -> (m instanceof Rotor)).forEach((m) -> sb.append(((Rotor) m).getCharPosition()));

        char[] currentRotorPositions = new char[sb.toString().length()];
        for (int i = 0; i < currentRotorPositions.length; i++) {
            currentRotorPositions[i] = sb.charAt(i);
        }
        return currentRotorPositions;
    }

} // end Klasse Machine

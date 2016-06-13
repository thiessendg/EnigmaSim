package enigmasim;

import java.util.ArrayList;

import enigmasim.parts.Mapper;
import enigmasim.parts.Plugboard;
import enigmasim.parts.Reflector;
import enigmasim.parts.Rotor;

/**
 * @author Gerald Schreiber 
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
public class Machine {

    private final Logic logic;
    private final String machineName;
    private final int maxRotors;
    private final String[][] availableMappers;
    private final ArrayList<Mapper> usedMappers;

    /**
     * Constructor which generates the machine and generates all built rotors, 
     * reflectors and the plugboard
     *
     * @param logic the Logic Class
     * @param machineName 
     * @param availableMappers Mappers available for this Machine
     * @param configuration Configuration, with which the machine is initialized
     * @throws Exception if Machine couldn't be created
     */
    public Machine(Logic logic, String machineName, String[][] availableMappers,
            String[] configuration) throws Exception {
        this.logic = logic;
        this.machineName = machineName;
        this.availableMappers = availableMappers;
        this.maxRotors = availableMappers.length;
        this.usedMappers = new ArrayList<>();

        // goes through all elements of the configuration to be set
        for (int i = 0; i < configuration.length; i++) {
            if (isMapperAllowedOnPosition(configuration[i], i)
                    && !mapperAlreadyExists(configuration[i])) {
                // adds the new Mapper
                addMapperObject(configuration[i], i);
            } //end if
            else {
                // throws an exception if the Mapper could not be added
                throw new Exception("Mapper with name " + configuration[i]
                        + " can't be used on position " + i
                        + " or already exists");
            } // end else
        } // end for
        setMapperConnections();
    } // end Constructor

    /**
     * Returns the name of the Machine
     *
     * @return name of the Machine
     */
    public String getMachineName() {
        return machineName;
    } // end method

    /**
     * Is all for this position available mapper back
     *
     * @param position 
     * @return String[] servicing all available mappers
     */
    public String[] getAvailableMappersOnPosition(int position) {
        return availableMappers[position];
    } // end method

    /**
     * Sets a new Mapper at a defined position
     *
     * @param name name of the new Mapper
     * @param position position of the new Mapper
     * @return false when the Mapper could not be set
     * @throws Exception if the new Mapper could not be set
     */
    boolean setMapper(String name, int position) throws Exception {
        if (mapperAlreadyExists(name)) {
            //Move existing Mapper here and move current Mapper there
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
    } // end method

    /**
     * Returns a reference back to a Mapper
     *
     * @param position position of the Mappers
     * @return Reference of the mapper at given position
     */
    public Mapper getMapper(int position) {
        return usedMappers.get(position);
    } // end method

    /**
     * Specifies the number of settable Mapper
     * @return number of the usable Mapper
     */
    public int getNumberOfMappers() {
        return maxRotors;
    } // method

    /**
     * Encrypts the message passed
     *
     * @param message message to encrypt
     * @return encrypted message
     */
    String encrypt(String message) {
        //String encryptedMessage = "";
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            //encryptedMessage += usedMappers.get(0).encrypt(message.charAt(i));
            buf.append(usedMappers.get(0).encrypt(message.charAt(i)));
        } // end for
        String encryptedMessage = buf.toString();
        return encryptedMessage;
    } // end method

    /**
     * Sets the PlugBoards connections
     * @param setting 
     * @throws Exception if connections can't be set
     */
    void setPlugboardConnections(String setting) throws Exception {
        Plugboard pb = (Plugboard) usedMappers.get(0);
        pb.setConnections(setting);
    } // end method

    /**
     * Sets the starting position of a specific rotor
     *
     * @param position position of the rotor
     * @param startPosition position to be set on the rotor
     */
    void setStartPosition(int position, char startPosition) {
        if (usedMappers.get(position) instanceof Rotor) {
            Rotor ro = (Rotor) usedMappers.get(position);
            ro.setPosition(startPosition);
        } // end if
    } // end method

    /**
     * Set the ring setting, Ringstellung, for the rotor
     *
     * @param position Position of the Rotor
     * @param ringoffset the ring offset for the rotor
     */
    void setRingSetting(int position, String ringoffset) {
        if (usedMappers.get(position) instanceof Rotor) {
            Rotor ro = (Rotor) usedMappers.get(position);
            ro.setRingOffset(ringoffset);
        } // end if
    } // end method

    /**
     * Checks whether a mapper already exists
`    *
     * @param name of the Mappers
     * @return boolean indicating if the mapper already exists
     */
    private boolean mapperAlreadyExists(String name) {
        return usedMappers.stream().anyMatch((mapper) -> (name.equals(mapper.getName())));
    } // end method

    /**
     * returns the current configuration
     *
     * @return String[] with the current configuration
     */
    public String[] getCurrentConfiguration() {
        String[] config = new String[usedMappers.size()];
        for (int i = 0; i < usedMappers.size(); i++) {
            config[i] = usedMappers.get(i).getName();
        }
        return config;
    }

    /**
     * Checks if the mapper is allowed at the location you passed
     *
     * @param name of the Mappers
     * @param position at which the mapper is to be positioned
     * @return boolean indicating if allowed on the position
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
    } // end method

    /**
     * Adds a new Mapper
     *
     * @param name of the new Mappers
     * @param position of the new Mappers
     * @throws Exception if Mapper could not be created
     */
    private void addMapperObject(String name, int position) throws Exception {
        String config = logic.getMapperConfig(name);
        String type = config.split(":")[0];
        String setting = config.split(":")[1];
        char[] notch = config.split(":")[2].toCharArray();
        if (null != type) {
            switch (type) {
                case "Ro":
                    usedMappers.add(position, new Rotor(name, setting, notch));
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
        }//end if
    } // end method

    /**
     * Replaces an existing Mapper
     *
     * @param name of the Mappers
     * @param position of the Mappers
     * @throws Exception if Mapper couldn't be created
     */
    private void setMapperObject(String name, int position) throws Exception {
        String config = logic.getMapperConfig(name);
        String type = config.split(":")[0];
        String setting = config.split(":")[1];
        char[] notch = config.split(":")[2].toCharArray();
        if (null != type) {
            switch (type) {
                case "Ro":
                    usedMappers.set(position, new Rotor(name, setting, notch));
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
        }//endif
    } // end method

    /**
     * Sets the neighbors of each mapper
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
    } // end method

    /**
     * returns the position of a specified mapper
     *
     * @param name of the Mappers
     * @return position of the mappers
     */
    private int getPositionOfMapper(String name) {
        for (int i = 0; i < usedMappers.size(); i++) {
            if (name.equals(usedMappers.get(i).getName())) {
                return i;
            } // end if
        } // end for
        return -1;
    } // end method

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
        usedMappers.stream().forEach((usedMapper1) -> {
            str.append(" ").append(usedMapper1.getName());
        }); // end for
        str.append(new_line).append("Walzenstellung: ");
        // end if
        // end for
        usedMappers.stream().filter(usedMapper -> usedMapper instanceof Rotor).forEachOrdered(usedMapper -> {
            Rotor rot = (Rotor) usedMapper;
            str.append(" ").append(rot.getCharPosition());
        });
        return str.toString();
    } // end Method

    /**
     * Return all the rotor positions in a char [].
     *
     * @return char[] of all rotor positions
     */
    char[] getCurrentRotorPositions() {
        StringBuilder sb = new StringBuilder();
        usedMappers.stream().filter((m) -> (m instanceof Rotor)).forEach((m)
                -> sb.append(((Rotor) m).getCharPosition()));

        char[] currentRotorPositions = new char[sb.toString().length()];
        for (int i = 0; i < currentRotorPositions.length; i++) {
            currentRotorPositions[i] = sb.charAt(i);
        }
        return currentRotorPositions;
    }//end method
} // end Class Machine

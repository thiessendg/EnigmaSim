package enigmasim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import enigmasim.gui.ChangeInfo;
import java.util.function.Consumer;

/**
 * @author David Thiessen based on work by: Philip Woelfel, Sebastian Chlan 
 * 
 */
public class Logic {

    /**
     * All Listeners, who have registered in the logic class.
     *
     */
    private final ArrayList<LogicListener> listeners = new ArrayList<>();

    /**
     * All machines, which can be used (ie were already generated).
     * <Machine name, machine object>
     *
     */
    private final HashMap<String, Machine> availableMachines = new HashMap<>();

    /**
     * The currently used machine.
     *
     */
    private String usedMachine;

    /**
     * Which mapper which configuration (wiring?)
     * <Name of the mapper(shortcut), wiring> For wiring: string looks like
     * this: [TypeMappers]: [wiring]: [supporting notches] If for no notches
     * exist, empty string' '.
     *
     */
    private final HashMap<String, String> allMappersConfig = new HashMap<>();

    /**
     * Initially the possible rolls and then the initial machines will be
     * created.
     *
     */
    public Logic() {
        /**
         * Here is the config type stuff. Ro indicates that it is a rotor Re
         * indicates a reflector case Re and Pb, which do not rotate, space at
         * the end of the configuration strings must be specified! and for Greek
         * rotors!
         *
         */

        //Plugboard
        allMappersConfig.put("Pb", "Pb:ABCDEFGHIJKLMNOPQRSTUVWXYZ: ");

        //Rotors ID:Type:Setting:Notch
        allMappersConfig.put("I", "Ro:EKMFLGDQVZNTOWYHXUSPAIBRCJ:Q"); // 1930 Enigma I
        allMappersConfig.put("II", "Ro:AJDKSIRUXBLHWTMCQGZNPYFVOE:E"); //1930 Enigma I
        allMappersConfig.put("III", "Ro:BDFHJLCPRTXVZNYEIWGAKMUSQO:V");//1930 Enigma I
        allMappersConfig.put("IV", "Ro:ESOVPZJAYQUIRHXLNFTGKDCMWB:J"); //1938 M3 Army
        allMappersConfig.put("V", "Ro:VZBRGITYUPSDNHLXAWMJQOFECK:Z");  //1938 M3 Army
        allMappersConfig.put("VI", "Ro:JPGVOUMFYQBENHZRDKASXLICTW:ZM");//1939 M3/M4 Naval (FEB 1942)
        allMappersConfig.put("VII", "Ro:NZJHGRCXMYSWBOUFAIVLPEKQDT:ZM");//1939 M3/M4 Naval (FEB 1942)
        allMappersConfig.put("VIII", "Ro:FKQHTLXOCBJSPDZRAMEWNIUYGV:ZM");//1939 M3/M4 Naval (FEB 1942)
        allMappersConfig.put("β", "Ro:LEYJVCNIXWPBQMDRTAKZGFUHOS: "); //Spring 1941 M4 R2
        allMappersConfig.put("γ", "Ro:FSOKANUERHMBTIYCWLQPZXVGJD: "); //Spring 1941 M4 R2

        //Reflectors
        //allMappersConfig.put("ETW", "Re:ABCDEFGHIJKLMNOPQRSTUVWXYZ: "); //Reflektor ETW Enigma I
        allMappersConfig.put("A", "Re:EJMZALYXVBWFCRQUONTSPIKHGD: "); //Before WWII
        allMappersConfig.put("B", "Re:YRUHQSLDPXNGOKMIEBFZCWVJAT: "); //Standard Reflektor
        allMappersConfig.put("C", "Re:FVPJIAOYEDRZXWGCTKUQSBNMHL: "); //Reflektor used temp during WWII
        allMappersConfig.put("B thin", "Re:ENKQAUYWJICOPBLMDXZVFTHRGS: "); //1940 	M4 R1 (M3 + Thin)
        allMappersConfig.put("C thin", "Re:RDOBJNTKVEHMLFCWZAXGYIPSUQ: "); //1940 	M4 R1 (M3 + Thin)

        addInitialMachines();
    }

    /**
     * The method returns back machines there are to choose from.
     *
     * @return array of all machine names
     *
     */
    public String[] getAllMachineNames() {
        String[] names = new String[availableMachines.size()];
        int i = 0;
        for (String m : availableMachines.keySet()) {
            names[i++] = availableMachines.get(m).getMachineName();
        }
        Arrays.sort(names);
        return names;
    } //getAllMachineNames()

    /**
     * The function sets the selected machine.
     *
     * @param machine name of the machine
     * @return false if the Machine has not been set
     */
    public boolean setMachine(String machine) {
        String[] allMach = getAllMachineNames();
        for (String allMach1 : allMach) {
            if (allMach1 == null ? machine == null : allMach1.equals(machine)) {
                this.usedMachine = machine;
                return true;
            }
        }
        return false;
    }

    /**
     * The method returns back an available machine by name.
     *
     * @param name the name of this Machine
     * @return the Machine Object named
     */
    public Machine getMachine(String name) {
        return availableMachines.get(name);
    }

    /**
     * The method is used to encrypt a text. First, the rollers, the breadboard 
     * and the reflector are configured. Thereafter, the encrypt method is 
     * called when Plugboard.
     *
     * @param msg the String to be encrypted
     * @param rotorConfiguration an array of strings with rotor configuration
     * @return the resulting encrypted String
     */
    public String encrypt(String msg, String[] rotorConfiguration) {
        String name;
        char startPosition;
        String offset = "";
        try {
            availableMachines.get(usedMachine).setMapper(
                    rotorConfiguration[0].split(":")[0], 0);
            availableMachines.get(usedMachine).setPlugboardConnections(
                    rotorConfiguration[0].split(":")[1]);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i < rotorConfiguration.length; i++) {
            String[] split = rotorConfiguration[i].split(":");
            name = split[0];
            startPosition = split[1].charAt(0);
            if (i != rotorConfiguration.length - 1) {
                offset = split[2];
            }
            try {
                availableMachines.get(usedMachine).setMapper(name, i);
                availableMachines.get(usedMachine).setStartPosition(i, 
                        startPosition);
                if (i != rotorConfiguration.length - 1) {
                    availableMachines.get(usedMachine).setRingSetting(i, 
                            offset);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String encrText = availableMachines.get(usedMachine).encrypt(msg);
        sendRotorPositionsToListeners(availableMachines.get(usedMachine).
                getCurrentRotorPositions());
        return encrText;
    }

    /**
     * This method sends a message to all logged Logic Listeners
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            listeners.stream().forEach(new ConsumerImpl(message));
        }
    }

    /**
     * sends a new rotor position to all registered listeners Logic
     *
     * @param c char array with rotor positions
     */
    private void sendRotorPositionsToListeners(char[] c) {
        listeners.stream().forEach((LogicListener l) -> 
                l.updateRotorSettings(c));
    }

    /**
     * The method adds a Listener to the list
     *
     * @param l the LogicListener
     * @return false when the listener could not be added
     */
    boolean addLogicListener(LogicListener l) {
        return listeners.add(l);
    }

    /**
     * Get the current configuration of the mapper. Name of the mapper chosen 
     * from list.
     *
     * @param mapperName name of the Mapper
     * @return a String with the configuration of the mapper
     */
    String getMapperConfig(String mapperName) {
        return allMappersConfig.get(mapperName);
    }

    /**
     * This method prepares the text for the post. It is looked up in a HashMap, 
     * if the letter is not permissible, it is replaced with its value from the 
     * list. Should there be an illegal character (outside A-Z), it will be 
     * deleted.
     *
     * @param msg the String to be substituted
     * @return a String (any invalid characters deleted)
     */
    public String substitute(String msg) {
        boolean substituted = false;
        HashMap<Character, String> substitution = new HashMap<>();

        substitution.put('\u00C4', "AE");
        substitution.put('\u00D6', "OE");
        substitution.put('\u00DC', "UE");
        substitution.put('\u00DF', "SS");
        substitution.put('0', "NULL");
        substitution.put('1', "EINS");
        substitution.put('2', "ZWEI");
        substitution.put('3', "DREI");
        substitution.put('4', "VIER");
        substitution.put('5', "FUENF");
        substitution.put('6', "SECHS");
        substitution.put('7', "SIEBEN");
        substitution.put('8', "ACHT");
        substitution.put('9', "NEUN");
        substitution.put('\u0020', "X");
        substitution.put('\n', "X");

        String repl = msg.toUpperCase();
        repl = repl.replaceAll("[^A-ZÄÖÜ0-9 ]*", "");
        if (Debug.isDebug()) {
            System.out.println("msg: \"" + msg + "\" " + msg.length());
            System.out.println("repl: \"" + repl + "\" " + repl.length());
        }
        if (!msg.toUpperCase().equals(repl.toUpperCase())) {
            substituted = true;
        }
        repl = repl.trim();

        for (Character key : substitution.keySet()) {
            repl = repl.replaceAll(key + "", substitution.get(key));
        }
        if (Debug.isDebug()) {
            System.out.println(substituted);
        }
        if (substituted) {
            ChangeInfo changeInfo = new ChangeInfo("Information");
        }

        return repl;
    }

    /**
     * Adds a machine to the list of available machines.
     *
     * @param machine a String containing the name of the machine
     * @param availableMappersa 2D array of Strings for possible additional 
     * Mappers for the positions
     * @param configuration default configuration for the Mapper
     */
    private void createMachine(String machine, String[][] availableMappers, 
            String[] configuration) {
        try {
            availableMachines.put(machine, new Machine(this, machine, 
                    availableMappers, configuration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the Enigma's to the available equipment
     */
    private void addInitialMachines() {
        String nameEnigma1 = "Enigma I";
        String[][] availableMappersEnigma1 = {
            {"Pb"},
            {"I", "II", "III"},
            {"I", "II", "III"},
            {"I", "II", "III"},
            {"A", "B", "C"}};
        String[] configurationEnigma1 = {"Pb", "III", "II", "I", "A"};
        createMachine(nameEnigma1, availableMappersEnigma1,
                configurationEnigma1);

        String nameEnigmaM3 = "Enigma M3";
        String[][] availableMappersEnigmaM3 = {
            {"Pb"},
            {"I", "II", "III", "IV", "V"},
            {"I", "II", "III", "IV", "V"},
            {"I", "II", "III", "IV", "V"},
            {"B", "C"}};
        String[] configurationEnigmaM3 = {"Pb", "III", "II", "I", "B"};
        createMachine(nameEnigmaM3, availableMappersEnigmaM3,
                configurationEnigmaM3);

        String nameEnigmaM3K = "Enigma M3 Navy";
        String[][] availableMappersEnigmaM3K = {
            {"Pb"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"B", "C"}};
        String[] configurationEnigmaM3K = {"Pb", "III", "II", "I", "B"};
        createMachine(nameEnigmaM3K, availableMappersEnigmaM3K,
                configurationEnigmaM3K);

        String nameEnigmaM4 = "Enigma M4";
        String[][] availableMappersEnigmaM4 = {
            {"Pb"},
            {"I", "II", "III", "IV", "V"},
            {"I", "II", "III", "IV", "V"},
            {"I", "II", "III", "IV", "V"},
            {"β", "γ"},
            {"B thin", "C thin"}};
        String[] configurationEnigmaM4 = {"Pb", "III", "II", "I", "β", "B thin"};
        createMachine(nameEnigmaM4, availableMappersEnigmaM4,
                configurationEnigmaM4);

        String nameEnigmaM4K = "Enigma M4 Navy";
        String[][] availableMappersEnigmaM4K = {
            {"Pb"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
            {"β", "γ"},
            {"B thin", "C thin"}};
        String[] configurationEnigmaM4K = {"Pb", "III", "II", "I", "β", "B thin"};
        createMachine(nameEnigmaM4K, availableMappersEnigmaM4K,
                configurationEnigmaM4K);
    }

    private static class ConsumerImpl implements Consumer<LogicListener> {

        private final String message;

        ConsumerImpl(String message) {
            this.message = message;
        }

        @Override
        public void accept(LogicListener l) {
            l.sendText(message);
        }
    }
}

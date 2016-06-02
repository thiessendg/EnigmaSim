package enigmasim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import enigmasim.gui.ChangeInfo;
import java.util.function.Consumer;

/**
 * Die Klasse erzeugt Maschinen, legt die Walzenkonfiguration fest und leitet
 * die verschluesselung weiter.
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
public class Logic {

    /**
     * Alle Listeners, die sich bei der Logic-Klasse angemeldet haben.
     */
    private final ArrayList<LogicListener> listeners = new ArrayList<>();

    /**
     * Alle Maschinen, die man benutzen kann (also schon erzeugt wurden).
     * <Maschinenname, Maschinenobjekt>
     */
    private final HashMap<String, Machine> availableMachines = new HashMap<>();

    /**
     * Die zur Zeit benutzte Maschine.
     */
    private String usedMachine;

    /**
     * Welcher Mapper hat welche Konfiguration (Verdrahtung?)
     * <Name des Mappers (Kuerzel), Verdrahtung>
     * Zu Verdrahtung: String sieht wie folgt aus: [Art des
     * Mappers]:[Verdrahtung]:[Uebertragskerben] Wenn keine Uebertragskerben
     * vorhanden, Leerstring ' ' anfuegen.
     */
    private final HashMap<String, String> allMappersConfig = new HashMap<>();

    /**
     * Zuerst werden die moeglichen Walzen und dann die anfaenglichen Maschinen
     * erstellt.
     */
    public Logic() {

        /*
		 * Hier werden die Walzen konfiguriert
		 * Ro gibt an dass es sich um eine Walze handelt
		 * Re gibt an dass es sich um einen Reflektor handelt
		 * Bei Re oder Ro, die sich nicht drehen, muss ein Leerzeichen am Ende des Konfigurations-Strings angegeben werden!
         */

        //Plugboard
        allMappersConfig.put("Pb", "Pb:ABCDEFGHIJKLMNOPQRSTUVWXYZ: : ");

        //Rotors ID:Type:Setting:Offset:Notch
        allMappersConfig.put("I", "Ro:EKMFLGDQVZNTOWYHXUSPAIBRCJ:1:Q"); //1930 	Enigma I
        allMappersConfig.put("II", "Ro:AJDKSIRUXBLHWTMCQGZNPYFVOE:2:E"); //1930 	Enigma I
        allMappersConfig.put("III", "Ro:BDFHJLCPRTXVZNYEIWGAKMUSQO:1:V"); //1930 	Enigma I
        allMappersConfig.put("IV", "Ro:ESOVPZJAYQUIRHXLNFTGKDCMWB:21:J"); //December 1938 	M3 Army
        allMappersConfig.put("V", "Ro:VZBRGITYUPSDNHLXAWMJQOFECK:12:Z"); //December 1938 	M3 Army
        allMappersConfig.put("VI", "Ro:JPGVOUMFYQBENHZRDKASXLICTW:1:ZM"); //1939 	M3 & M4 Naval (FEB 1942)
        allMappersConfig.put("VII", "Ro:NZJHGRCXMYSWBOUFAIVLPEKQDT:1:ZM"); //1939 	M3 & M4 Naval (FEB 1942)
        allMappersConfig.put("VIII", "Ro:FKQHTLXOCBJSPDZRAMEWNIUYGV:1:ZM"); //1939 	M3 & M4 Naval (FEB 1942)
        allMappersConfig.put("β", "Ro:LEYJVCNIXWPBQMDRTAKZGFUHOS:1: "); //Spring 1941 	M4 R2
        allMappersConfig.put("γ", "Ro:FSOKANUERHMBTIYCWLQPZXVGJD:1: "); //Spring 1941 	M4 R2

        //Reflectors
        allMappersConfig.put("A", "Re:EJMZALYXVBWFCRQUONTSPIKHGD: : "); //Before WWII
        allMappersConfig.put("B", "Re:YRUHQSLDPXNGOKMIEBFZCWVJAT: : "); //Standard Reflektor
        allMappersConfig.put("C", "Re:FVPJIAOYEDRZXWGCTKUQSBNMHL: : "); //Reflektor used temp during WWII
        //allMappersConfig.put("ETW", "Re:ABCDEFGHIJKLMNOPQRSTUVWXYZ: "); //Reflektor ETW Enigma I
        allMappersConfig.put("B thin", "Re:ENKQAUYWJICOPBLMDXZVFTHRGS: : "); //1940 	M4 R1 (M3 + Thin)
        allMappersConfig.put("C thin", "Re:RDOBJNTKVEHMLFCWZAXGYIPSUQ: : "); //1940 	M4 R1 (M3 + Thin)

        addInitialMachines();
    }

    /**
     * Die Methode gibt zurueck welche moeglichen Maschinen es zur Auswahl gibt.
     *
     * @return Array mit allen Maschinennamen
     */
    public String[] getAllMachineNames() {
        String[] names = new String[availableMachines.size()];
        //FIXME: notlösung von daniel - kA obs korrekt ist, aber bei availableMachines
        // erfordert die get anweisung ein object des typs String.
        int i = 0;
        for (String m : availableMachines.keySet()) {
            names[i++] = availableMachines.get(m).getMachineName();
        }
        //sort the names array
        Arrays.sort(names);
        return names;
    } //getAllMachineNames()

    /**
     * Die Funktion setzt die ausgewaehlte Maschine.
     *
     * @param machine der Name der Maschine
     * @return false wenn die Maschine nicht gesetzt wurde
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
     * Die Methode liefert eine verfuegbare Maschine anhand des Namens zurueck.
     *
     * @param name der Name der gesuchten Maschine
     * @return das Machine Objekt mit dem Namen
     */
    public Machine getMachine(String name) {
        return availableMachines.get(name);
    }

    /**
     * Die Methode dient zum verschluesseln eines Textes. Zuerst werden die
     * Walzen, das Steckbrett und der Reflektor konfiguriert. Danach wird die
     * verschluesseln Methode beim Plugboard aufgerufen.
     *
     * @param msg der String der verschluesselt werden soll
     * @param rotorConfiguration ein String-Array mit der Rotor Konfiguration
     * @return ein String mit der verschluesselten Nachricht
     */
    public String encrypt(String msg, String[] rotorConfiguration) {
        String name = "";
        char startPosition;
        try {
            availableMachines.get(usedMachine).setMapper(rotorConfiguration[0].split(":")[0], 0);
            availableMachines.get(usedMachine).setPlugboardConnections(rotorConfiguration[0].split(":")[1]);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i < rotorConfiguration.length; i++) {

            String[] split = rotorConfiguration[i].split(":");
            name = split[0];
            startPosition = split[1].charAt(0);

            try {
                availableMachines.get(usedMachine).setMapper(name, i);
                availableMachines.get(usedMachine).setStartPosition(i, startPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String encrText = availableMachines.get(usedMachine).encrypt(msg);
        sendRotorPositionsToListeners(availableMachines.get(usedMachine).getCurrentRotorPositions());
        return encrText;
    }

    /**
     * Diese Methode sendet eine Nachricht an alle angemeldeten LogicListeners
     *
     * @param message die Nachricht
     */
    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            listeners.stream().forEach(new ConsumerImpl(message));
        }
    }

    /**
     * sendet eine neue Rotorstellung an alle angemeldeten LogicListener
     *
     * @param c ein char-Array mit der Rotorstellung
     */
    public void sendRotorPositionsToListeners(char[] c) {
        listeners.stream().forEach((LogicListener l) -> {
            l.updateRotorSettings(c);
        });
    }

    /**
     * Die Methode fuegt einen Listener der Liste hinzu
     *
     * @param l der LogicListener
     * @return false wenn der Listener nicht hinzugefuegt werden konnte
     */
    public boolean addLogicListener(LogicListener l) {
        return listeners.add(l);
    }

    /**
     * Hier wird ein Listener aus der Liste geloescht, also abgemeldet.
     *
     * @param l der LogicListener
     * @return false wenn der Listener nicht entfernt werden konnte
     */
    public boolean removeLogicListener(LogicListener l) {
        return listeners.remove(l);
    }

    /**
     * Gibt die aktuelle Konfiguration eines Mappers zurueck. Uebernimmt den
     * Namen des Mappers und waehlt mit diesem aus eienr Liste aus.
     *
     * @param mapperName
     * @return einen String mit der Konfiguration des Mappers
     */
    public String getMapperConfig(String mapperName) {
        return allMappersConfig.get(mapperName);
    }

    /**
     * Diese Methode bereitet den Text fuer das senden vor. Dabei wird in einer
     * HashMap nachgesehen, ob der Buchstabe zulaessig ist, falls nicht wird
     * dieser mit seinem Value aus der Liste ersetzt. Sollte es sich um ein
     * nicht zulaessiges Zeichen (ausserhalb von A-Z) handeln wird es geloescht.
     *
     * @param msg der zu substituierende String
     * @return ein String in dem alle ungueltigen Zeichen geloescht wurden
     */
    public String substitute(String msg) {
        boolean substituted = false; //wird auf true gesetzt falls etwas am Text veraendert wurd
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
     * Fuegt eine Maschine zu der Liste der vorhanden Maschinen hinzu.
     *
     * @param machine ein String mit dem Namen der Maschine
     * @param availableMappers ein 2D-String-Array mit den moeglichen Mappern
     * fuer die Positionen
     * @param configuration eine default Konfiguration fuer die Mapper
     */
    public void createMachine(String machine, String[][] availableMappers, String[] configuration) {
        try {
            availableMachines.put(machine, new Machine(this, machine, availableMappers, configuration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht eine Maschine aus der Liste der vorhandenen Maschinen
     *
     * @param machine der Name der Maschine
     */
    public void removeMachineData(String machine) {
        availableMachines.remove(machine);
    }

    /**
     * Gibt ein String-Array mit den moeglichen Mappern fuer diese Position mit
     *
     * @param position die Position
     * @return ein String-Array mit den moeglichen Mappern
     */
    public String[] getAvailableMappersOnPosition(int position) {
        return availableMachines.get(usedMachine).getAvailableMappersOnPosition(position);
    }

    /**
     * Fuegt die Enigma 1 und die Enigma M4 zu den vorhanden Maschinen hinzu.
     */
    private void addInitialMachines() {
        String nameEnigma1 = "Enigma I";
        String[][] availableMappersEnigma1 = {{"Pb"}, {"I", "II", "III", "IV","V"},
        {"I", "II", "III", "IV","V"}, {"I", "II", "III", "IV","V"}, {"A","B","C"}}; // Welche Walzen duerfen eingesetzt werden?
        String[] configurationEnigma1 = {"Pb", "I", "II", "III", "A"}; // Eingesetzte Walzen
        createMachine(nameEnigma1, availableMappersEnigma1,
                configurationEnigma1);

        String nameEnigmaM3 = "Enigma M3";
        String[][] availableMappersEnigmaM3 = {{"Pb"},
                {"I", "II", "III", "IV", "V"},
                {"I", "II", "III", "IV", "V"},
                {"I", "II", "III", "IV", "V"},
                {"B", "C"}};
        String[] configurationEnigmaM3 = {"Pb", "I", "II", "III", "B"};
        createMachine(nameEnigmaM3, availableMappersEnigmaM3,
                configurationEnigmaM3);

        String nameEnigmaM3K = "Enigma M3 Navy";
        String[][] availableMappersEnigmaM3K = {{"Pb"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"B", "C"}};
        String[] configurationEnigmaM3K = {"Pb", "I", "II", "III", "B"};

        createMachine(nameEnigmaM3K, availableMappersEnigmaM3K,
                configurationEnigmaM3K);

        String nameEnigmaM4 = "Enigma M4";
        String[][] availableMappersEnigmaM4 = {{"Pb"},
        {"I", "II", "III", "IV", "V"},
        {"I", "II", "III", "IV", "V"},
        {"I", "II", "III", "IV", "V"},
        {"β", "γ"}, {"B thin", "C thin"}};
        String[] configurationEnigmaM4 = {"Pb", "I", "II", "III", "β", "B thin"};
        createMachine(nameEnigmaM4, availableMappersEnigmaM4,
                configurationEnigmaM4);

        String nameEnigmaM4K = "Enigma M4 Navy";
        String[][] availableMappersEnigmaM4K = {{"Pb"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"},
                {"β", "γ"}, {"B thin", "C thin"}};
        String[] configurationEnigmaM4K = {"Pb", "I", "II", "III", "β", "C thin"};
        createMachine(nameEnigmaM4K, availableMappersEnigmaM4K,
                configurationEnigmaM4K);
    }

    private static class ConsumerImpl implements Consumer<LogicListener> {

        private final String message;

        public ConsumerImpl(String message) {
            this.message = message;
        }

        @Override
        public void accept(LogicListener l) {
            l.sendText(message);
        }
    }

}

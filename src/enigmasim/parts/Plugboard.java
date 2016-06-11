package enigmasim.parts;

import enigmasim.Debug;

/**
 * @author Gerald Schreiber, Philip Woelfel, Sebastian Chlan <br />
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
public class Plugboard extends Mapper {

    /**
     * Default-Konstruktor, welcher ein Steckbrett erzeugt, bei welchem jedes
     * Zeichen auf sich selbst abgebildet wird.
     *
     */
    public Plugboard() {
        super("Plugboard");
    }

    /**
     * Konstruktor, welcher ein Steckbrett mit der angegebenen Verdrahtung
     * erstellt.
     *
     * @param name name
     * @param setting String, der die Verdrahtung angibt
     */
    public Plugboard(String name, String setting) {
        super(name, setting);
    }

    /**
     * Konstruktor, welcher ein Steckbrett mit der angegebenen Verdrahtung
     * erstellt.
     *
     * @param name name
     * @param setting char[], welches die Verdrahtung angibt
     */
    public Plugboard(String name, char[] setting) {
        super(name, setting);
    }

    /**
     * Setzt alle Verbindungen neu.
     *
     * @param setting String, mit den Zielbuchstaben
     * @throws Exception falls der uebergebene String fehlerhaft ist
     */
    public void setConnections(String setting) throws Exception {
        stringToSettingArray(setting);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see enigma.parts.Mapper#encrypt(char)
     */
    @Override
    public char encrypt(char c) {
        Rotor temp = (Rotor) nextMapper;
        temp.rotate();
        int pos = c - 'A';
        if (pos >= 0 && pos <= 25) {
            if (Debug.isDebug()) {
                System.out.println(getName() + ":\nc: '" + c + "'\t out: '"
                        + setting[pos] + "'");
            }
            return temp.encrypt(setting[pos]);
        } else {
            throw new IllegalArgumentException("Not a valid character!");
        }
    }
}

package enigmasim.parts;

import enigmasim.Debug;

/**
 * @author Gerald Schreiber, Philip Woelfel, Sebastian Chlan 
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
public class Plugboard extends Mapper {

    /**
     * Default constructor which generates a breadboard, in which each character
     * is mapped to itself.
     *
     */
    public Plugboard() {
        super("Plugboard");
    }

    /**
     * Constructor, which creates a breadboard with the specified wiring.
     *
     * @param name name
     * @param setting wiring
     */
    public Plugboard(String name, String setting) {
        super(name, setting);
    }

    /**
     * Constructor creates a breadboard with the specified wiring
     *
     * @param name name
     * @param setting wiring
     */
    public Plugboard(String name, char[] setting) {
        super(name, setting);
    }

    /**
     * sets all connections
     *
     * @param setting String, with the target letter
     * @throws Exception the passed string is faulty
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

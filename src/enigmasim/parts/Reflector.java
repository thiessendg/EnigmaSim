package enigmasim.parts;

import java.util.TreeSet;

import enigmasim.Debug;

/**
 * @author Sebastian Chlan, Philip Woelfel <br />
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
public class Reflector extends Mapper {

    /**
     * Default Konstruktor Standardmaessig wird der Reflektor der Enigma 1,
     * Refelktor A
     */
    public Reflector() {
        try {
            this.name = "Reflector";
            stringToSettingArray("EJMZALYXVBWFCRQUONTSPIKHGD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * erstellt einen Reflektor mit den Einstellungen welche aus dem
     * mitgegebenen String ausgelesen werden
     *
     * @param name
     * @param setting der String mit der internen Verdrahtung des Reflektors
     */
    public Reflector(String name, String setting) {
        super(name, setting);
    }

    /**
     * erstellt einen Reflektor mit den Einstellungen welche aus dem
     * mitgegebenen char Array ausgelesen werden
     *
     * @param name
     * @param setting das char Array mit der internen Vedrahtung des Reflektors
     */
    public Reflector(String name, char[] setting) {
        super(name, setting);
    }

    /**
     * Diese Methode liesst aus einem String die Verkabelung aus und speicher
     * sie in einem char Array. Der String wird auf ungueltige (A-Z) und
     * doppelete Zeichen geprueft. Weiters wird geprueft ob der String doppelte
     * Zeichen oder Verkabelungsfehler beinhaltet (Bsp.: A-->E, E-->A, B-->Z,
     * Z-->B)
     *
     * @param str the setting string
     * @throws IllegalArgumentException falls das char Array ungueltig ist (
     * besteht nicht aus 26 Zeichen von A-Z, bzw. enthaelt doppelte Buchstaben
     * oder die Verkabelung ist ungueltig )
     */
    @Override
    void stringToSettingArray(String str) {
        TreeSet<Character> checkdouble = new TreeSet<>();
        for (char c : str.toCharArray()) {
            checkdouble.add(c);
        }
        if (str.length() != 26 || !str.matches("[A-Z]*")
                || checkdouble.size() != 26) {
            throw new IllegalArgumentException("String doesn't have the "
                    + "required length (26 characters A-Z) or contains illegal "
                    + "or duplicate characters");
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(str.charAt(i) - 'A') != (char) ('A' + i)) {
                throw new IllegalArgumentException("Wiring not correct.");
            }
        }
        str = str.toUpperCase();
        this.setting = str.toCharArray();
    }

    /**
     * Methode wird von der prev. Rotor aufgerufen --> Zeichen wird
     * verschluesselt --> die reverse Encrypt Methode des prev. Rotor wird
     * aufgerufen
     *
     * @param c Zeichen welches verschluesselt werden soll
     * @return
     */
    @Override
    public char encrypt(char c) {
        int pos = c - 'A';
        if (pos >= 0 && pos <= 25) {
            if (Debug.isDebug()) {
                System.out.println(getName() + ":\ninput: '" + c
                        + "'\noutput: '" + setting[pos] + "'");
            }
            return prevMapper.reverseEncrypt(setting[pos]);
        } else {
            throw new IllegalArgumentException("Not a valid character!");
        }
    }

}

package enigmasim.parts;

import java.util.TreeSet;

import enigmasim.Debug;

/**
 * @author Philip Woelfel, Sebastian Chlan <br />
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
public class Mapper {

    char[] setting = new char[26]; // Cabling
    Mapper nextMapper;
    Mapper prevMapper;
    String name; // name of the mapper

    /**
     * Default constructor creates a mapper with the Alphabet
     * "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
     */
    Mapper() {
        this("Mapper", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Create a mapper with the Alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZ" and the
     * name specified
     */
    Mapper(String nam) {
        this(nam, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Create a mapper to the specified destination Alphabet
     *
     * @param name The name of the mapper
     * @param setting The string with the aim Alphabet
     * @throws IllegalArgumentException if string is invalid (not consisting of
     * 26 characters A-Z, or contains duplicate letters)
     *
     */
    Mapper(String name, String setting) {
        this.name = name;
        stringToSettingArray(setting);
    }

    /**
     * Create a mapper to the specified destination Alphabet
     *
     * @param name The name of the mapper
     * @param setting The char array with the aim Alphabet
     * @throws IllegalArgumentException if string is invalid (not consisting of
     * 26 characters A-Z, or contains duplicate letters)
     */
    Mapper(String name, char[] setting) {
        this.name = name;
        String input = new String(setting);
        stringToSettingArray(input);
    }

    /**
     * get name of mapper
     *
     * @return the name of the Mapper
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the mapper
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Maps the letters entered in the target alphabet
     *
     * @param c To be encrypted characters Return the letter shown
     * @throws IllegalArgumentException If the handed over character is not
     * between A-Z
     */
    public char encrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Invalid character! Not A-Z.");
        }
        int pos = c - 'A';
        if (Debug.isDebug()) {
            System.out.println(getName() + ":\ninput: '" + c + "'\noutput: '"
                    + setting[pos] + "'");
        }

        return setting[pos];
    }

    /**
     * Makes the sign of the target alphabet in the source alphabet from
     *
     * @param c Characters to be encrypted
     * @return the exchange encoded characters
     * @throws IllegalArgumentException If the handed over character is not
     * between A-Z
     */
    char reverseEncrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Invalid character! Not A-Z.");
        }
        for (int i = 0; i < setting.length; i++) {
            if (setting[i] == c) {
                if (Debug.isDebug()) {
                    System.out.println(getName() + ":\ninput: '" + c
                            + "'\noutput: '" + (char) ('A' + i) + "'");
                }
                return (char) ('A' + i);
            }
        }
        return 0;
    }

    /**
     * Verifies a string (26 characters from A to Z, each occurs only once),
     * then stores it in the setting array
     *
     * @param str The string to be stored
     */
    void stringToSettingArray(String str) {
        TreeSet<Character> checkdouble = new TreeSet<>();
        for (char c : str.toCharArray()) {
            checkdouble.add(c);
        }
        if (str.length() != 26 || !str.matches("[A-Z]*")
                || checkdouble.size() != 26) {
            throw new IllegalArgumentException("String doesn't have the "
                    + "required length (26 characters A-Z) or contains illegal"
                    + "or duplicate characters");
        }
        str = str.toUpperCase();
        this.setting = str.toCharArray();
    }

    /**
     * Sets the next Mapper
     *
     * @param nextMapper sets the next Mapper
     */
    public void setNextMapper(Mapper nextMapper) {
        this.nextMapper = nextMapper;
    }

    /**
     * Is a Mapper after
     *
     * @return Whether a Mapper is inserted after
     */
    boolean hasNextMapper() {
        return nextMapper != null;
    }

    /**
     * Sets the mapper of the front is
     *
     * @param prevMapper sets the mapper of the front is
     */
    public void setPrevMapper(Mapper prevMapper) {
        this.prevMapper = prevMapper;
    }

    /**
     * Gives back if an Mapper is available before
     *
     * @return Whether Mapper previously inserted
     */
    boolean hasPrevMapper() {
        return prevMapper != null;
    }

    /**
     * (non-Javadoc)
     *
     * @return string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String new_line = System.getProperty("line.separator");
        StringBuilder str = new StringBuilder();
        str.append("Previous mapper: ").append(prevMapper.getName()).
                append(new_line);
        str.append("Actual Mapper (").append(getClass().getSimpleName()).
                append(", ").append(name).append(") setting: ");
        for (char aSetting : setting) {
            str.append(aSetting);
        }
        str.append(new_line).append("Next mapper: ").
                append(nextMapper.getName()).append(new_line);
        return str.toString();
    }

}

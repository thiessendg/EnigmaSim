package enigmasim.parts;

import enigmasim.Debug;

import java.util.Arrays;

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
 */
public class Rotor extends Mapper {

    private int position = 0;
    private char[] jumpChars = new char[2];
    private boolean isStatic = false;
    private String ringStellung;

    /**
     * Default Konstruktor erstellt einen Rotor mit der Verdrahtung
     * "ABCDEFGHIJKLMNOPQRSTUVWXYZ" und einer Uebertragskerbe bei Z
     *
     * @param name
     * @param setting
     * @param chumpchars
     */
    public Rotor(String name, String setting, char[] chumpchars) {
        super("Rotor");
        jumpChars[0] = 0;
        jumpChars[1] = 0;
        ringStellung = "1";
    }

    /**
     * Created a rotor with the specified wiring
     *
     * @param setting The string to the wirings
     * @param ringSetting
     * @param jump A char array with the / the mark in the next should continue
     * to turn roll may contain 1 or 2 elements
     * @throws Exception If the string is invalid (not consisting of 26
     * characters A-Z, or contains duplicate letters)
     */
    /**
     * Created a rotor with the specified wiring
     *
     * @param setting The char array to the wirings
     * @param ringSetting
     * @param jump A char array with the / the mark in the next should continue
     * to turn roll may contain 1 or 2 elements
     * @throws Exception If the string is invalid (not consisting of 26
     * characters A-Z, or contains duplicate letters)
     */
    public Rotor(String name, String setting, String ringSetting, char[] jump) throws Exception {
        super(name, setting);
        //if (checkRingstellung(ringSetting)) {
        ringStellung = ringSetting;
        //} else {
        //    throw new Exception("Invalid character for ring offset, ringStellung");
        //}

        if (checkJumpChars(jump)) {
            jumpChars = jump;
        } else {
            throw new Exception(
                    "Invalid carry-character ( must be an char-Array with one or two elements )!");
        }
    }

    /**
     * @param ch check the array if there is only one or two characters from A-Z
     * contains
     * @return False if the conditions are not met
     */
    private boolean checkJumpChars(char[] ch) {
        if (ch.length < 1) {
            return false;
        }
        if (ch[0] == ' ') {
            setStatic(true);
            return true;
        }
        return (ch[0] >= 'A' && ch[0] <= 'Z') && !(ch.length == 2 && (!(ch[1] >= 'A' && ch[1] <= 'Z') || ch[0] == ch[1])) && ch.length <= 2;
    }

    /*
    private boolean checkRingstellung(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }
     */
    /**
     * (non-Javadoc)
     *
     * @see enigmasim.parts.Mapper#encrypt(char)
     */
    public char encrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Not a valid character! Must be A-Z.");
        }
        // example character E coming in is in slot 5, or element 4 of the alphabet
        //CV will be the shifted value due to the ring offset
        //position is the count of the char entered?
        //E 69 = E 69- A 64 = 5th letter, 4th element
        char cV = (char) (c + position - (Integer.parseInt(ringStellung) - 1));
        //the character entered by the rotation
        //the roll offset -> the need
        //be encrypted
        if (cV < 'A') { // if you pass A
            cV += 26; // start at Z again ;-)
        } else if (cV > 'Z') {
            cV -= 26;
        }

        //dgt: i think pos is the position of the character input???
        int pos = cV - 'A'; // Position of the letter to be encrypted in
        // Alphabet - 1 -> index in the array with the
        // cabling
        //dgt: does ver need to be offset by the ring setting?
        //ver is just the real position E is position 5, [4]
        //ver is setting[real+rinStellung-1];
        char ver = setting[pos]; // The exchange encoded characters

        //the output char; note thee offset - here is assume B
        char out = (char) (ver - position + (Integer.parseInt(ringStellung) - 1)); //Convert characters again as
        //as would be the roll to position
        //A -> other roller can again
        //to anticipate shifting
        if (out < 'A') { // if you pass A
            out += 26; // start at Z again ;-)
        } else if (out > 'Z') {
            out -= 26;
        }

        if (Debug.isDebug()) {
            System.out
                    .println(getName() + ":\nc: '" + c + "'\t maps to cV: '" + cV + "'"
                            + "'\t shifts by ring setting? ver: '" + ver
                            + "'" + "'\t out: '" + out + "'");
        }

        if (hasNextMapper()) { // wenn ich wen nach mir hab
            return nextMapper.encrypt(out); // gib ich das Zeichen weiter zum
            // verschluesseln
        }
        return out; // wenn nicht gib ich meins zurueck
    }

    /**
     * Forms the entered letters of the alphabet to the target Source Alphabet
     * from
     *
     * @param c The input characters
     * @return the letter shown
     * @throws IllegalArgumentException If the handed over character is not
     * between A-Z
     */
    public char reverseEncrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException(
                    "Not a valid character! Must be A-Z.");
        }

        char cV = (char) (c + position - (Integer.parseInt(ringStellung) - 1)); // das eingebene Zeichen um die Drehung
        // der Walze versetzt --> das muss
        // verschluesselt werden
        if (cV < 'A') {
            cV += 26;
        } else if (cV > 'Z') { // wenn man am Z vorbeikommt
            cV -= 26; // muss man wieder beim A anfangen ;-)
        }

        char ver = 0; // da wird der verschluesselte Buchstabe gespeichert

        for (int i = 0; i < setting.length; i++) { // alle Verkabelungen
            // durchgehen
            if (setting[i] == cV) { // wenn der zu verschluesselnde Buchstabe
                // gefunden ist
                ver = (char) ('A' + i); // wird der dazugehoerige Buchstabe aus
                // dem Alphabet gespeichert
            }
        }

        char out = (char) (ver - position + (Integer.parseInt(ringStellung) - 1)); // Zeichen wieder so umrechnen wie
        // als waere die Walze auf Stellung
        // A --> andere Walze kann wieder
        // Verschiebung dazurechnen
        if (out < 'A') { // wenn man am A vorbeikommt
            out += 26; // muss man wieder beim Z anfangen ;-)
        } else if (out > 'Z') {
            out -= 26;
        }

        // Debugmessages ausgeben
        if (Debug.isDebug()) {
            System.out
                    .println(getName() + ":\nc: '" + c + "'\t cV: '" + cV + "'"
                            + "'\t ver: '" + ver + "'" + "'\t out: '" + out
                            + "'");
        }

        if (hasPrevMapper()) { // wenn ich wen vor mir hab
            return prevMapper.reverseEncrypt(out); // gib ich das Zeichen weiter
            // zum verschluesseln
        }
        return out; // wenn nicht gib ich meins zurueck
    }

    /**
     * dreht den Rotor und alle daran haengenden wenn er eine Uebertragskerbe
     * erreicht
     */
    void rotate() {
        //print debug information if debug enabled
        if (Debug.isDebug()) {
            System.out.println("Rotor " + getName() + " rotating from pos " + getCharPosition());
        }
        if (Debug.isDebug()) {
            System.out.println(getName() + " rotates from: " + position + "("
                    + getCharPosition() + ")");
        }

        char prevCharPosition = getCharPosition();

        //we want this here
        position++;
        if (position > 25) {
            position -= 26;
        }

        if (Debug.isDebug()) {
            System.out.println(getName() + " rotates to: " + position + "("
                    + (char) ('A' + position) + ")");
        }

        //if we have a rotor after current rotor
        if (hasNextMapper()) {
            if (nextMapper instanceof Rotor) {
                //create a rotor to left of current rotor
                Rotor nextRotor = (Rotor) nextMapper;

                //rotate next rotor??
                //nextRotor.position++;
                //if (nextRotor.position > 25) {
                //    nextRotor.position -= 26;
                //}
                if (!nextRotor.isStatic) {
                    //print some debug info
                    if (Debug.isDebug()) {
                        System.out.println("Current rotor: " + getName() + "\tNext rotor: " + nextRotor.getName());
                    }

                    if (jumpChars[0] == prevCharPosition && nextRotor.jumpChars[0] == nextRotor.getCharPosition()) {
                        nextRotor.rotate();
                    }
                    if (jumpChars[0] == prevCharPosition || nextRotor.jumpChars[0] == nextRotor.getCharPosition()) {
                        nextRotor.rotate();
                    }
                    if (nextRotor.jumpChars[0] == nextRotor.getCharPosition()) {
                        nextRotor.rotate();
                    }
                }

            }
        }

        //update rotate my rotor letter/position
        //position++;
        if (Debug.isDebug()) {
            System.out.println(getName() + " rotated to: " + getCharPosition());
        }
    }
    //}

    /**
     * getter fuer Position als char
     *
     * @return position auf der der Rotor steht als char ( A-Z )
     */
    public char getCharPosition() {
        return (char) ('A' + position);
    }

    public char getPrevCharPosition() {
        return (char) ('A' + position - 1);
    }

    public char getNextCharPosition() {
        return (char) ('A' + position + 1);
    }

    /**
     * setter fuer die Position als char ( A-Z )
     *
     * @param position char mit der neuen Position
     */
    public void setPosition(char position) {
        this.position = position - 'A';
    }

    /**
     * macht den Rotor statisch
     *
     * @param isStatic der neue Wert fuer isStatic
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * gibt zurueck ob der Rotor statisch ist
     *
     * @return der Wert von isStatic
     */
    public boolean isStatic() {
        return isStatic;
    }

}

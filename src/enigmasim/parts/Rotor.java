package enigmasim.parts;

import enigmasim.Debug;

/**
 * @author David Thiessen thiessendg@gmail.com based on work by: Philip Woelfel,
 * Sebastian Chlan <br />
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
     * Create a rotor with the specified wiring
     *
     * @param name
     * @param setting The char array to the wirings
     * @param jump A char array with the containing the notch positions to roll
     * over next rotor
     * @throws Exception If the string is invalid (not consisting of 26
     * characters A-Z, or contains duplicate letters)
     */
    public Rotor(String name, String setting, char[] jump) throws Exception {
        super(name, setting);
        if (checkJumpChars(jump)) {
            jumpChars = jump;
        } else {
            throw new Exception("Invalid carry-character (must be an char "
                    + "array with one or two elements)!");
        }
    }

    /**
     * @param ch check the array if there is only one or two characters from A-Z
     * @return boolean false if the conditions are not met
     */
    private boolean checkJumpChars(char[] ch) {
        if (ch.length < 1) {
            return false;
        }
        if (ch[0] == ' ') {
            //dgt: if there is a blank char, this is a static rotor
            setStatic(true);
            return true;
        }
        return (ch[0] >= 'A' && ch[0] <= 'Z')
                && !(ch.length == 2 && (!(ch[1] >= 'A' && ch[1] <= 'Z')
                || ch[0] == ch[1])) && ch.length <= 2;
    }

    /**
     * (non-Javadoc)
     *
     * @return
     * @see enigmasim.parts.Mapper#encrypt(char)
     */
    @Override
    public char encrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Invalid character! Not A-Z.");
        }

        char cV = (char) (c + position - (Integer.parseInt(ringStellung)-1));

        if (cV < 'A') {
            cV += 26;
        } else if (cV > 'Z') {
            cV -= 26;
        }

        // Position of the letter to be encrypted in Alphabet - 1 -> index 
        // in the cabling array
        int pos = cV - 'A';

        char ver = setting[pos]; // The exchange encoded characters

        char out = (char) (ver - position + (Integer.parseInt(ringStellung)-1));
        //Convert characters again as would be the roll to position A -> other 
        //roller can again to anticipate shifting
        if (out < 'A') {
            out += 26;
        } else if (out > 'Z') {
            out -= 26;
        }

        if (Debug.isDebug()) {
            System.out.println(getName() + ":\nc: '" + c + "'\tcV: '" + cV + "'"
                    + "'\tver: '" + ver + "'" + "'\tout: '" + out + "'");
        }

        if (hasNextMapper()) {
            return nextMapper.encrypt(out);
        }
        return out;
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
    @Override
    public char reverseEncrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Invalid character! Not A-Z.");
        }

        char cV = (char) (c + position - (Integer.parseInt(ringStellung)-1));

        if (cV < 'A') {
            cV += 26;
        } else if (cV > 'Z') {
            cV -= 26;
        }

        char ver = 0; // since the exchange encoded letter is stored

        for (int i = 0; i < setting.length; i++) { // all cabling go through
            if (setting[i] == cV) { // when to be encrypted letter is found
                ver = (char) ('A' + i); // the letter is saved from the alphabet
            }
        }

        char out = (char) (ver - position + (Integer.parseInt(ringStellung)-1));
        //Mark again convert like as would be the roll to position A -> other 
        //roll can be expected to shift again
        if (out < 'A') {
            out += 26;
        } else if (out > 'Z') {
            out -= 26;
        }

        // debug messages
        if (Debug.isDebug()) {
            System.out.println(getName() + ":\nc: '" + c + "'\tcV: '" + cV
                    + "'" + "'\tver: '" + ver + "'" + "'\tout: '" + out + "'");
        }

        if (hasPrevMapper()) {
            return prevMapper.reverseEncrypt(out);
        }
        return out;
    }

    /**
     * Rotate rotor and the next rotor when turnover notch is hit
     */
    void rotate() {
        if (Debug.isDebug()) {
            System.out.println(getName() + " rotating from pos "
                    + getCharPosition());
        }
        if (Debug.isDebug()) {
            System.out.print(getName() + " rotates from: " + position + "("
                    + getCharPosition() + ")");
        }

        char prevCharPosition = getCharPosition();

        position++;
        if (position > 25) {
            position -= 26;
        }

        if (Debug.isDebug()) {
            System.out.println(" to: " + position + "("
                    + (char) ('A' + position) + ")");
        }

        //if we have a mapper after current rotor
        if (hasNextMapper()) {
            //if that mapper is a rotor
            if (nextMapper instanceof Rotor) {
                //create a rotor to left of current rotor
                Rotor nextRotor = (Rotor) nextMapper;
                //by default this next rotor is not the last, final rotor
                boolean islastRotor = false;

                //make sure it's a moveable rotor?
                if (!nextRotor.isStatic) {
                    //try placing restriction here for last wheel
                    //if next rotor has a reflector after it, it's going to be
                    //the final rotor before hitting reflector
                    if ((nextRotor.nextMapper instanceof Reflector)) {
                        //since i am last rotor
                        islastRotor = true;
                    }

                    //if current rotor hits notch, rotate the next rotor
                    if (contains(prevCharPosition, jumpChars)) {
                        if (Debug.isDebug()) {
                            System.out.println("Rotor: " + getName()
                                    + " hit notch. Rotating next rotor: "
                                    + nextRotor.getName());
                        }
                        nextRotor.rotate();
                    }

                    //trying to handle double step sequence
                    //if current is not last rotor
                    if (!islastRotor) {
                        if (contains(getCharPosition(), jumpChars)
                                && contains(nextRotor.getCharPosition(),
                                        nextRotor.jumpChars)) {
                            if (Debug.isDebug()) {
                                System.out.println("Rotor: " + getName()
                                        + " hit notch. Rotating next rotor: "
                                        + nextRotor.getName());
                            }
                            nextRotor.rotate();
                        }

                        //next
                        if (contains(nextRotor.getCharPosition(),
                                nextRotor.jumpChars)) {
                            if (Debug.isDebug()) {
                                System.out.println("Rotor: " + getName()
                                        + " hit notch. Rotating next rotor: "
                                        + nextRotor.getName());
                            }
                            nextRotor.rotate();
                        }
                    }
                }
            }
        }

        if (Debug.isDebug()) {
            System.out.println(getName() + " rotated to: " + getCharPosition());
        }
    }

    /**
     * getter for position as char
     *
     * @return position on rotor as char ( A-Z )
     */
    public char getCharPosition() {
        return (char) ('A' + position);
    }

    /**
     * setter for the position as char (A-Z)
     *
     * @param position char with the new position
     */
    public void setPosition(char position) {
        this.position = position - 'A';
    }

    /*
     * setter for the ringstellung, the ring offset
     */
    public void setRingOffset(String offset) {
        this.ringStellung = offset;
    }

    /**
     * macht den Rotor statisch
     *
     * @param isStatic the new value for isStatic
     */
    private void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * returns whether the rotor is static
     *
     * @return der Wert von isStatic
     */
    public boolean isStatic() {
        return isStatic;
    }

    //dgt
    private boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

}

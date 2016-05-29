package enigmasim.parts;

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
public class Rotor extends Mapper {
	private int position = 0;
	private char[] jumpChars = new char[2];
	private boolean isStatic = false;

	/**
	 * Default Konstruktor erstellt einen Rotor mit der Verdrahtung
	 * "ABCDEFGHIJKLMNOPQRSTUVWXYZ" und einer Uebertragskerbe bei Z
	 */
	public Rotor() {
		super("Rotor");
		jumpChars[0] = 'Z';
		jumpChars[1] = 0;
	}

	/**
	 * erstellt einen Rotor mit der angegebenen Verdrahtung
	 * 
	 * @param setting
	 *            der String mit den Verdrahtungen
	 * @param jump
	 *            ein char-Array mit dem/den Zeichen bei der sich die naechste
	 *            Walze weiterdrehen soll kann 1 oder 2 Elemente enthalten
	 * 
	 * @throws Exception
	 *             falls der String ungueltig ist ( besteht nicht aus 26 Zeichen
	 *             von A-Z, bzw. enthaelt doppelte Buchstaben )
	 */
	public Rotor(String name, String setting, char[] jump) throws Exception {
		super(name, setting);
		if (checkJumpChars(jump)) {
			jumpChars = jump;
		} else {
			throw new Exception(
					"Invalid carry-character ( must be an char-Array with one or two Elements )!");
		}
	}

	/**
	 * erstellt einen Rotor mit der angegebenen Verdrahtung
	 * 
	 * @param setting
	 *            das char-Array mit den Verdrahtungen
	 * @param jump
	 *            ein char-Array mit dem/den Zeichen bei der sich die naechste
	 *            Walze weiterdrehen soll kann 1 oder 2 Elemente enthalten
	 * 
	 * @throws Exception
	 *             falls der String ungueltig ist ( besteht nicht aus 26 Zeichen
	 *             von A-Z, bzw. enthaelt doppelte Buchstaben )
	 */
	public Rotor(String name, char[] setting, char[] jump) throws Exception {
		super(name, setting);
		if (checkJumpChars(jump)) {
			jumpChars = jump;
		} else {
			throw new Exception(
					"Invalid carry-character ( must be an char-Array with one or two elements )!");
		}
	}

	/**
	 * @param ch
	 *            ueberprueft das Array ob es nur ein oder zwei Zeichen von A-Z
	 *            enthaelt
	 * @return false wenn die Bedingungen nicht erfuellt
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
	 * (non-Javadoc)
	 * 
	 * @see enigma.parts.Mapper#encrypt(char)
	 */
	public char encrypt(char c) {
		if (!(c >= 'A' && c <= 'Z')) {
			throw new IllegalArgumentException(
					"Not a valid character! Must be A-Z.");
		}
		char cV = (char) (c + position); // das eingebene Zeichen um die Drehung
											// der Walze versetzt --> das muss
											// verschluesselt werden
		if (cV > 'Z') { // wenn man am Z vorbeikommt
			cV -= 26; // muss man wieder beim A anfangen ;-)
		}
		int pos = cV - 'A'; // Position des zu verschluesselnden Buchstabens im
							// Alphabet - 1 --> index im Array mit den
							// Verkabelungen
		char ver = setting[pos]; // das verschluesselte Zeichen

		char out = (char) (ver - position); // Zeichen wieder so umrechnen wie
											// als waere die Walze auf Stellung
											// A --> andere Walze kann wieder
											// Verschiebung dazurechnen
		if (out < 'A') { // wenn man am A vorbeikommt
			out += 26; // muss man wieder beim Z anfangen ;-)
		}

		// Debugmessages ausgeben
		if (Debug.isDebug()) {
			System.out
					.println(getName() + ":\nc: '" + c + "'\t cV: '" + cV + "'"
							+ "'\t ver: '" + ver + "'" + "'\t out: '" + out
							+ "'");
		}

		if (hasNextMapper()) { // wenn ich wen nach mir hab
			return nextMapper.encrypt(out); // gib ich das Zeichen weiter zum
											// verschluesseln
		}
		return out; // wenn nicht gib ich meins zurueck
	}

	/**
	 * bildet den eingegebenen Buchstaben des Zielalphabetes auf das
	 * Quellalphabet ab
	 * 
	 * @param c
	 *            das eingegeben Zeichen
	 * @return der abgebildete Buchstabe
	 * @throws IllegalArgumentException
	 *             falls das uebergeben Zeichen nicht zwischen A-Z ist
	 */
	public char reverseEncrypt(char c) {
		if (!(c >= 'A' && c <= 'Z')) {
			throw new IllegalArgumentException(
					"Not a valid character! Must be A-Z.");
		}
		char cV = (char) (c + position); // das eingebene Zeichen um die Drehung
											// der Walze versetzt --> das muss
											// verschluesselt werden
		if (cV > 'Z') { // wenn man am Z vorbeikommt
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

		char out = (char) (ver - position); // Zeichen wieder so umrechnen wie
											// als waere die Walze auf Stellung
											// A --> andere Walze kann wieder
											// Verschiebung dazurechnen
		if (out < 'A') { // wenn man am A vorbeikommt
			out += 26; // muss man wieder beim Z anfangen ;-)
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
		if (Debug.isDebug()) {
			System.out.println(getName() + " rotates from: " + position + "("
					+ (char) ('A' + position) + ")");
		}
		position++;
		if (position > 25) {
			position -= 26;
		}
		if (Debug.isDebug()) {
			System.out.println(getName() + " rotates to: " + position + "("
					+ (char) ('A' + position) + ")");
		}

		if (hasNextMapper()) {
			if (nextMapper instanceof Rotor) {
				Rotor next = (Rotor) nextMapper;
				if (!next.isStatic()) {
					for (char jump : jumpChars) {
						if (jump == 'A' + position - 1) { // vorher wurde schon
															// rotiert --> wenn
															// zeichen an
															// position-1 =
															// uebertrags
															// Buchstabe nachbar
															// drehen
							Rotor temp = (Rotor) nextMapper;
							temp.rotate();
						}
					}// for
				}// if !static
			}// if instanceof
		}// if hastnext
	}

	/**
	 * getter fuer Position als char
	 * 
	 * @return position auf der der Rotor steht als char ( A-Z )
	 */
	public char getCharPosition() {
		return (char) ('A' + position);
	}

	/**
	 * setter fuer die Position als char ( A-Z )
	 * 
	 * @param position
	 *            char mit der neuen Position
	 */
	public void setPosition(char position) {
		this.position = position - 'A';
	}

	/**
	 * macht den Rotor statisch
	 * 
	 * @param isStatic
	 *            der neue Wert fuer isStatic
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

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
	char[] setting = new char[26]; // die Verkabelung
	Mapper nextMapper; // der Mapper der danach kommt
	Mapper prevMapper; // der Mapper der davor kommt
	protected String name; // der Name des Mappers

	/**
	 * Default Konstruktor erstellt einen Mapper mit dem Zielalphabet
	 * "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	 */
	Mapper() {
		this("Mapper", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * erstellt einen Mapper mit dem Zielalphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	 * und dem angegebenen Namen
	 */
	Mapper(String nam) {
		this(nam, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * erstellt einen Mapper mit dem angegebenen Zielalphabet
	 * 
	 * @param name
	 *            der Name des Mappers
	 * @param setting
	 *            der String mit dem Zielalphabet
	 * @throws IllegalArgumentException
	 *             falls der String ungueltig ist ( besteht nicht aus 26 Zeichen
	 *             von A-Z, bzw. enthaelt doppelte Buchstaben )
	 */
	Mapper(String name, String setting) {
		this.name = name;
		stringToSettingArray(setting);
	}

	/**
	 * erstellt einen Mapper mit dem angegebenen Zielalphabet
	 * 
	 * @param name
	 *            der Name des Mappers
	 * @param setting
	 *            das char-Array mit dem Zielalphabet
	 * @throws IllegalArgumentException
	 *             falls der String ungueltig ist ( besteht nicht aus 26 Zeichen
	 *             von A-Z, bzw. enthaelt doppelte Buchstaben )
	 */
	Mapper(String name, char[] setting) {
		this.name = name;
		String eingabe = new String(setting);
		stringToSettingArray(eingabe);
	}

	/**
	 * getter fuer den Namen
	 * 
	 * @return der Name des Mappers
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter fuer den Namen
	 * 
	 * @param name
	 *            der neue Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * bildet den eingegebenen Buchstaben auf das Zielalphabet ab
	 * 
	 * @param c
	 *            das zu verschluesselnde Zeichen
	 * @return der abgebildete Buchstabe
	 * @throws IllegalArgumentException
	 *             falls das uebergeben Zeichen nicht zwischen A-Z ist
	 */
	public char encrypt(char c) {
		if (!(c >= 'A' && c <= 'Z')) {
			throw new IllegalArgumentException(
					"Invalid character! Must be A-Z.");
		}
		int pos = c - 'A';
		if (Debug.isDebug()) {
			System.out.println(getName() + ":\ninput: '" + c + "'\noutput: '"
					+ setting[pos] + "'");
		}

		return setting[pos];
	}

	/**
	 * bildet das Zeichen aus dem Zielalphabet im Quellalphabet ab
	 * 
	 * @param c
	 *            Zeichen das verschluesselt werden soll
	 * @return das verschluesselte Zeichen
	 * @throws IllegalArgumentException
	 *             falls das uebergeben Zeichen nicht zwischen A-Z ist
	 */
	public char reverseEncrypt(char c) {
		if (!(c >= 'A' && c <= 'Z')) {
			throw new IllegalArgumentException(
					"Invalid character! Must be A-Z.");
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
	 * ueberprueft einen String ( 26 Zeichen von A-Z, jedes kommt nur einmal vor
	 * ) und speichert ihn dann in dem setting-Array
	 * 
	 * @param str
	 *            der String der gespeichert werden soll
	 */
	protected void stringToSettingArray(String str) {
		TreeSet<Character> checkdouble = new TreeSet<>();
		for (char c : str.toCharArray()) {
			checkdouble.add(c);
		}
		if (str.length() != 26 || !str.matches("[A-Z]*")
				|| checkdouble.size() != 26) {
			throw new IllegalArgumentException(
					"String doesn't have the required length (26 characters A-Z) or contains illegal or duplicate characters");
		}
		str = str.toUpperCase();
		this.setting = str.toCharArray();
	}

	/**
	 * setzt den Mapper danach
	 * 
	 * @param nextMapper
	 *            setzt den naechsten Mapper
	 */
	public void setNextMapper(Mapper nextMapper) {
		this.nextMapper = nextMapper;
	}

	/**
	 * gibt zurueck ob ein Mapper danach eingefuegt ist
	 * 
	 * @return ob ein Mapper danach eingefuegt ist
	 */
	boolean hasNextMapper() {
		return nextMapper != null;
	}

	/**
	 * setzt den Mapper der davor ist
	 * 
	 * @param prevMapper
	 *            setzt den Mapper der davor ist
	 */
	public void setPrevMapper(Mapper prevMapper) {
		this.prevMapper = prevMapper;
	}

	/**
	 * gibt zurueck ob ein Mapper davor vorhanden ist
	 * 
	 * @return ob ein Mapper davor eingefuegt ist
	 */
	boolean hasPrevMapper() {
		return prevMapper != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		final String new_line = System.getProperty("line.separator");
		StringBuilder str = new StringBuilder();
		str.append("Previous mapper: ").append(prevMapper.getName()).append(new_line);
		str.append("Actual Mapper (").append(getClass().getSimpleName()).append(", ").append(name).append(") setting: ");
		for (char aSetting : setting) {
			str.append(aSetting);
		}
		str.append(new_line).append("Next mapper: ").append(nextMapper.getName()).append(new_line);
		return str.toString();
	}

}
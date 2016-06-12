package enigmasim.gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;


/**
 * @author bre
 *
 */
class GUIText {
	/** Der Dateiname des Sprachen-Ressource-Files */
	private static final String FILE_NAME = "languages.csv";
	
	/** Die Namen der verfuegbaren Sprachen */
	private static String[] languages = {};
	
	/**
	 * Sprachumsetzungs-Map: 
	 *   key->value[]
	 *   die Sprach-Position im value-Array ist analog zu, languages-Array
	 */
	private static final HashMap<String, String[]> MAP = new HashMap<>();
	
	/** Die Nummer der aktuellen Sprache (=Position im languages-Array) */
	private static int languageNr = 0;
	
	/** Alle angemeldeten graphischen Elemente, bei denen der Text gesetzt werden soll */
	private static final HashMap<Object, String> COMPONENTS = new HashMap<>();
	
	
	/**
	 * Liefert alle verfuegbaren Sprachen als Array
	 * @return die verfuegbaren Sprachen
	 */
	static String[] getLanguages() {
		return languages;
	}
	
	
	/**
	 * Ermittelt die derzeitig aktuelle Sprache
	 * @return die Sprache
	 */
	static String getLanguage() {
		return languages[languageNr];
	}
	
	
	/**
	 * Waehlt eine neue Sprache aus.
	 * @param language die neue Sprache
	 */
	static void setLanguage(String language) {
		if (languages[languageNr].equalsIgnoreCase(language))
			return;
		for (int i=0; i<languages.length; i++) {
			if (languages[i].equalsIgnoreCase(language)) {
				languageNr = i;
				updateLanguage();
				return;
			}
		}
		throw new IllegalArgumentException("Langugage >" + language + "< not found.");
	}
	
	
	/**
	 * Liefert den Text zu einem Key
	 * @param key der Key fuer den gewuenschten Text
	 * @return der Text
	 */
	static String getText(String key) {
		String value[] = MAP.get(key.toLowerCase().trim());
		if (value==null)
			throw new IllegalArgumentException("Key >" + key + "< not found.");
		return value[languageNr];
	}
	
	
	/**
	 * Aendert fuer alle angemeldeten Componenten die Beschriftung.
	 */
	private static void updateLanguage() {
            // TODO Aendere fuer alle angemeldeten Componenten die Beschriftung.
            COMPONENTS.keySet().stream().forEach((c) -> {
                updateLanguage(c, COMPONENTS.get(c));
            });
	}
	
	
	/**
	 * Beschriftet die Componente neu.
	 * @param component die zu beschriftende Componente
	 * @param key der Key fuer die Beschriftung
	 */
	private static void updateLanguage(Object component, String key) {
		String text = getText(key);
		if (component instanceof AbstractButton) {
			((AbstractButton)component).setText(text);
		} else if (component instanceof JLabel) {
			((JLabel)component).setText(text);
		} else if (component instanceof TitledBorder){
			((TitledBorder)component).setTitle(text);
		} else if (component instanceof JFrame) {
			((JFrame) component).setTitle(text);
		} else if (component instanceof GUIString) {
			 ((GUIString) component).setString(text);
		}// TODO ergaenze zusaetzliche graphische Elemente
			
	}
	
	
	/**
	 * Beschriftet die Componente und fuegt sie zu den verwalteten Componenten hinzu.
	 * @param component die Componente
	 * @param key       der Key fuer die Beschriftung
	 * @return          die neu beschriftete Componente
	 */
	public static Object add(Object component, String key) {
		COMPONENTS.put(component, key);
		updateLanguage(component, key);
		return component;
	}
	
	/**
	 * Statischer Konstruktor
	 */
	static {
		loadLanguageRessourceFile(FILE_NAME);
	}

	
	/**
	 * Ladet das Sprachen-Ressourcen-File.
	 * @param fileName der Name des Sprachen-Ressourcen-Files
	 */
	private static void loadLanguageRessourceFile(String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream("resources/" + fileName), "UTF-8")
			);
		} catch (Exception e) {
			try {
				in = new BufferedReader(new InputStreamReader(
						Object.class.getResourceAsStream("/resources/"+fileName), "UTF-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
		}
		try {
			String line = in.readLine();
			languages = line.split(";",2)[1].split(";");
			while ((line=in.readLine()) != null) {
				String[] s      = line.split(";",2);
				String[] values = s[1].split(";");
				MAP.put(s[0].toLowerCase().trim(), values);
			}
			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Erzeugt eine String-Darstellung der Sprachressourcen (analog zu toString)
	 * @return die String-Darstellung
	 */
	@SuppressWarnings("unused")
	private static String getString() {
		StringBuilder s = new StringBuilder();
		s.append("key/languages: "); 
		for (int i=0; i<languages.length; i++) {
			if (i>0) s.append(";");
			s.append(languages[i]);
		}
		s.append("\n");
		
                MAP.keySet().stream().map((key) -> {
                    s.append(key);
                return key;
            }).map((key) -> {
                s.append(": ");
                String v[] = MAP.get(key);
                return v;
            }).map((v) -> {
                for (int i=0; i<v.length; i++) {
                    if (i>0) s.append(";");
                    s.append(v[i]);
                }
                return v;
            }).forEach((_item) -> {
                s.append("\n");
            });
		
		return s.toString();
	}
	
	/**
	 * Liefert die ersten zwei Buchstaben der Sprache in Kleinbuchstaben (z.B. 'de', 'en').
	 * @return - 
	 */
	static String getLanguageCode(){
		return getLanguage().substring(0, 2).toLowerCase();
	}
}

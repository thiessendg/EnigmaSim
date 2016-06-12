package enigmasim.gui;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.*;

import enigmasim.Debug;


/**
 * @author Gerald Schreiber 
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
@SuppressWarnings("serial")
public class Help extends JFrame {

	/**
	 * 
	 */
	public Help() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		String title = GUIText.getText("mabouthelp");
		setTitle(title);
		setSize(600, 500);
		setIconImage(readImageFromRessources("Icon_Enigma.png"));

		String text = getTextFromFile("helpText_" + GUIText.getLanguage() + ".txt");

		JEditorPane helpPane = new JEditorPane("text/html", text);
		JScrollPane scrollHelpPane = new JScrollPane(helpPane);

		helpPane.setEditable(false);
		helpPane.setCaretPosition(0);

		add(scrollHelpPane);

		setVisible(true);
	}

	/**
	 * @param args cl arguments
	 */
	public static void main(String[] args) {
		new Help();
	}

	/**
	 * liest ein Bild ein
	 * @param fname der Dateiname
	 * @return ein Image Objekt mit dem Bild
	 */
	private Image readImageFromRessources(String fname) {
		try {
			return ImageIO.read(new File("resources/" + fname));
		} catch (Exception e) {
			try {
				return ImageIO.read(getClass().getResource(
						"/resources/" + fname));
			} catch (Exception e1) {
				if(Debug.isDebug())
					System.out.println("Falscher Dateiname");
				return null;
			}
		}
	}

	/**
	 * liest den Text aus einer Datei ein
	 * @param file der Dateiname
	 * @return String mit dem Inhalt der Datei
	 */
	private String getTextFromFile(String file) {
		BufferedReader br = getBufferedReader(file);
		StringBuilder sb = new StringBuilder();
		try {
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line).append(System.getProperty("line.separator"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Gibt den passenden BufferedReader zurueck, egal ob in eine Jar-File oder nicht.
	 * @param fileName der Dateiname fuer den der BufferedReader gebraucht wird
	 * @return der BufferedReader fuer das File
	 */
	private BufferedReader getBufferedReader(String fileName) {
		BufferedReader in = null;
		try {
			return new BufferedReader(new InputStreamReader(
					new FileInputStream("resources/" + fileName), "UTF-8"));
		} catch (Exception e) {
			try {
				return new BufferedReader(new InputStreamReader(Object.class.getResourceAsStream(
								"/resources/" + fileName), "UTF-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
				System.exit(-1);
			} finally {
				closeStream(null);
			}
		}
		return in;
	}

	/**
	 * schließt ein Closeable-Object
	 * 
	 * @param closeable
	 *            das zu schliessende Objekt
	 */
	private void closeStream(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

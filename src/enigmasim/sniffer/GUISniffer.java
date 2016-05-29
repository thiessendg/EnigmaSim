package enigmasim.sniffer;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;

import enigmasim.Debug;
import enigmasim.NetworkListener;

/**
 * @author Sebastian Chlan <br />
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
@SuppressWarnings("serial")
class GUISniffer extends JFrame implements NetworkListener,
		ActionListener {
	private ArrayList<String> messages = new ArrayList<>();
	private JEditorPane msgarea = new JEditorPane();
	private int countmsg = 0;
	private static final int MSGBUFFERSIZE = 10;
	private static final int FRAMEWIDTH = 900;
	private static final int FRAMEHEIGHT = 600;
	private static final int MSGAREAWIDTH = 865;
	private static final int MSGAREAHEIGHT = 565;
	private static final int COUNT_LINEBREAK = 60;

	/**
	 * Default Konstruktor
	 * erzeugt die GUI
	 */
	GUISniffer() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconsAndTray("Logo24.png");
		
		JPanel areapanel = new JPanel();
		Container cp = this.getContentPane();

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Enigma Sniffer");
		this.setResizable(false);
		displayCenter(FRAMEWIDTH, FRAMEHEIGHT, this);

		msgarea.setContentType("text/html");
		msgarea.setEditable(false);
		msgarea.setPreferredSize(new Dimension(MSGAREAWIDTH, MSGAREAHEIGHT));
		msgarea.setAutoscrolls(false);
		msgarea.setToolTipText("Abgeh\u00F6rte Enigma Nachrichten");
		JScrollPane msgpane = new JScrollPane(msgarea);
		msgpane.setPreferredSize(new Dimension(MSGAREAWIDTH, MSGAREAHEIGHT));
		msgpane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		areapanel.add(msgpane);
		cp.add(areapanel);

		this.setVisible(true);
	}

	
	/**
	 * Diese Methode setzt den TrayIcon und das Icon in der Titelleiste.
	 * Weiters wird im Tray ein PopupMenu erstellt, welches die Möglichkeit bietet das Programm zu schliessen
	 * @param imgName Der Name des Bildes (muss sich im Folder resources befinden). 
	 */
	private void setIconsAndTray(String imgName) {
		Image image = readImageFromRessources(imgName);
		assert image != null;
		TrayIcon trayIcon = new TrayIcon(image);
		PopupMenu pop = new PopupMenu("Enigma Sniffer");
		pop.add("Exit");
		pop.addActionListener(this);
		trayIcon.setPopupMenu(pop);
		this.setIconImage(image);
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum zentrieren und setzen der Groesse des Sniffer Fensters auf
	 * dem Desktop
	 * 
	 * @param w
	 *            Breite des Fensters
	 * @param h
	 *            Höhe des Fensters
	 * @param f
	 *            Das JFrame selbst
	 */
	private static void displayCenter(int w, int h, JFrame f) {
		f.setSize(w, h);
		Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((int) (dem.getWidth() / 2 - w / 2), (int) (dem
				.getHeight() / 2)
				- h / 2);
	}

	/**
	 * Diese Methode schreibt die Nachrichten aus der messages ArrayList in die
	 * JEditorPane
	 */
	private void writeToArea() {
		String[] tmp;
		String txt = "<div style=\"background-image: url(/home/basti/Desktop/Logo.png)\">";
		for (String s : messages) {
			tmp = s.split("%");
			tmp[1] = insertLineBreaks(tmp[1]);
			txt += ("<span style=\"font-size:20pt;font-family:Arial;\"><b><u>Nachricht "
					+ tmp[0] + "</u></b><br />" + tmp[1] + "</span>");
			txt += "<br /><br />";
		}
		txt += "</div>";
		print(txt);
	}

	/**
	 * Diese Methode fuegt in einen String der in die JEditorPane eingefuegt
	 * werden soll LineBreaks ein da die JEditorPane keine automatischen Line
	 * Wrap hat. Die Position des Linebreaks wird in der Variable
	 * COUNT_LINEBREAK gespeichert.
	 * 
	 * @param s
	 *            Der String in den die Linebreaks eingefuegt werden.
	 * @return Der String mit den eingefuegten Linebreaks.
	 */
	private String insertLineBreaks(String s) {
		String ret = "";
		for (int i = 0; i < s.length(); i++) {
			if ((i % COUNT_LINEBREAK) == 0 && i != 0) {
				ret += s.charAt(i) + "<br />";
			} else {
				ret += s.charAt(i);
			}
		}
		return ret;
	}

	/**
	 * Diese Methode schreibt den Text in die JEditorPane.
	 * 
	 * @param txt
	 *            Der String der in die JEditorPane eingefuegt werden soll.
	 */
	private void print(String txt) {
		msgarea.setText(txt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see enigma.NetworkListener#sendRecievedMessage(java.lang.String)
	 */
	@Override
	public void sendRecievedMessage(String msg) {
		countmsg++;
		Date timestamp = new Date();
		messages.add(0, countmsg + " - " + timestamp.toString() + "%" + msg);
		if (messages.size() > MSGBUFFERSIZE) {
			messages.remove(MSGBUFFERSIZE);
		}
		writeToArea();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/**
	 * Methode liesst ein Bild aus dem resources Ordner aus und gibt es als Image Objekt zurueck
	 * @param fname Der Name des Bildes
	 * @return Das gefundene Bild, falls keines gefunden wurde wird null zurueckgegeben
	 */
	private Image readImageFromRessources(String fname) {
		try {
			return ImageIO.read(new File("resources/" + fname));
		} catch (Exception e) {
			try {
				return ImageIO.read(getClass().getResource("/resources/" + fname));
			} catch (Exception e1) {
				if(Debug.isDebug())
					System.out.println("Falscher Dateiname");
				return null;
			}
		}
	}
}

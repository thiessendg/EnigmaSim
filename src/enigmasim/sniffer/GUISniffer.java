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
 * @author Sebastian Chlan
 *
 * ENIGMA_TEC 2010 technik[at]enigma-ausstellung.at 
 * http://enigma-ausstellung.at
 *
 * HTL Rennweg Rennweg 89b A-1030 Wien
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
     * Default Constructor generates the GUI
     */
    GUISniffer() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setIconsAndTray("Icon_Enigma.png");

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
        msgpane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        areapanel.add(msgpane);
        cp.add(areapanel);

        this.setVisible(true);
    }

    /**
     * This method sets the tray icon and the icon in the title bar.
     * Furthermore, a PopupMenu is created in the system tray, which offers the
     * possibility to close the program
     *
     * @param imgName name of the image (must be in the folder resources).
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
     * method to center and put the size of the Sniffer window on the desktop
     *
     * @param w width of window
     * @param h height of window
     * @param f the JFrame itself
     */
    private static void displayCenter(int w, int h, JFrame f) {
        f.setSize(w, h);
        Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int) (dem.getWidth() / 2 - w / 2), (int) (dem
                .getHeight() / 2)
                - h / 2);
    }

    /**
     * This method writes the messages from the messages ArrayList in the
     * JEditorPane
     */
    private void writeToArea() {
        String[] tmp;
        String txt = "<div style=\"background-image: "
                + "url(/home/basti/Desktop/Logo.png)\">";
        for (String s : messages) {
            tmp = s.split("%");
            tmp[1] = insertLineBreaks(tmp[1]);
            txt += ("<span style=\"font-size:20pt;font-family:Arial;\">"
                    + "<b><u>Nachricht " + tmp[0] + "</u></b>" + tmp[1] + 
                    "</span>");
            txt += "";
        }
        txt += "</div>";
        print(txt);
    }

    /**
     * This method adds to a string to be listed on the JEditorPane linebreaks
     * one since the JEditorPane has no automatic Line wrap. The position of the
     * Line Breaks is stored in the variable COUNT_LINEBREAK.
     *
     * @param s The string in the Line Breaks are inserted.
     * @return The string with the eingefuegten linebreaks.
     */
    private String insertLineBreaks(String s) {
        String ret = "";
        for (int i = 0; i < s.length(); i++) {
            if ((i % COUNT_LINEBREAK) == 0 && i != 0) {
                ret += s.charAt(i) + "";
            } else {
                ret += s.charAt(i);
            }
        }
        return ret;
    }

    /**
     * This method writes the text in the JEditorPane.
     *
     * @param txt The string to be listed on the JEditorPane.
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
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
     * ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**
     * method reads an image from the resources folder and returns available as
     * image object
     *
     * @param fname name of the image
     * @return The image found. If none was found, return null
     */
    private Image readImageFromRessources(String fname) {
        try {
            return ImageIO.read(new File("resources/" + fname));
        } catch (Exception e) {
            try {
                return ImageIO.read(getClass().getResource(
                        "/resources/" + fname));
            } catch (Exception e1) {
                if (Debug.isDebug()) {
                    System.out.println("Falscher Dateiname");
                }
                return null;
            }
        }
    }
}

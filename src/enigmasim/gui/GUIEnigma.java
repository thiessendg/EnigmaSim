package enigmasim.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import enigmasim.Debug;
import enigmasim.Logic;
import enigmasim.LogicListener;
import enigmasim.Machine;
import enigmasim.NetworkListener;
import enigmasim.parts.Rotor;

/**
 * @author Daniel Boschofsky
 *
 * ENIGMA_TEC 2010 technik[at]enigma-ausstellung.at http://enigma-ausstellung.at
 *
 * HTL Rennweg Rennweg 89b A-1030 Wien
 *
 */
@SuppressWarnings("serial")
public class GUIEnigma extends JFrame implements ActionListener, LogicListener,
        NetworkListener {

    /**
     * Courier New, Plain, 14pt. Wird benötigt für das Steckbrett und die Walzen
     */
    static final Font monoFont = new Font("Courier New", Font.PLAIN, 14);
    /**
     * Ein String mit dem Alphabet
     */
    static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * Ein String mit allen Zeichen des Alphabets in QWERTZ-Form
     */
    static final String KEYBOARD = "QWERTZUIOPASDFGHJKLYXCVBNM";

    private Logic log = null;
    private String[] availMachines = null;
    private Machine currentMachine = null;

    private final JPanel pRotors = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private final ArrayList<GUIRotor> rotors = new ArrayList<>();
    private final ArrayList<String> config = new ArrayList<>();
    private final ArrayList<String> startConfig = new ArrayList<>();

    private final TitledBorder tbplugBoard = new TitledBorder("");
    private final GUIPlugboard plugBoard = new GUIPlugboard();

    private final JTextArea taInput = new JTextArea();
    private final TitledBorder tbInput = new TitledBorder("");
    private final JTextArea taOutput = new JTextArea();
    private final TitledBorder tbOutput = new TitledBorder("");

    private final JLabel lLogo = new JLabel(new ImageIcon(readImageFromRessources("Logo_Enigma_verysmall.png")));
    private final GUIButton bSet = new GUIButton();
    private final GUIButton bReset = new GUIButton();
    private final GUIButton bClear = new GUIButton();
    private final GUIButton bEncrypt = new GUIButton();
    private final GUIButton bSend = new GUIButton();

    private final TitledBorder tbmessages = new TitledBorder("");
    private final JComboBox<String> cMessages = new JComboBox<>();

    private final JMenuBar menubar = new JMenuBar();

    private final JMenu mSet = new JMenu();
    private final JMenu mSetMachine = new JMenu();
    private final JMenu mSetLang = new JMenu();

    private final ButtonGroup groupMachines = new ButtonGroup();
    private final ArrayList<JRadioButtonMenuItem> rbMachine = new ArrayList<>();

    private final ButtonGroup groupLang = new ButtonGroup();
    private final ArrayList<JRadioButtonMenuItem> rbLanguages = new ArrayList<>();

    private final JMenu mAbout = new JMenu();
    private final JMenuItem mAboutAbout = new JMenuItem();
    private final JMenuItem mAboutHelp = new JMenuItem();

    private final PopupMenu pop = new PopupMenu("Enigma");

    private JLabel lrefl = null;

    // Tooltip texte
    private final GUIString ttEncrypt = new GUIString("");
    private final GUIString ttSend = new GUIString("");
    private final GUIString ttMessages = new GUIString("");
    private final GUIString ttSet = new GUIString("");
    private final GUIString ttReset = new GUIString("");
    private final GUIString ttClear = new GUIString("");

    public GUIEnigma(Logic logic) {
        super("Enigma");
        initiateLangObjects();

        log = logic;
        availMachines = log.getAllMachineNames();
        currentMachine = log.getMachine(availMachines[0]);
        log.setMachine(currentMachine.getMachineName());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenubar();
        setJMenuBar(menubar);

        setLayout(new BorderLayout());
        setIconsAndTray("Icon_Enigma.png");

        JPanel boxlay = new JPanel();
        boxlay.setLayout(new BoxLayout(boxlay, BoxLayout.PAGE_AXIS));

        // Erste Zeile (Walzen, Buttons)
        JPanel firstLine = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createRotors();

        firstLine.add(pRotors);
        pRotors.setMinimumSize(new Dimension(400, 230));

        SplashScreen.updateProgressBar(50);

        JPanel corner;

        corner = placeButtonsAndLogo();
        corner.setPreferredSize(new Dimension(80, 230));
        firstLine.add(corner);
        boxlay.add(firstLine);

        // Zeile 2 (Dropdown Nachrichten)
        cMessages.setBorder(tbmessages);
        tbmessages.setTitle(GUIText.getText("tbmessages"));
        cMessages.setPreferredSize(new Dimension(450, 50));
        cMessages.setMaximumSize(cMessages.getPreferredSize());
        boxlay.add(cMessages);

        SplashScreen.updateProgressBar(60);

        // Zeile 3 (Input, Verschl√ºsselnbutton)
        JPanel thirdLine = new JPanel(new FlowLayout());
        JScrollPane spInput = new JScrollPane(taInput);
        spInput.setBorder(tbInput);
        tbInput.setTitle(GUIText.getText("tbinput"));
        spInput.setPreferredSize(new Dimension(300, 100));
        taInput.setLineWrap(true);
        thirdLine.add(spInput);

        JPanel pBEncrypt = new JPanel(new BorderLayout());
        pBEncrypt.setPreferredSize(new Dimension(130, 100));
        bEncrypt.setText(GUIText.getText("bencrypt"));
        pBEncrypt.add(bEncrypt, BorderLayout.SOUTH);
        thirdLine.add(pBEncrypt);
        boxlay.add(thirdLine);

        SplashScreen.updateProgressBar(70);

        // 4. Zeile (Output, Sendenbutton)
        JPanel fourthLine = new JPanel(new FlowLayout());
        JScrollPane spOutput = new JScrollPane(taOutput);
        spOutput.setBorder(tbOutput);
        tbOutput.setTitle(GUIText.getText("tboutput"));
        spOutput.setPreferredSize(new Dimension(300, 100));
        taOutput.setLineWrap(true);
        taOutput.setEditable(false);
        fourthLine.add(spOutput);

        JPanel pBSend = new JPanel(new BorderLayout());
        pBSend.setPreferredSize(new Dimension(130, 100));
        bSend.setText(GUIText.getText("bsend"));
        pBSend.add(bSend, BorderLayout.SOUTH);
        fourthLine.add(pBSend);
        boxlay.add(fourthLine);

        plugBoard.setPreferredSize(new Dimension(430, 160));
        plugBoard.setBorder(tbplugBoard);
        boxlay.add(plugBoard);

        add(boxlay);

        //	getExtendedState() == MAXIMIZED_BOTH;
        SplashScreen.updateProgressBar(80);

        updateTooltips();
        resize();
        registerActionListeners();

        setVisible(true);
    }

    /**
     * Diese Methode plaziert die Buttone bSet, bReset, bClear und das Logo auf
     * einem Panel
     */
    private JPanel placeButtonsAndLogo() {
        JPanel pButtons = new JPanel(new GridLayout(3, 1));
        bSet.setText(GUIText.getText("bset"));
        pButtons.add(bSet);
        bReset.setText(GUIText.getText("breset"));
        pButtons.add(bReset);
        bClear.setText(GUIText.getText("bclear"));
        pButtons.add(bClear);
        JPanel dirx = new JPanel(new BorderLayout());
        JPanel diry = new JPanel(new BorderLayout());
        dirx.add(pButtons, BorderLayout.SOUTH);
        diry.add(lLogo, BorderLayout.NORTH);
        diry.add(dirx, BorderLayout.SOUTH);
        return diry;
    }

    /**
     * Erstellt das Menü
     */
    private void createMenubar() {
        mSet.setText(GUIText.getText("mset"));
        menubar.add(mSet);
        mSetMachine.setText(GUIText.getText("msetmachine"));
        mSet.add(mSetMachine);
        for (String mach : availMachines) {
            rbMachine.add(new JRadioButtonMenuItem(mach));
        }
        rbMachine.get(0).setSelected(true);

        rbMachine.stream().map((mi) -> {
            groupMachines.add(mi);
            return mi;
        }).forEach(mSetMachine::add);

        mSetLang.setText(GUIText.getText("msetlang"));
        mSet.add(mSetLang);
        for (String lang : GUIText.getLanguages()) {
            rbLanguages.add(new JRadioButtonMenuItem(lang));
        }
        rbLanguages.get(0).setSelected(true);

        rbLanguages.stream().map((mi) -> {
            groupLang.add(mi);
            return mi;
        }).forEach(mSetLang::add);

        mAbout.setText(GUIText.getText("mabout"));
        menubar.add(mAbout);
        mAboutAbout.setText(GUIText.getText("maboutabout"));
        mAbout.add(mAboutAbout);

        mAboutHelp.setText(GUIText.getText("mabouthelp"));
        mAboutHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0,
                false));
        mAbout.add(mAboutHelp);
    }

    /**
     * Meldet alle bei allen Elementen einen ActionListener an, die ihn
     * benötigen
     */
    private void registerActionListeners() {
        bEncrypt.addActionListener(this);
        bSend.addActionListener(this);
        bSet.addActionListener(this);
        bReset.addActionListener(this);
        bClear.addActionListener(this);

        cMessages.addActionListener(this);
        pop.addActionListener(this);

        mAboutHelp.addActionListener(this);
        mAboutAbout.addActionListener(this);

        rbLanguages.stream().forEach((mi) -> mi.addActionListener(this));
        rbMachine.stream().forEach((mi) -> mi.addActionListener(this));

    }

    /**
     * Fügt alle GUIElemente einer Liste in GUIText hinzu, sofern diese
     * uebersetzbar sein sollen
     */
    private void initiateLangObjects() {
        GUIText.add(bSet, "bset");
        GUIText.add(bReset, "breset");
        GUIText.add(bClear, "bclear");
        GUIText.add(tbmessages, "tbmessages");
        GUIText.add(tbInput, "tbinput");
        GUIText.add(tbOutput, "tboutput");
        GUIText.add(bEncrypt, "bencrypt");
        GUIText.add(lrefl, "lrefl");
        GUIText.add(tbplugBoard, "tbplugboard");
        GUIText.add(bSend, "bsend");
        GUIText.add(mSet, "mset");
        GUIText.add(mSetMachine, "msetmachine");
        GUIText.add(mSetLang, "msetlang");
        GUIText.add(mAbout, "mabout");
        GUIText.add(mAboutAbout, "maboutabout");
        GUIText.add(mAboutHelp, "mabouthelp");
        GUIText.add(ttClear, "ttclear");
        GUIText.add(ttEncrypt, "ttencrypt");
        GUIText.add(ttMessages, "ttmessages");
        GUIText.add(ttReset, "ttreset");
        GUIText.add(ttSend, "ttsend");
        GUIText.add(ttSet, "ttset");
    }

    /**
     * Aktualisiert die Sprache der Tooltips
     */
    private void updateTooltips() {
        bEncrypt.setToolTipText(ttEncrypt.getString());
        bSend.setToolTipText(ttSend.getString());
        bClear.setToolTipText(ttClear.getString());
        bReset.setToolTipText(ttReset.getString());
        bSet.setToolTipText(ttSet.getString());
        cMessages.setToolTipText(ttMessages.getString());
    }

    /**
     * L√∂scht alle Walzen vom Panel pRotors und der Arraylist rotors und setzt
     * sie neu, abh√§ngig von der aktuellen Maschine
     */
    private void createRotors() {
        pRotors.removeAll();
        rotors.clear();
        int maxRot = currentMachine.getNumberOfMappers();
        String[] conf = currentMachine.getCurrentConfiguration();

        for (int i = 1; i < maxRot; i++) {
            rotors.add(new GUIRotor(currentMachine.getAvailableMappersOnPosition(i), currentMachine.getMapper(i), conf[i]));
        }

        for (int i = maxRot - 2; i >= 0; i--) {
            pRotors.add(rotors.get(i));
        }

        rotors.stream().forEach((gr) -> gr.addRotorListeners(rotors));

        lrefl = rotors.get(rotors.size() - 1).getLrefl();

        resize();

        saveCurrSettings();
        startConfig.clear();
        for (int i = 0; i < config.size(); i++) {
            startConfig.add(i, config.get(i));
        }
    }

    /**
     * Diese Methode wird beim Maschinenwechsel aufgerufen. setVisible(false)
     * ist nötig um möglichen Anzeigefehlern vorzubeugen
     */
    private void resize() {
        setVisible(false);
        setResizable(true);
        setMinimumSize(new Dimension(0, 0));
        pack();
        setMinimumSize(getSize());
        setResizable(false);
        displayCenter(getWidth(), getHeight());
        setVisible(true);
    }

    @Override
    public void sendText(String text) {
    }

    @Override
    public void updateRotorSettings(char[] rotorSetting) {
        for (int i = 0; i < rotors.size() - 1; i++) {
            rotors.get(i).setLetter(rotorSetting[i]);
        }
    }

    @Override
    public void sendRecievedMessage(String msg) {
        if (Debug.isDebug()) {
            System.out.println("Message-History[9]: " + cMessages.getItemAt(9));
        }

        cMessages.removeActionListener(this);
        if (cMessages.getItemCount() >= 10) {
            cMessages.removeItemAt(9);
            cMessages.setSelectedIndex(0);
        }
        addMessageHistoryItem(new Date() + " --- " + msg);
        cMessages.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (e.getSource() == bEncrypt) {
            String input = taInput.getText();
            if (input.length() > 25000) {
                JOptionPane.showMessageDialog(this, GUIText.getText("toolong"));
                input = input.substring(0, 25000);
            }
            String subst = log.substitute(input);
            taInput.setText(subst);
            String[] rotConf = new String[currentMachine.getNumberOfMappers()];

            rotConf[0] = "Pb:" + plugBoard.getPlugSettings();
            for (int i = 0; i < rotors.size(); i++) {
                rotConf[i + 1] = rotors.get(i).getMapperConf();
            }

            taOutput.setText(log.encrypt(subst, rotConf));
        }

        for (JRadioButtonMenuItem mi : rbMachine) {
            if (e.getSource() == mi) {
                log.setMachine(mi.getText());
                currentMachine = log.getMachine(mi.getText());
                createRotors();
                return;
            }
        }

        for (JRadioButtonMenuItem mi : rbLanguages) {
            if (e.getSource() == mi) {
                GUIText.setLanguage(mi.getText());
                repaint();
                updateTooltips();
                repaint();
                return;
            }
        }

        if (e.getSource() == bSend) {
            log.sendMessage(taOutput.getText());
        }

        if (e.getSource() == bSet) {
            saveCurrSettings();
            return;
        } else if (e.getSource() == bReset) {
            resetCurrSettings();
            return;
        } else if (e.getSource() == bClear) {
            for (int i = 0; i < startConfig.size(); i++) {
                config.set(i, startConfig.get(i));
            }
            resetCurrSettings();
        } else if (src.equals(mAboutHelp)) {
            new Help();
        } else if (src.equals(mAboutAbout)) {
            ImageIcon icon = new ImageIcon(
                    readImageFromRessources("Logo_Enigma_small.png"));
            if (Debug.isDebug()) {
                System.out.println("Lang: " + GUIText.getLanguageCode());
            }
            JOptionPane.showMessageDialog(this, getTextFromFile("aboutText_" + 
                    GUIText.getLanguageCode() + ".txt"), 
                    GUIText.getText("maboutabout"), JOptionPane.PLAIN_MESSAGE, 
                    icon);
        }
        if (e.getSource() == pop) {
            System.exit(0);
        }
        if (e.getSource() == cMessages) {
            String chosen = cMessages.getSelectedItem().toString();

            String output = "";

            for (int i = chosen.length() - 1; ; i--) {
                if (chosen.charAt(i) < 'A' || chosen.charAt(i) > 'Z') {
                    break;
                }
                output = chosen.charAt(i) + output;
            }

            taInput.setText(output);
        }
    }

    /**
     * Speichert aktuelle Walzen- und Steckbrettkonfiguration
     */
    private void saveCurrSettings() {
        config.clear();
        config.add(plugBoard.getPlugSettings());
        config.addAll(rotors.stream().map(GUIRotor::getMapperConf).
                collect(Collectors.toList()));
    }

    /**
     * Setzt die Walzen- und Steckbrettkonfiguration zum letzten gespeicherten
     * Stand zur√ºck
     */
    private void resetCurrSettings() {
        plugBoard.setPlugSettings(config.get(0));
        for (int i = 0; i < rotors.size(); i++) {
            rotors.get(i).getAvailRotors().setSelectedItem(config.get(i + 1).
                    split(":")[0]);
            if (rotors.get(i).getMapper() instanceof Rotor) {
                rotors.get(i).setLetter(config.get(i + 1).
                        split(":")[1].charAt(0));
            }
        }

    }

    /**
     * Methode liesst ein Bild aus dem resources Ordner aus und gibt es als
     * Image Objekt zurueck
     *
     * @param fname Der Name des Bildes
     * @return Das gefundene Bild, falls keines gefunden wurde wird null
     * zurueckgegeben
     */
    private Image readImageFromRessources(String fname) {
        try {
            return ImageIO.read(new File("resources/" + fname));
        } catch (Exception e) {
            try {
                return ImageIO.read(getClass().getResource("/resources/" + fname));
            } catch (Exception e1) {
                if (Debug.isDebug()) {
                    System.out.println("Falscher Dateiname");
                }
                return null;
            }
        }
    }

    /**
     * Diese Methode setzt den TrayIcon und das Icon in der Titelleiste. Weiters
     * wird im Tray ein PopupMenu erstellt, welches die M√∂glichkeit bietet das
     * Programm zu schliessen
     *
     * @param imgName Der Name des Bildes (muss sich im Folder resources
     * befinden).
     */
    private void setIconsAndTray(String imgName) {
        Image image = readImageFromRessources(imgName);
        TrayIcon trayIcon = new TrayIcon(image);

        pop.add("Exit");

        trayIcon.setPopupMenu(pop);
        this.setIconImage(image);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Methode zum zentrieren und setzen der Groesse des Sniffer Fensters auf
     * dem Desktop
     *
     * @param w Breite des Fensters
     * @param h H√∂he des Fensters ATparam f Das JFrame selbst
     */
    private void displayCenter(int w, int h) {
        Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dem.getWidth() / 2 - w / 2), (int) (dem
                .getHeight() / 2)
                - h / 2);
    }

    private String getTextFromFile(String file) {
        BufferedReader br = getBufferedReader(file);
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private BufferedReader getBufferedReader(String fileName) {
        if (Debug.isDebug()) {
            System.out.println("Dateiname: " + fileName);
        }
        BufferedReader in = null;
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream("resources/" + fileName), "UTF-8"));
        } catch (Exception e) {
            try {
                return new BufferedReader(new InputStreamReader(Object.class.getResourceAsStream("/resources/" + fileName), "UTF-8"));
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
     * schlie√üt ein Closeable-Object
     *
     * @param closeable das zu schliessende Objekt
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

    private void addMessageHistoryItem(String msg) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < cMessages.getItemCount(); i++) {
            // toString() statt +"" zerschiesst geralds Netbook?
            items.add(cMessages.getItemAt(i));
        }
        cMessages.removeAllItems();

        cMessages.addItem(msg);

        for (String item : items) {
            cMessages.addItem(item);
        }
    }
}

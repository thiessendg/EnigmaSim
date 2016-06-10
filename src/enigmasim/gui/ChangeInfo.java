package enigmasim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Dient zum darstellen des Info Fensters wenn Buchstaben ersetzt wurden.
 *
 * @author Mario Heindl, Philip Woelfel <br />
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
public class ChangeInfo extends JFrame implements ActionListener {

    private final JButton ok = new JButton("OK");

    /**
     * Erstellt das Fenster mit einen bestimmten Titel
     *
     * @param title der Titel des angezeigten Fensters
     */
    public ChangeInfo(String title) {
        super(title);
        JLabel info = new JLabel("");
        GUIText.add(info, "lsuberror");

        info.setHorizontalAlignment(SwingConstants.CENTER);
        ok.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(ok);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(info, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(500, 140);
        displayCenter(500, 140);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            this.dispose();
        }
    }

    /**
     * Methode zum zentrieren und setzen der Groesse des Sniffer Fensters auf
     * dem Desktop
     *
     * @param w Breite des Fensters
     * @param h Hoehe des Fensters ATparam f Das JFrame selbst
     */
    private void displayCenter(int w, int h) {
        Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( (int) (dem.getWidth() / 2 - w / 2), 
                (int) (dem.getHeight() / 2) - h / 2 );
    }

}

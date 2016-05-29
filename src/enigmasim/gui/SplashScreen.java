package enigmasim.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import enigmasim.Debug;
//import org.jetbrains.annotations.Nullable;

/**
 * Ladebildschirm.
 *
 * Wird verwendet, um das Laden von 'GUIEnigma' anzuzeigen.
 *
 * @author Mathias Kub <br />
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
public class SplashScreen extends JFrame {

    private static JProgressBar progressBar;

    public SplashScreen() {
        setUndecorated(true);

        progressBar = new JProgressBar();
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(getSize().width, 10));
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(Color.BLACK);

        ImageIcon icon = new ImageIcon(readImageFromRessources("Logo_Enigma_small.png"));

        JLabel picLabel = new JLabel(icon);
        picLabel.setBorder(new LineBorder(Color.BLACK, 4));
        getContentPane().add(picLabel, BorderLayout.CENTER);
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        Dimension label = picLabel.getPreferredSize();
        setBounds(0, 0, label.width, label.height + progressBar.getPreferredSize().height);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - label.width) / 2, (screen.height - label.height) / 2);

        setVisible(true);
    }

    public static void updateProgressBar(int progressBarValue) {
        progressBar.setValue(progressBarValue);
    }

    //@Nullable
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

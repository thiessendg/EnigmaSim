/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enigmasim;

import javax.swing.UIManager;
import enigmasim.gui.GUIEnigma;
import enigmasim.gui.SplashScreen;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author dthiessen
 */
public class EnigmaSim {

    /**
     * Konstruktor, welcher die benoetigten Klassen initialisiert
     */
    public EnigmaSim() {
        SplashScreen splash = new SplashScreen();
        SplashScreen.updateProgressBar(0);
        /*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        /*
         */
        Logic logic = new Logic();
        SplashScreen.updateProgressBar(20);
        GUIEnigma gui = new GUIEnigma(logic);
        SplashScreen.updateProgressBar(90);
        Network network = new Network();
        logic.addLogicListener(network);
        logic.addLogicListener(gui);
        network.addNetworkListener(gui);

        SplashScreen.updateProgressBar(100);
        splash.setVisible(false);
        splash.dispose();
    } // end Konstruktor

    /**
     * main-Methode, welche den Konstruktor der Klasse aufruft
     *
     * @param args Argumente, welche von der Kommandozeile uebernommen, aber
     * nicht beachtet werden
     */
    public static void main(String[] args) {
        EnigmaSim enigma = new EnigmaSim();
    } // end main
} // end Klasse Enigma

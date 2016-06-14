/* 
 * 	This program simulates the function of the Enigma.
 * 
 *	Copyright © 2016 David Thiessen <thiessendg@gmail.com>
 *      
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

package enigmasim;

import javax.swing.UIManager;
import enigmasim.gui.GUIEnigma;
import enigmasim.gui.SplashScreen;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author David Thiessen <thiessendg@gmail.com>
 * 
 */
public class EnigmaSim {

    /**
     * The main method
     * 
     * @param args command line args (none)
     */

    public static void main(String[] args) {
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
    } // end main
} // end EnigmaSim

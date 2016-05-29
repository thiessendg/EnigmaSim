/* 
 * 	This program simulates the function of the Enigma.
 * 
 *	Copyright © 2010 Mathias Kub <git@makubi.at>
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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Diese Klasse wird zum Debugging verwendet. Die Einstellungen dazu sind in der
 * Datei 'ressources/enigma.properties' zu finden.
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
public class Debug {

    private static String fileName = "enigma.properties";
    private static Properties properties = new Properties();
    private static boolean DEBUG = readDebugVariableFromFile(fileName);

    /**
     * Gibt einen String aus wenn das Debugging aktiviert ist.
     *
     * @param debugMessage der String der ausgegeben wird
     */
    public static void print(String debugMessage) {
        if (DEBUG) {
            System.out.println(debugMessage);
        }
    }

    /**
     * ist das Debugging aktiviert?
     *
     * @return ob Debugging aktiviert ist
     */
    public static boolean isDebug() {
        return DEBUG;
    }

    /**
     * aktiviert oder deaktiviert Debugging
     *
     * @param debug der neue Wert fuers debuggen
     */
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * ACHTUNG: Der Dateinamenpfad ist relativ zum 'ressources'-Ordner.
     *
     * @param filename der Dateiname
     */
    public static void setFileName(String filename) {
        fileName = filename;
    }

    /**
     * ACHTUNG: Der Dateinamenpfad ist relativ zum 'ressources'-Ordner.
     *
     * @return der Dateiname
     */
    public static String getFileName() {
        return fileName;
    }

    /**
     * Liest die Debug Variable aus dem Properties-File ein.
     *
     * @param fileName der Name des Property-Files
     * @return der ausgelesene Wert
     */
    private static boolean readDebugVariableFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReader(fileName);
            properties.load(reader);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return properties.getProperty("debug").equals("true") ? true : false;
    }

    /**
     * Gibt den passenden BufferedReader zurueck, egal ob in eine Jar-File oder
     * nicht.
     *
     * @param fileName der Dateiname fuer den der BufferedReader gebraucht wird
     * @return der BufferedReader fuer das File
     */
    private static BufferedReader getBufferedReader(String fileName) {
        BufferedReader in = null;
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream("ressources/" + fileName), "UTF-8"));
        } catch (Exception e) {
            try {
                return new BufferedReader(new InputStreamReader(new Object().getClass().getResourceAsStream("/ressources/" + fileName), "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
                System.exit(-1);
            } finally {
                closeStream(in);
            }
        }
        return in;
    }

    /**
     * schließt ein Closeable-Object
     *
     * @param closeable das zu schliessende Objekt
     */
    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

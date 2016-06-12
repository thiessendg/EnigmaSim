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
 * This class is used for debugging. The settings for this can be found in the 
 * 'resources/enigma.properties' file.
 *
 * @author Mathias Kub 
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
public class Debug {

    private static final String FILENAME = "enigma.properties";
    private static final Properties PROPERTIES = new Properties();
    private static final boolean DEBUG = readDebugVariableFromFile(FILENAME);

    /**
     * is debugging enabled
     *
     * @return boolean indicating whether debug level output enabled
     */
    public static boolean isDebug() {
        return DEBUG;
    }

    /**
     * Reads the debug variable from the properties file.
     *
     * @param fileName the filename of properties file
     * @return the value read
     */
    private static boolean readDebugVariableFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReader(fileName);
            PROPERTIES.load(reader);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        //return properties.getProperty("debug").equals("true") ? true : false;
        return Boolean.parseBoolean(PROPERTIES.getProperty("debug"));
    }

    /**
     * Is back for royalty BufferedReader, whether in a jar file or not.
     *
     * @param fileName the filename for the the BufferedReader
     * @return the BufferedReader for the file
     */
    private static BufferedReader getBufferedReader(String fileName) {
        BufferedReader in = null;
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(
                    "resources/" + fileName), "UTF-8"));
        } catch (Exception e) {
            try {
                return new BufferedReader(new InputStreamReader(
                        Object.class.getResourceAsStream(
                                "/resources/" + fileName), "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
                //TODO: This needs to change!
                System.exit(-1);
            } finally {
                closeStream(null);
            }
        }
        return in;
    }

    /**
     * indicates a Closeable Object
     *
     * @param closeable the Closeable Object
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

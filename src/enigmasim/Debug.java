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
 * @author David Thiessen based on previous work by Mathias Kub 
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

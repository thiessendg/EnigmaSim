package enigmasim;

/**
 * @author Mina Toma 
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
public interface NetworkListener {

    /**
     *
     * @param msg
     */
    void sendRecievedMessage(String msg);
}

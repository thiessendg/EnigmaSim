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
public interface LogicListener {

    /**
     *
     * @param text
     */
    void sendText(String text);

    /**
     *
     * @param rotorSetting
     */
    void updateRotorSettings(char[] rotorSetting);
}

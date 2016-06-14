package enigmasim;

/**
 * @author David Thiessen based on previous work by Mina Toma 
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

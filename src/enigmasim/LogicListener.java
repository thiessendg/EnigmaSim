package enigmasim;

/**
 * @author Mina Toma <br />
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
public interface LogicListener {

    public void sendText(String text);

    public void updateRotorSettings(char[] rotorSetting);
}

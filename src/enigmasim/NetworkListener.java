package enigmasim;

/**
 * @author David Thiessen based on previous work by Mina Toma 
 *
 */
public interface NetworkListener {

    /**
     *
     * @param msg
     */
    void sendRecievedMessage(String msg);
}

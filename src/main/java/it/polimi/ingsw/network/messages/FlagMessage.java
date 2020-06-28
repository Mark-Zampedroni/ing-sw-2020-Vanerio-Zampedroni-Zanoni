package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class FlagMessage extends Message implements Serializable {

    private static final long serialVersionUID = -6450677796291264081L;
    private final boolean flag;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param info information
     * @param flag boolean flag
     * @param type type of the message
     * @param recipient the recipient of the message
     */
    public FlagMessage(MessageType type, String sender, String info, boolean flag, String recipient) {
        super(type, sender, info, recipient);
        this.flag = flag;
    }


    /**
     * Getter for the flag
     *
     * @return flag
     */
    public boolean getFlag() {
        return flag;
    }

}

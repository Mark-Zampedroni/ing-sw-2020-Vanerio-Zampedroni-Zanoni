package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class FlagMessage extends Message implements Serializable {

    private static final long serialVersionUID = -6450677796291264081L;
    private final boolean flag;

    public FlagMessage(MessageType type, String sender, String info, boolean flag, String recipient) {
        super(type, sender, info, recipient);
        this.flag = flag;
    }

    public boolean getFlag() { return flag; }

}

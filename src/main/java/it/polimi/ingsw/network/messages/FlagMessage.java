package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.MessageType;

public class FlagMessage extends Message {

    private final boolean flag;

    public FlagMessage(MessageType type, String sender, String info, boolean flag) {
        super(type, sender, info);
        this.flag = flag;
    }

    public boolean getFlag() { return flag; }

}

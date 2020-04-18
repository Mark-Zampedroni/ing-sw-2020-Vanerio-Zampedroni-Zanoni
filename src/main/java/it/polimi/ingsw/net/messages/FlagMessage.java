package it.polimi.ingsw.net.messages;

import it.polimi.ingsw.enumerations.MessageType;

public class FlagMessage extends Message {

    boolean flag;

    public FlagMessage(MessageType type, String sender, String info, boolean flag) {
        super(type, sender, info);
        this.flag = flag;
    }

    public boolean getFlag() { return flag; }

}

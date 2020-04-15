package it.polimi.ingsw.net.messages;

import it.polimi.ingsw.enumerations.MessageType;

public class UpdateMessage extends Message {

    public UpdateMessage(String sender, String info) {
        super(MessageType.UPDATE, sender, info);
    }
}

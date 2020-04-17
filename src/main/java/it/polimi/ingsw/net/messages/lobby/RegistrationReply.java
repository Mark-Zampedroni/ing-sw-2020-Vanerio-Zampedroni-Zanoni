package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.Message;

public class RegistrationReply extends Message {

    boolean success;

    public RegistrationReply(String sender, String info, boolean success) {
        super(MessageType.REGISTRATION, sender, info);
        this.success = success;
    }

    public boolean isSuccess() { return success; }

}

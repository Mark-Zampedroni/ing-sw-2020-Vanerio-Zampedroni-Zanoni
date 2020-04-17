package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.Message;

public class RegistrationMessage extends Message {

    private Colors color;

    public RegistrationMessage(String sender, String info, Colors color) {
        super(MessageType.REGISTRATION, sender, info);
        this.color = color;
    }

    public Colors getColor() { return color; }

}

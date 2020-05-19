package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

public class RegistrationMessage extends Message {

    private final Colors color;

    public RegistrationMessage(String sender, String info, Colors color, String recipient) {
        super(MessageType.REGISTRATION, sender, info, recipient);
        this.color = color;
    }

    public Colors getColor() { return color; }

}

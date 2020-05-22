package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

import java.io.Serializable;

public class RegistrationMessage extends Message implements Serializable {

    private static final long serialVersionUID = 5046831758303349320L;
    private final Colors color;

    public RegistrationMessage(String sender, String info, Colors color, String recipient) {
        super(MessageType.REGISTRATION_UPDATE, sender, info, recipient);
        this.color = color;
    }

    public Colors getColor() { return color; }

}

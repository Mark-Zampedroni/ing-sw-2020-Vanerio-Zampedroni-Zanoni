package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class RegistrationMessage extends Message implements Serializable {

    private static final long serialVersionUID = 5046831758303349320L;
    private final Colors color;


    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param info information
     * @param color the color
     * @param recipient the recipient of the message
     */
    public RegistrationMessage(String sender, String info, Colors color, String recipient) {
        super(MessageType.REGISTRATION_UPDATE, sender, info, recipient);
        this.color = color;
    }

    /**
     * Getter for the color
     *
     * @return the color
     */
    public Colors getColor() {
        return color;
    }

}

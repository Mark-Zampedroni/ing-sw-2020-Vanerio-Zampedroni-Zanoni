package it.polimi.ingsw.net.messages;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.MessageType;

import java.util.ArrayList;
import java.util.List;

public class RegistrationReply extends Message {

    private List<Colors> colors;

    public RegistrationReply(MessageType type, String sender, String info, List<Colors> colors) {
        super(type, sender, info);
        this.colors = colors;
    }

    public RegistrationReply(MessageType type, String sender, String info) {
        super(type, sender, info);
        this.colors = new ArrayList<>();
    }

    public List<Colors> getColors() {
        return colors;
    }
}

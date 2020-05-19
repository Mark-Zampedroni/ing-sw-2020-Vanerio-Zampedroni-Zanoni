package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;

public class ActionMessage extends Message {

    private final Action action;
    private final DtoPosition position;

    public ActionMessage(String sender, String content, Action action, DtoPosition position, String recipient) {
        super(MessageType.ACTION, sender, content, recipient);
        this.action = action;
        this.position = position;
    }

    public DtoPosition getPosition() {
        return position;
    }

    public Action getAction() {
        return action;
    }
}


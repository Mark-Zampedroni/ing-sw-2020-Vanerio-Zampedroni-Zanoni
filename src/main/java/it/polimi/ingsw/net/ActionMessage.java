package it.polimi.ingsw.net;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.MessageType;

public class ActionMessage extends Message {

    private Action action;

    public ActionMessage(String sender, String content, Action action) {
        super(MessageType.ACTION, sender, content);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

}


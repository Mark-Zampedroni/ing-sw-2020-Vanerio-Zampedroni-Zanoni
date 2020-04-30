package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;

public class ActionMessage extends Message {

    private final Action action;
    private final DTOposition position;

    public ActionMessage(String sender, String content, Action action, DTOposition position) {
        super(MessageType.ACTION, sender, content);
        this.action = action;
        this.position = position;
    }

    public DTOposition getPosition() {
        return position;
    }

    public Action getAction() {
        return action;
    }
}


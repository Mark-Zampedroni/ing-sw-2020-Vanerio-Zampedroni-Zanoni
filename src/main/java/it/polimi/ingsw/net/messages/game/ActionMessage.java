package it.polimi.ingsw.net.messages.game;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.net.messages.Message;

public class ActionMessage extends Message {

    private final Action action;
    private final Position position;
    private final Worker worker;

    public ActionMessage(String sender, String content, Action action, Position position, Worker worker) {
        super(MessageType.ACTION, sender, content);
        this.action = action;
        this.position = position;
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    public Position getPosition() {
        return position;
    }

    public Action getAction() {
        return action;
    }
}


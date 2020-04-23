package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.messages.Message;

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


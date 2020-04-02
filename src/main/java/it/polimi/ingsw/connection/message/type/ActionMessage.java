package it.polimi.ingsw.connection.message.type;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.model.player.Worker;

public class ActionMessage extends Message {
    Worker worker;
    Action type;

    public Worker getWorker() {
        return worker;
    }

    public Action getType() {
        return type;
    }

}

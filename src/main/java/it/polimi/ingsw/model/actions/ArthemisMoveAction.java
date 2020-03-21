package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class ArthemisMoveAction extends MoveAction{

    @Override
    public void fixOthers(Position position, Position oldPosition, Worker worker) {
        worker.getMaster().getRules().setEvent(true);
        worker.getMaster().getRules().setPos(position);
    }
}

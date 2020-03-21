package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class MinotaurMoveAction extends MoveAction {

    @Override
    public void fixOthers(Position position, Position oldPosition, Worker worked) {
        Position newPosition = position.copy();
        newPosition.setValue(position.getX()-oldPosition.getX()+position.getX(), position.getY()-oldPosition.getY()+position.getY());
        position.getWorker().setPosition(newPosition);
    }
}

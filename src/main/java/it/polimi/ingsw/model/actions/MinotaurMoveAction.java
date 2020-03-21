package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;

public class MinotaurMoveAction extends MoveAction {

    @Override
    public void fixOthers(Position position, Position oldPosition) {
        Position newPosition = new Position();
        newPosition.setValue(position.getX()-oldPosition.getX()+position.getX(), position.getY()-oldPosition.getY()+position.getY());
        position.getWorker().setPosition(newPosition);
    }
}

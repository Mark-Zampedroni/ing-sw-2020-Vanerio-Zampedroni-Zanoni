package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class MinotaurMoveAction extends MoveAction {

    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worked) {
        Position newPosition = position.copy();
        newPosition.setValue(position.getX()-oldPosition.getX()+position.getX(), position.getY()-oldPosition.getY()+position.getY());
        position.getWorker().setPosition(newPosition);
        return super.fixOthers(position, oldPosition, worked);
    }
}

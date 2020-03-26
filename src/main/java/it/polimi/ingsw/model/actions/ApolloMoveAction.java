package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class ApolloMoveAction extends MoveAction {

    @Override
    public ArrayList <ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        position.getWorker().setPosition(oldPosition);
        return super.fixOthers(position, oldPosition, worker);
    }
}

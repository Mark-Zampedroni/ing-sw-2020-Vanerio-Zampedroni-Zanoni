package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class ApolloMoveAction extends MoveAction {

    @Override
    public void fixOthers(Position position, Position oldPosition, Worker worker) {
        position.getWorker().setPosition(oldPosition);
    }
}

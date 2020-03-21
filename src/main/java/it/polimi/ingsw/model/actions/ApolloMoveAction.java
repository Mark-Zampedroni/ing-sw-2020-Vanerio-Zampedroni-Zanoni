package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;

public class ApolloMoveAction extends MoveAction {

    @Override
    public void fixOthers(Position position, Position oldPosition) {
        position.getWorker().setPosition(oldPosition);
    }
}

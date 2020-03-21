package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class AthenaMoveAction {
    @Override
    public void fixOthers(Position position, Position oldPosition, Worker worker) {
        if (Board.getTile(oldPosition).getHeight() < Board.getTile(position).getHeight()) {
            worker.getMaster().getRules().setEvent(true);
        }
    }
}

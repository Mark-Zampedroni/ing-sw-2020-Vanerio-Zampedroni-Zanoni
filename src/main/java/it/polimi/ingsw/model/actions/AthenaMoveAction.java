package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;

import java.util.ArrayList;

public class AthenaMoveAction extends MoveAction{
    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        if (Board.getTile(oldPosition).getHeight() < Board.getTile(position).getHeight()) {
            EventRule rules = (EventRule) worker.getMaster().getRules();
            rules.setEvent(true);
        }
        return super.fixOthers(position, oldPosition, worker);
    }
}

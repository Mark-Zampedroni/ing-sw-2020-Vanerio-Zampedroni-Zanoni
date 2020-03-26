package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;

import java.util.ArrayList;

public class TritonMoveAction extends MoveAction {

    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        ArrayList<ActionType> nextAction = super.fixOthers(position, oldPosition, worker);
        if (position.isBoundary()) {
            nextAction.add(ActionType.MOVE);
        }
        return nextAction;
        }
}


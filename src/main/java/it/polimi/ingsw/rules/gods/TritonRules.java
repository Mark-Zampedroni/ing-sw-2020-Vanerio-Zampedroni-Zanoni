package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;

public class TritonRules extends GodRules {
    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        ArrayList<ActionType> nextAction = super.fixOthers(position, oldPosition, worker);
        if (position.isBoundary()) {
            nextAction.add(ActionType.MOVE);
        }
        return nextAction;
    }
}

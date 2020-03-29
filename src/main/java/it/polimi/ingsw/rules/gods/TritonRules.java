package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;
import java.util.List;

public class TritonRules extends EventRule {

    @Override
    public void executeMove(Worker worker, Position position) {
        super.executeMove(worker, position);
        setEvent(position.isBoundary());
    }

    @Override
    public List<ActionType> afterMove() {
        List<ActionType> actions = super.afterMove();
        if(getEvent()) { actions.add(ActionType.MOVE); }
        return actions;
    }

}

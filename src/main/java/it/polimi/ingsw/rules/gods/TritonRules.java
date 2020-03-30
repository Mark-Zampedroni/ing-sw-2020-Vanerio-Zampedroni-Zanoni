package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

public class TritonRules extends EventRules {

    @Override
    public void executeMove(Worker worker, Position position) {
        super.executeMove(worker, position);
        setEvent(position.isBoundary());
    }

    @Override
    public List<Action> afterMove() {
        List<Action> actions = super.afterMove();
        if(getEvent()) { actions.add(Action.MOVE); }
        return actions;
    }

}

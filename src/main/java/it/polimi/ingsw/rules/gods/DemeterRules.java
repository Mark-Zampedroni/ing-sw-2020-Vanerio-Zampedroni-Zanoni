package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

// event is true when additional building turn has been occurred

public class DemeterRules extends EventRules {

    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
            setPos(position);
        }
        super.executeBuild(position);
    }

    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) { actions.add(Action.BUILD); }
        return actions;
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.oldPosition(worker, position);
    }

}
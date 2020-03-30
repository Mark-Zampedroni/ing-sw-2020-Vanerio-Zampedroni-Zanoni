package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

public class PrometheusRules extends EventRules {
//event true if the worker has built before movement

    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
        }
        super.executeBuild(position);
    }

    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) { actions.add(Action.MOVE); }
        return actions;
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.height(worker, position);
    }

}
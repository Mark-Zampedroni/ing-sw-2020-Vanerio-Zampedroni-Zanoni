package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRule;
import java.util.List;

public class PrometheusRules extends EventRule {
//event true if the worker has built before movement

    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
        }
        super.executeBuild(position);
    }

    @Override
    public List<ActionType> afterBuild() {
        List<ActionType> actions = super.afterBuild();
        if(!getEvent()) { actions.add(ActionType.MOVE); }
        return actions;
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.height(worker, position);
    }

}
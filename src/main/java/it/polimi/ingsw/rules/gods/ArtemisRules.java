package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRule;
import java.util.List;

// event is true when additional movement turn has been occurred
public class ArtemisRules extends EventRule {

    @Override
    public List<ActionType> afterMove() {
        List<ActionType> actions = super.afterMove();
        if(!getEvent()) { actions.add(ActionType.MOVE); }
        return actions;
    }

    @Override
    public void executeMove(Worker worker, Position position) {
        setEvent(true);
        setPos(position);
        super.executeMove(worker, position);
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.oldPosition(worker,position);
    }

}
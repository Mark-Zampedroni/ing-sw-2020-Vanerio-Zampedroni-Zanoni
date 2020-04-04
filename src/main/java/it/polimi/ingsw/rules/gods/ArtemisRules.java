package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

// event is true when additional movement turn has been occurred
public class ArtemisRules extends EventRules {

    @Override
    public List<Action> afterMove() {
        List<Action> actions = super.afterMove();
        if(!getEvent()) { actions.add(Action.MOVE); }
        return actions;
    }

    @Override
    public void executeMove(Worker worker, Position position) {
        setEvent(true);
        setPos(worker.getPosition());
        super.executeMove(worker, position);
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.oldPosition(worker,position);}

}
package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.ArtemisException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
// event is true when additional movement turn has been occurred
public class ArtemisRules extends EventRule {

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (!getEvent())
        {
            setPos(worker.getPosition());
        }
        else
        {
            setEvent(false);
            if (position.equals(getPos()))
            {
                throw new ArtemisException("Not Allowed");
            }
        }
    }

}
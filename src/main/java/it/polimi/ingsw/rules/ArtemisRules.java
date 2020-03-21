package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.ArtemisException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.player.Worker;
// event is true when additional movement turn has been occurred
public class ArtemisRules extends GodRules {

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (getEvent() && position.equals(getPos()))
        {
            throw new ArtemisException("Not Allowed");
        }
        setPos(worker.getPosition());

    }

}
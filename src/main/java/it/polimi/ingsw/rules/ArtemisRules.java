package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.MoveGodPowerException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.player.Worker;
// event is true when additional movement turn has been occurred
public class ArtemisRules extends EventRule {

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (getEvent() && position.equals(getPos()))
        {
            throw new MoveGodPowerException("Not Allowed");
        }
    }

}
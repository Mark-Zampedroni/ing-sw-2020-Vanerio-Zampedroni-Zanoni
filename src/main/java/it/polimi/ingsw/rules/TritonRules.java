package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.MoveGodPowerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
//event is true if it's NOT the first movement
public class TritonRules extends EventRule {
     @Override
     public void consentMovement(Worker worker, Position position) throws CantMoveException {
         super.consentMovement(worker, position);
         if(getEvent() && !position.isBoundary())
         {
             throw new MoveGodPowerException("Not Allowed");
         }
     }

     }

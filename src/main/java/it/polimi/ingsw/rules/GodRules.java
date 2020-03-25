package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class GodRules extends GodSharedRules{

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if(position.getWorker()!=null)
        {
            throw new AlreadyOccupiedException("This tile is already occupied");
        }
    }


    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if(position.getWorker()!=null)
        {
            throw new MoveOnWorkerException("This tile is already occupied");
        }

    }

}
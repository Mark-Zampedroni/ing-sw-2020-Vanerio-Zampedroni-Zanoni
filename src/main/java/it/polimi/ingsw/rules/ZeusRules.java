package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class ZeusRules extends GodRules {
    @Override
    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        if(worker.getPosition().getDistanceFrom(position)!=1 )
        {
            throw new OutOfReachException("Maximum building limit exceeded");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new AlreadyCompleteException("This tile is no longer available for building");
        }
        else if(position.getWorker()!=null && worker.getPosition()!=position)
        {
            throw new AlreadyOccupiedException("This tile is already occupied");
        }

    }
}

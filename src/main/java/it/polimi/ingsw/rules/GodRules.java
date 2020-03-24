package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import static it.polimi.ingsw.constants.Height.*;

public abstract class GodRules {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        if(worker.getPosition().getDistanceFrom(position)!=1 )
        {
            throw new OutOfReachException("Maximum building limit exceeded");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new AlreadyCompleteException("This tile is no longer available for building");
        }
        else if(position.getWorker()!=null)
        {
            throw new AlreadyOccupiedException("This tile is already occupied");
        }

        }


    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if(worker.getPosition().getDistanceFrom(position)!=1)
        {
            throw new MoveOutsideRangeException("Maximum movement limit exceeded");
        }
        else if(position.getWorker()!=null)
        {
            throw new MoveOnWorkerException("This tile is already occupied");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new DomeMoveException("This tile is no longer available for movement");
        }
        else if(Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() +1)
        {
            throw new ClimbMoveException("Unreachable tile from this position");
        }
        else if(blockedByEnemy(worker, position))
        {
            throw new MoveGodPowerException("Not allowed due to Athena's power");
        }



    }

    public boolean isWinner(Worker worker, Position position) {
        return(Board.getTile(worker.getPosition()).getHeight()==MID && Board.getTile(position).getHeight()==TOP);
    }

    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false;
    }



}
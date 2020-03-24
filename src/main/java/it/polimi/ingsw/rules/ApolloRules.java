package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.MoveOnAllyWorkerException;
import it.polimi.ingsw.exceptions.actions.movement.MoveOutsideRangeException;
import it.polimi.ingsw.exceptions.actions.movement.DomeMoveException;
import it.polimi.ingsw.exceptions.actions.movement.ClimbMoveException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class ApolloRules extends GodRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1 )
        {
            throw new MoveOutsideRangeException("Maximum movement limit exceeded");
        }
        else if ( Board.getTile(position).hasDome())
        {
            throw new DomeMoveException("This tile is no longer available for movement");
        }
        else if ( Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1)
        {
            throw new ClimbMoveException("Unreachable tile from this position");
        }
        else if(worker.getMaster()==position.getWorker().getMaster())
        {
            throw new MoveOnAllyWorkerException("Same player");
        }
    }
}

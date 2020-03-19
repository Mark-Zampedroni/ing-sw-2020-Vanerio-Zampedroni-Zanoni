package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.SamePlayerException;
import it.polimi.ingsw.exceptions.actions.movement.aException;
import it.polimi.ingsw.exceptions.actions.movement.bException;
import it.polimi.ingsw.exceptions.actions.movement.cException;
import it.polimi.ingsw.exceptions.actions.movement.dException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class ApolloRules extends GodRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1 )
        {
            throw new aException("Maximum movement limit exceeded");
        }
        else if ( Board.getTile(position).hasDome())
        {
            throw new cException("This tile is no longer available for movement");
        }
        else if ( Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1)
        {
            throw new dException("Unreachable tile from this position");
        }
        else if(worker.getMaster()==position.getWorker().getMaster())
        {
            throw new SamePlayerException("Same player");
        }
    }
}

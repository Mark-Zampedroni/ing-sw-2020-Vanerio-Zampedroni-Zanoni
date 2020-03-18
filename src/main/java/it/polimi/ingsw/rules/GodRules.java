package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.exceptions.actions.movement.aException;
import it.polimi.ingsw.exceptions.actions.movement.bException;
import it.polimi.ingsw.exceptions.actions.movement.cException;
import it.polimi.ingsw.exceptions.actions.movement.dException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import static it.polimi.ingsw.constants.Height.*;

public abstract class GodRules {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        if(worker.getPosition().getDistanceFrom(position)!=1 || !askEnemyConsent())
        {
            throw new OutOfReachException("U went too far");
        }
        else if(!askEnemyConsent() || Board.getTile(position).hasDome())
        {
            throw new AlreadyCompleteException("Dome");
        }
        else if(!askEnemyConsent() || position.getWorker()!=null)
        {
            throw new AlreadyOccupiedException("There is another");
        }

    }

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if(worker.getPosition().getDistanceFrom(position)!=1 || !askEnemyConsent())
        {
            throw new aException("U went too far");
        }
        else if(!askEnemyConsent() || position.getWorker()!=null)
        {
            throw new bException("There is another");
        }
        else if(!askEnemyConsent() || Board.getTile(position).hasDome())
        {
            throw new cException("Dome");
        }
        else if(!askEnemyConsent() || Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() +1)
        {
            throw new dException("U went too high");
        }


    }

    public boolean checkWin(Worker worker) {
        return(Board.getTile(worker.getPosition()).getHeight()==TOP);
    }

    public boolean askEnemyConsent() { return true; } // Qui si chiede consenso alle regole dei nemici
}

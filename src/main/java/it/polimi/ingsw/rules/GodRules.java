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

    public void consentBuild(Worker worker, Position position) throws CantBuildException, EnemyConsentException {
        if(worker.getPosition().getDistanceFrom(position)!=1 || !askEnemyConsent())
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
        else if(!askEnemyConsent())
        {
            throw new EnemyConsentException("Not Allowed");
        }
        }


    public void consentMovement(Worker worker, Position position) throws CantMoveException, EnemyConsentException {
        if(worker.getPosition().getDistanceFrom(position)!=1 || !askEnemyConsent())
        {
            throw new aException("Maximum movement limit exceeded");
        }
        else if(position.getWorker()!=null)
        {
            throw new bException("This tile is already occupied");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new cException("This tile is no longer available for movement");
        }
        else if(Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() +1)
        {
            throw new dException("Unreachable tile from this position");
        }
        else if(!askEnemyConsent())
        {
            throw new EnemyConsentException("Not Allowed");
        }


    }

    public boolean isWinner(Worker worker, Position position) {
        return(Board.getTile(worker.getPosition()).getHeight()==MID && Board.getTile(position).getHeight()==TOP); // return(Board.getTile(worker.getPosition()).getHeight()==TOP);
                                                                                                                    // Dipende quando viene effettuato il controllo, adesso subito successivo al consentMovement
    }

    public boolean askEnemyConsent() { return true; } // Qui si chiede consenso alle regole dei nemici
}

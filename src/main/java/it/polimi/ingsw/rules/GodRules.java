package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

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
        if(Board.getTile(worker.getPosition()).getHeight()==3)
        {
            return true;
        }
        else{
            return false;
        }

    }

    public boolean askEnemyConsent() { return true; } // Qui si chiede consenso alle regole dei nemici
}

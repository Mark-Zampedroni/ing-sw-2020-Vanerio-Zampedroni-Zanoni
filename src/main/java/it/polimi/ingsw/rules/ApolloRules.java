package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
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
        if (worker.getPosition().getDistanceFrom(position) != 1 || !askEnemyConsent())
        {
            throw new aException("Maximum moviment limit excedeed");
        }
        else if (!askEnemyConsent() || Board.getTile(position).hasDome())
        {
            throw new cException("This tile is no longer available for movement");
        }
        else if (!askEnemyConsent() || Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1)
        {
            throw new dException("Unreachable tile from this position");
        }
    }
}

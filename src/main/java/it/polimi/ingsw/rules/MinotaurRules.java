package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.SamePlayerException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class MinotaurRules extends GodRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1 || !askEnemyConsent()) {
            throw new aException("Maximum movement limit exceeded");
        } else if (!askEnemyConsent() || Board.getTile(position).hasDome()) {
            throw new cException("This tile is no longer available for movement");
        } else if (!askEnemyConsent() || Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1) {
            throw new dException("Unreachable tile from this position");
        } else if (!isValide(getPositionBackwards(worker.getPosition(), position))) {
            throw new OutofBorderException("Out of border");
        } else if (!askEnemyConsent() || getPositionBackwards(worker.getPosition(), position).getWorker() != null || Board.getTile(getPositionBackwards(worker.getPosition(), position)).hasDome()) {
            throw new MinotaurException("Occupied tile");
        }
        else if(worker.getMaster()==position.getWorker().getMaster())
        {
            throw new SamePlayerException("Same player");
        }
    }

    public Position getPositionBackwards(Position pos1, Position pos2) {
        return new Position(pos2.getX() + (pos2.getX() - pos1.getX()), pos2.getY() + (pos2.getY() - pos1.getY()));
    }

}
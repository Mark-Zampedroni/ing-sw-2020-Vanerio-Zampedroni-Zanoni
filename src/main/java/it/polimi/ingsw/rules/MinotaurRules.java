package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class MinotaurRules extends ApolloRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker,position);
          if (getPositionBackwards(worker.getPosition(), position).isValid()) {
            throw new OutofBorderException("Out of border");
        } else if (getPositionBackwards(worker.getPosition(), position).getWorker() != null || Board.getTile(getPositionBackwards(worker.getPosition(), position)).hasDome()) {
            throw new MoveGodPowerException("Occupied tile");
        }

    }

    public Position getPositionBackwards(Position pos1, Position pos2) {
        return new Position(pos2.getX() + (pos2.getX() - pos1.getX()), pos2.getY() + (pos2.getY() - pos1.getY()));
    }

}
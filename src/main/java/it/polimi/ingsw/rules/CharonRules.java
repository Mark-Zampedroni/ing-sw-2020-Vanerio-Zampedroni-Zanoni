package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class CharonRules extends GodRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1) {
            throw new aException("Maximum movement limit exceeded");
        } else if (Board.getTile(position).hasDome()) {
            throw new cException("This tile is no longer available for movement");
        } else if ( Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1) {
            throw new dException("Unreachable tile from this position");
        } else if (getOppositePosition(worker.getPosition(), position).isValid()) {
            throw new OutofBorderException("Out of border");
        } else if (getOppositePosition(worker.getPosition(), position).getWorker() != null || Board.getTile(getOppositePosition(worker.getPosition(), position)).hasDome()) {
            throw new CharonException("Occupied tile");
        }
        else if(worker.getMaster()==position.getWorker().getMaster())
        {
            throw new SamePlayerException("Same player");
        }
    }

    public Position getOppositePosition(Position pos1, Position pos2) {
        return new Position(pos1.getX() + (pos1.getX() - pos2.getX()), pos1.getY() + (pos1.getY() - pos2.getY()));
    }

}

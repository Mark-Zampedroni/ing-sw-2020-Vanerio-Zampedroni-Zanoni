package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class MinotaurRules extends ApolloRules {

    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worked) {
        position.getWorker().setPosition(getPositionBackwards(position, oldPosition));
        return super.fixOthers(position, oldPosition, worked);
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker,position);
        if(position.getWorker()!=null) {
            Position backpos = getPositionBackwards(worker.getPosition(), position);
            Check.positionValidity(backpos, true, "Can't push enemy worker out of boundaries");
            Check.occupant(worker, backpos, Target.ANY, true, "Can't push enemy worker to an occupied tile");
            Check.dome(backpos, true, "Can't push enemy worker to a tile with a dome");
        }

    }

    public Position getPositionBackwards(Position from, Position to) {
        return new Position(2*to.getX() - from.getX(), 2*to.getY() - from.getY());
    }

}
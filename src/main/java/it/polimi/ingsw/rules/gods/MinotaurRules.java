package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
public class MinotaurRules extends ApolloRules {

    @Override
    public void executeMove(Worker worker, Position position) {
        position.getWorker().setPosition(getPositionBackwards(worker.getPosition(),position));
        worker.setPosition(position);
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
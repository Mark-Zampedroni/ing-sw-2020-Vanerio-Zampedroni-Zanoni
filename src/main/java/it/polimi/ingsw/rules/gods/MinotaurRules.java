package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;


/**
 * Rules for a player with Minotaur as God
 */
public class MinotaurRules extends ApolloRules {

    /**
     * Executes a movement {@link Action action}, eventually push the {@link Worker worker} of an {@link Player opponent}
     * in the {@link Position position} backwards if the {@link Position position} were occupied
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
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

    private Position getPositionBackwards(Position from, Position to) {
        return new Position(2*to.getX() - from.getX(), 2*to.getY() - from.getY());
    }

}
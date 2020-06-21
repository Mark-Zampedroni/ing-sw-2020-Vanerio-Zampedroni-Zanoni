package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Target;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;

/**
 * Rules for a player with Minotaur as God
 */
public class MinotaurRules extends ApolloRules implements Serializable {

    private static final long serialVersionUID = -3906822084144057573L;

    /**
     * Executes a {@link Action movement}, if the position is occupied by an opponent it's pushed
     * backwards (if possible)
     *
     * @param worker   selected worker
     * @param position position the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        oldPosition = worker.getPosition();
        if (position.getWorker() != null) {
            position.getWorker().setPosition(getPositionBackwards(worker.getPosition(), position));
        }
        worker.setPosition(position);
        notify(new DtoSession(Session.getInstance()));
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action}
     *
     * @param worker   worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        if (position.getWorker() != null) {
            Position backpos = getPositionBackwards(worker.getPosition(), position);
            Check.positionValidity(backpos, true, "Can't push enemy worker out of boundaries");
            Check.relation(worker, backpos, Target.ANY, true, "Can't push enemy worker to an occupied tile");
            Check.dome(backpos, true, "Can't push enemy worker to a tile with a dome");
        }
    }

    /**
     * Method used to calculate the new position of the enemy's worker after using the Minotaur's ability
     *
     * @param from {@link Position position} of the worker whose {@link Player player} has Minotaur as god
     * @param to   {@link Position position} of the enemy's worker current position
     * @return the {@link Position position} where the enemy's worker is going to be settled
     */
    private Position getPositionBackwards(Position from, Position to) {
        return new Position(2 * to.getX() - from.getX(), 2 * to.getY() - from.getY());
    }

}
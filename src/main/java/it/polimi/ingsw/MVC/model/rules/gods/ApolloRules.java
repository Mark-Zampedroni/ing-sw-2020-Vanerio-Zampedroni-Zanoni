package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Target;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.Check;
import it.polimi.ingsw.MVC.model.rules.GodRules;

/**
 * Rules for a player with Apollo as God
 */
public class ApolloRules extends GodRules {

    /**
     * Executes a movement {@link Action action}, eventually switch the {@link Worker worker}'s {@link Position oldPosition}
     * with the {@link Position position} of a {@link Player opponent}'s {@link Worker worker} that were in the {@link Position position}
     * where the {@link Worker worker} moved
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        if(position.getWorker() != null) {
            worker.switchPosition(position.getWorker());
        }
        else {
            super.executeMove(worker,position);
        }
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action}
     *
     * @param worker worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.relation(worker, position, Target.ALLY);
    }

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action}
     *
     * @param worker worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.relation(worker, position, Target.ANY);
        Check.distance(worker, position);
    }
}

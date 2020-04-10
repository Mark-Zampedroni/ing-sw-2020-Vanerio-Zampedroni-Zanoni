package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.GodRules;

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

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.occupant(worker, position, Target.ALLY);
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.occupant(worker, position, Target.ANY);
        Check.distance(worker, position);
    }
}

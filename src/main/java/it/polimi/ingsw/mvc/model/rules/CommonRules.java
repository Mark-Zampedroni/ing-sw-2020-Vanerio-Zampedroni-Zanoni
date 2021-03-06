package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Target;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;

/**
 * Rules shared by most of the gods
 */
public abstract class CommonRules extends GodRules implements Serializable {

    private static final long serialVersionUID = -7868858109759746638L;

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action}
     *
     * @param worker   worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.relation(worker, position, Target.ANY);
        Check.distance(worker, position);
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
        Check.relation(worker, position, Target.ANY);
    }

}
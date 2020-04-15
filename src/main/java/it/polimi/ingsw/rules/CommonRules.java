package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;

import java.io.Serializable;

/**
 * Rules shared by most of the gods
 */
public abstract class CommonRules extends GodRules implements Serializable {

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action}
     *
     * @param worker worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.occupant(worker, position, Target.ANY);
        Check.distance(worker, position);
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action}
     *
     * @param worker worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.occupant(worker, position, Target.ANY);

    }

}
package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EventRules;

import java.io.Serializable;
import java.util.List;

/**
 * Rules for a player with Hestia as God
 */

public class HestiaRules extends EventRules implements Serializable {

    private static final long serialVersionUID = 6239381109670423644L;

    /**
     * Returns a list of possible actions after a {@link Action build}, if it's the first build of the
     * turn adds another {@link Action build}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) {
            actions.add(Action.BUILD);
            setEvent(true);
        }
        return actions;
    }

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action},
     * for the second build checks if the position is boundary
     *
     * @param worker worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        if(getEvent()) {
            Check.boundary(position);
        }
    }
}

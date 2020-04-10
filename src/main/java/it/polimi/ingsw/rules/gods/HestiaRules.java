package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

/**
 * Rules for a player with Hestia as God
 */

public class HestiaRules extends EventRules {

    /**
     * Executes a build {@link Action action}, if it is the first build {@link Action action}
     * calls the {@link #setEvent setEvent} with {@code true} argument
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
        }
        super.executeBuild(position);
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is {@code true}
     * adds another {@link Action BUILD} action to the next actions list
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) { actions.add(Action.BUILD); }
        return actions;
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.boundary(position);
    }
}

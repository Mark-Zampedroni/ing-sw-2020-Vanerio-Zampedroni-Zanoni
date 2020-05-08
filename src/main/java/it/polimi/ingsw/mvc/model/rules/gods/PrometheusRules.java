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
 * Rules for a player with Prometheus as God
 */
public class PrometheusRules extends EventRules implements Serializable {

    /**
     * Executes a build {@link Action action}, if is the first build {@link Action action}
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
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is {@code true} the next action is {@link Action MOVE}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) {
            actions.add(Action.MOVE);
            actions.remove(0); }
        return actions;
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
        Check.height(worker, position, 0, "Tile out of reach");}
}
package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rules for a player with Prometheus as God
 */
public class PrometheusRules extends EventRules implements Serializable {

    private static final long serialVersionUID = 6128545018299786443L;
    private boolean movementFlag = false;

    /**
     * Executes a {@link Action build}
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        setEvent(!getEvent());
        super.executeBuild(position);
    }

    /**
     * Returns a list of possible actions after a {@link Action build}, if no movement was performed
     * in the turn then returns only move
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if (getEvent() && !movementFlag) {
            actions.add(Action.MOVE);
            actions.remove(0);
        }
        return actions;
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action},
     * if the player built before moving adds a check
     *
     * @param worker   worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        if (getEvent()) {
            Check.height(worker, position, 0, "Tile out of reach");
        }
    }

    /**
     * Returns a list of possible actions after a {@link Action selection},
     * adds an optional build
     *
     * @return list of {@link Action actions} that can be done after {@link Action selection}
     */
    @Override
    public List<Action> afterSelect() {
        return new ArrayList<>(Arrays.asList(Action.SELECT_WORKER, Action.MOVE, Action.BUILD));
    }

    /**
     * Executes a movement {@link Action action}
     *
     * @param worker   selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        movementFlag = true;
        super.executeMove(worker, position);
    }

    /**
     * Reset all the flags
     */
    @Override
    public void clear() {
        setEvent(false);
        movementFlag = false;
    }
}
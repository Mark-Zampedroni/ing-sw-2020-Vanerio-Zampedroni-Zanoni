package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rules for a player with Prometheus as God
 */
public class PrometheusRules extends EventRules implements Serializable {

    private static final long serialVersionUID = 6128545018299786443L;
    private boolean movementFlag=false;

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
        } else {setEvent(false);}
        super.executeBuild(position);
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is {@code true}
     * and the flag setted in {@link #executeMove(Worker, Position) executeMove} method
     * is false the next action is {@link Action MOVE}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(getEvent() && !movementFlag) {
            actions.add(Action.MOVE);
            actions.remove(0); }
        return actions;
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action},
     * if the player build before moving adds the check on height
     *
     * @param worker worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        if (getEvent()) {Check.height(worker, position, 0, "Tile out of reach");} }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action selects} a {@link Worker worker},
     * adds a optional first build with different conditions
     *
     * @return list of {@link Action actions} that can be done after {@link Action selection}
     */
    @Override
    public List<Action> afterSelect() {
        return new ArrayList<>(Arrays.asList(Action.SELECT_WORKER, Action.MOVE, Action.BUILD)); }

    /**
     * Executes a movement {@link Action action}, sets the movement flag to {@code true}
     *
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        movementFlag=true;
        super.executeMove(worker,position);
    }

    /**
     * Reset all the flags
     */
    @Override
    public void clear() {
        setEvent(false);
        movementFlag=false;
    }
}
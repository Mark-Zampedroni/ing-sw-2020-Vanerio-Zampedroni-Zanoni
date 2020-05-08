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
 * Rules for a player with Artemis as God
 */
public class ArtemisRules extends EventRules implements Serializable {

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action moved} a {@link Worker worker},
     * if it is the first {@link Action MOVE} action adds an additional {@link Action MOVE} action
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    @Override
    public List<Action> afterMove() {
        List<Action> actions = super.afterMove();
        if(!getEvent()) { actions.add(Action.MOVE); }
        return actions;
    }

    /**
     * Executes a movement {@link Action action}, calls the {@link #setPos(Position) setPos} method with the
     * {@link Worker worker} {@link Position oldPosition} argument, and the {@link #setEvent(boolean) setEvent} method with {@code true} argument
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        setEvent(true);
        setPos(worker.getPosition());
        super.executeMove(worker, position);
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
        Check.oldPosition(worker,position);}

}
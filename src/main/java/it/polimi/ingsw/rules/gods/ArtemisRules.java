package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

/**
 * Rules for a player with Artemis as God
 */
public class ArtemisRules extends EventRules {

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action moved} a {@link Worker worker},
     * if is the first {@link Action MOVE} action adds an additional {@link Action MOVE} action
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
     * {@link Worker worker} {@link Position oldPosition}, and the {@link #setEvent(boolean) setEvent} method with {@code true} argument
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

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.oldPosition(worker,position);}

}
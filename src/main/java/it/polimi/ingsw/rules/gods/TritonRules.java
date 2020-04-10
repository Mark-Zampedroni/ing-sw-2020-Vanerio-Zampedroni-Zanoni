package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

/**
 * Rules for a player with Triton as God
 */
public class TritonRules extends EventRules {

    /**
     * Executes a movement {@link Action action}, if the movement is on a boundary tile calls the {@link #setEvent setEvent} with {@code true}
     * argument else with {@code false} argument
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        super.executeMove(worker, position);
        setEvent(position.isBoundary());
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action moved} a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is true add a optional {@link Action MOVE}
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    @Override
    public List<Action> afterMove() {
        List<Action> actions = super.afterMove();
        if(getEvent()) { actions.add(Action.MOVE); }
        return actions;
    }

}

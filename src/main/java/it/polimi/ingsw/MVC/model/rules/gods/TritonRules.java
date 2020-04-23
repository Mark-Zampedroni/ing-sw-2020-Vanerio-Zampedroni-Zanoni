package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.EventRules;

import java.io.Serializable;
import java.util.List;

/**
 * Rules for a player with Triton as God
 */
public class TritonRules extends EventRules implements Serializable {

    /**
     * Executes a movement {@link Action action}, if the movement is on a boundary tile calls the {@link #setEvent setEvent} with {@code true}
     * argument else with {@code false} argument
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        setEvent(position.isBoundary());
        super.executeMove(worker, position);
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.MVC.model.player.Player player}
     * {@link Action moved} a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is true add a optional {@link Action MOVE} action
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

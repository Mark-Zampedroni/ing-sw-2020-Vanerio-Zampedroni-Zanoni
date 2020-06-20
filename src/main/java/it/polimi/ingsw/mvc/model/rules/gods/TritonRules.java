package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.EventRules;

import java.io.Serializable;
import java.util.List;

/**
 * Rules for a player with Triton as God
 */
public class TritonRules extends EventRules implements Serializable {

    private static final long serialVersionUID = -4240895679955654178L;

    /**
     * Executes a {@link Action movement}, if it's on a boundary tile triggers the god's event
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
     * Returns a list of possible actions after a {@link Action move}, if the movement was to a boundary tile
     * adds {@link Action move} to the list
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    @Override
    public List<Action> afterMove() {
        List<Action> actions = super.afterMove();
        if(getEvent()) {
            actions.add(Action.MOVE);
        }
        return actions;
    }

}

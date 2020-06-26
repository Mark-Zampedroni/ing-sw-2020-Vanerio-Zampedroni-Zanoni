package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Actions that a {@link it.polimi.ingsw.mvc.model.player.Player player} can perform during his turn
 */
public enum Action implements Serializable {
    END_TURN, SELECT_WORKER, MOVE, BUILD, SPECIAL_POWER, WIN, ADD_WORKER;

    /**
     * Return a list with the actions that don't require position
     */
    public static List<Action> getNullPosActions() {
        return new ArrayList<>(Arrays.asList(Action.SPECIAL_POWER, Action.END_TURN, Action.WIN));
    }
}

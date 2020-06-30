package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Actions a Client can execute during its turn
 */
public enum Action implements Serializable {
    END_TURN, SELECT_WORKER, MOVE, BUILD, SPECIAL_POWER, WIN, ADD_WORKER;

    /**
     * Returns a list with the actions that don't require a position to be executed
     * @return the list of possible actions
     */
    public static List<Action> getNullPosActions() {
        return new ArrayList<>(Arrays.asList(Action.SPECIAL_POWER, Action.END_TURN, Action.WIN));
    }
}

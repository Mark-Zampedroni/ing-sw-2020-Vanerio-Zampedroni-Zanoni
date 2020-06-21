package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Actions that the {@link it.polimi.ingsw.mvc.model.player.Player players} can perform during their turn
 */
public enum Action implements Serializable {
    SELECT_WORKER, MOVE, BUILD, SPECIAL_POWER, END_TURN, WIN, ADD_WORKER;

    public static List<Action> getNullPosActions() {
        return new ArrayList<>(Arrays.asList(Action.SPECIAL_POWER, Action.END_TURN, Action.WIN));
    }
}

package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;

/**
 * Actions that the {@link it.polimi.ingsw.MVC.model.player.Player players} can perform during their turn
 */
public enum Action implements Serializable {
    SELECT_WORKER, MOVE, BUILD, END_TURN, WIN, ADD_WORKER
}

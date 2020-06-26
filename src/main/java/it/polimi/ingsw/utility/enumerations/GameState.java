package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;

/**
 * Different phases of the game, used both on {@link it.polimi.ingsw.network.client.Client} and
 * {@link it.polimi.ingsw.mvc.controller.SessionController Controller} to tune the game flow
 */
public enum GameState implements Serializable {
    CONNECTION,
    PRE_LOBBY,
    LOGIN,
    LOBBY,
    CHALLENGER_SELECTION,
    GOD_SELECTION,
    STARTER_SELECTION,
    GAME,
    END_GAME
}

package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;

/**
 * Represents the different phases of the game, from the connection to the end_game
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

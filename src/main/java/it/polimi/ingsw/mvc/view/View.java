package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.util.List;
import java.util.Map;

/**
 * Interface of methods used to handle the inputs and outputs independently of the implementation.
 */
public interface View {

    /**
     * Shows the queue position before joining the Lobby
     * @param text queue position
     */
    void showQueueInfo(String text);

    /* PRE-LOBBY */
    /**
     * Requests to choose between 2 or 3 players
     */
    void requestNumberOfPlayers();

    /* LOBBY */
    /**
     * Shows the lobby
     * @param availableColors list of available {@link Colors Colors}
     */
    void showLobby(List<Colors> availableColors);

    /**
     * Requests to choose the Client name and color
     */
    void requestLogin();

    /* CHALLENGER GODS SELECTION */
    /**
     * Shows the gods selected by the challenger
     * @param chosenGods list containing the names of the {@link it.polimi.ingsw.utility.enumerations.Gods gods} selected
     */
    void updateChallengerGodSelection(List<String> chosenGods);

    /**
     * Requests to choose a god
     * @param chosenGods list containing the names of the {@link it.polimi.ingsw.utility.enumerations.Gods gods} already selected
     */
    void requestChallengerGod(List<String> chosenGods);

    /* PLAYERS GODS SELECTION */
    /**
     * Shows the god selected by each player and the player currently choosing
     * @param turnOwner name of the player choosing his god
     * @param choices map of player's names to {@link it.polimi.ingsw.utility.enumerations.Gods gods}' names, providing which player chose which god
     * @param chosenGods list containing the names of the {@link it.polimi.ingsw.utility.enumerations.Gods gods} available to be chosen
     */
    void updatePlayerGodSelection(String turnOwner, Map<String,String> choices, List<String> chosenGods);

    /**
     * Requests to choose the player's god
     * @param chosenGods list containing the names of the {@link it.polimi.ingsw.utility.enumerations.Gods gods} available to be chosen
     * @param choices map of player's names to {@link it.polimi.ingsw.utility.enumerations.Gods gods}' names, providing which player chose which god
     */
    void requestPlayerGod(List<String> chosenGods, Map<String,String> choices);

    /* STARTER PLAYER SELECTION */
    /**
     * Shows the {@link it.polimi.ingsw.utility.enumerations.Gods god} chosen by each player
     * @param choices map of player's names to {@link it.polimi.ingsw.utility.enumerations.Gods gods}' names, providing which player chose which god
     */
    void updateStarterPlayerSelection(Map<String,String> choices);

    /**
     * Requests to select the player who will take the first turn
     * @param choices map of player's names to {@link it.polimi.ingsw.utility.enumerations.Gods god} name, providing which player chose which god
     */
    void requestStarterPlayer(Map<String,String> choices);

    /* GAME */
    /**
     * Requests to select an action and its position
     * @param possibleActions map of the available actions to the available positions for the action
     * @param session dto that provides the current {@link it.polimi.ingsw.mvc.model.Session values} at the server side
     * @param colors map of player's names to the color chosen by the player
     * @param gods map of player's name to the god chosen by the player
     * @param specialPower {@code true} if the turn owner gods' special power is toggled as active
     */
    void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String,Colors> colors, Map<String,String> gods, boolean specialPower);

    /**
     * Shows the board, updating the tiles height and workers positions
     * @param session dto that provides the current {@link it.polimi.ingsw.mvc.model.Session values} at the server side
     * @param colors map of player's names to the color chosen by the player
     * @param gods map of player's name to the god chosen by the player
     */
    void showBoard(DtoSession session, Map<String,Colors> colors, Map<String,String> gods);

    /* PLAYER STATUS */
    /**
     * Shows that a player won
     * @param playerName name of the player who won
     */
    void showWin(String playerName);

    /**
     * Shows that a player lost
     * @param playerName name of the player who lost
     */
    void showLose(String playerName);

    /* CONNECTION */
    /**
     * Shows if the client is trying to reconnect/has reconnected to the server
     * @param isReconnecting {@code true} if the client is reconnecting, {@code false} if the client successfully reconnected
     */
    void showReconnection(boolean isReconnecting);

    /**
     * Shows if the client was purposely disconnected by the server
     * @param info reason of the disconnection
     */
    void showDisconnected(String info);

}

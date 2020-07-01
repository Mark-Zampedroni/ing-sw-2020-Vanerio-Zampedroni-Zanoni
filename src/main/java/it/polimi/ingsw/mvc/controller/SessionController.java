package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.mvc.controller.states.LobbyController;
import it.polimi.ingsw.mvc.controller.states.SelectionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.controller.states.TurnController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.utility.persistency.ReloadGame;
import it.polimi.ingsw.utility.persistency.SaveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller of the MVC.
 * Implements the "state" pattern, using one state for each game phase.
 */
public class SessionController implements Observer<Message> {

    private final Object viewLock = new Object();
    private final Session session;
    private final List<RemoteView> views;
    private GameState state;
    private StateController stateController;
    private int gameCapacity;
    private String turnOwner;
    private Logger log;

    /**
     * Creates the controller
     *
     * @param connections list of client in game
     * @param log         general logger of the server
     */
    public SessionController(List<ServerConnection> connections, Logger log) {
        views = new ArrayList<>();
        this.log = log;
        session = Session.getInstance();
        state = GameState.LOBBY;
        stateController = new LobbyController(this, views, log, connections);
    }

    /**
     * Creates the controller of the game after a restart of the server
     *
     * @param saveData information saved on disk
     * @param log general logger of the server
     * @param map map username to connection for the connections
     */
    public SessionController(Logger log, SaveData saveData, Map<String, ServerConnection> map) {
        log.info("[CONTROLLER] Reloading game data");
        views = new ArrayList<>();
        session = saveData.getSession();
        reloadValues(log, saveData);
        ReloadGame.reloadViews(this, map, views, state);
        stateController.reloadState(this, saveData, views, log);
        log.info("[CONTROLLER] Done with reload");
    }

    /**
     * Reloads all the values of the game reading a save on disk
     *
     * @param saveData information saved on disk
     * @param log logger where the reload progress will be stored
     */
    private void reloadValues(Logger log, SaveData saveData) {
        this.log = log;
        Session.loadInstance(session);
        state = saveData.getGameState();
        turnOwner = saveData.getTurnOwner();
        gameCapacity = saveData.getGameCapacity();
        stateController = saveData.getStateController();
    }

    /**
     * Getter for Session
     *
     * @return the current Session of the game
     */
    public Session getSession() {
        return session;
    }

    /**
     * Getter for the game state
     *
     * @return the state of the game
     */
    public GameState getState() {
        return state;
    }

    /**
     * Getter for turn owner
     *
     * @return the turn owner
     */
    public String getTurnOwner() {
        return turnOwner;
    }

    /**
     * Sets the name of the player who is the turn owner
     *
     * @param turnOwner the current turn owner
     */
    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
    }

    /**
     * Returns if the game is started or not
     *
     * @return {@code true} if the game si started
     */
    public boolean isGameStarted() {
        return (state != GameState.LOBBY);
    }

    /**
     * Changes the current state of the game
     *
     * @param state the new state of the game
     */
    public void switchState(GameState state) {
        this.state = state;
        switch (state) {
            case GOD_SELECTION:
                stateController = new SelectionController(this, views, log);
                break;
            case GAME:
                assignFirstDTOSession();
                stateController = new TurnController(this, views, log);
                break;
            default:
                log.severe(() -> "[CONTROLLER] Tried to switch to state " + state + ", but the Server doesn't support it");
                // Altri stati
        }
        synchronized (viewLock) {
            sendStateUpdate();
        }
    }

    /**
     * Updates the clients on the state change
     */
    private void sendStateUpdate() {
        stateController.notifyMessage(new StateUpdateMessage(MessageType.STATE_UPDATE, "SERVER", "New state", state, "ALL"));
    }

    /**
     * Update of the situation according to the state of the game
     */
    public void sendUpdate() {
        stateController.sendUpdate();
    }

    /**
     * Adds a connection to the current game
     *
     * @param connection the connection that identifies a client
     */
    public void addUnregisteredView(ServerConnection connection) {
        synchronized (viewLock) {
            stateController.addUnregisteredView(connection);
        }
    }

    /**
     * Registers a player and adds it to the game
     *
     * @param username the name of the player
     * @param color    the color of the player
     * @param view     the view associated to the player
     */
    public void addPlayer(String username, Colors color, RemoteView view) {
        synchronized (viewLock) {
            stateController.addPlayer(username, color, view);
        }
    }

    /**
     * Removes a player from the game
     *
     * @param username the name of the player
     */
    public void removePlayer(String username) {
        synchronized (viewLock) {
            stateController.removePlayer(username);
        }
    }

    /**
     * Returns the available colors
     *
     * @return the list of the not chosen colors
     */
    public List<Colors> getFreeColors() {
        return stateController.getFreeColors();
    }

    /**
     * Notify the arrive of a message and elaborates its content, how to manage is different for different game states
     *
     * @param message the message received from the client
     */
    public void update(Message message) {
        synchronized (viewLock) {
            saveGame(message, false);
            log.info(() -> "[CONTROLLER] Received RemoteView message: " + message);
            stateController.parseMessage(message);
        }
    }

    /**
     * Getter for the game capacity
     *
     * @return the capacity of the game (as number of players)
     */
    public int getGameCapacity() {
        return gameCapacity;
    }

    /**
     * Setter for game capacity
     *
     * @param capacity desired capacity for the current game (as number of players)
     */
    public void setGameCapacity(int capacity) {
        gameCapacity = capacity;
    }

    /**
     * Getter for the players in game
     *
     * @return the list of the players in game
     */
    public List<Player> getPlayers() {
        return session.getPlayers();
    }

    /**
     * Sends the first DTO-Session to all the clients
     */
    private void assignFirstDTOSession() {
        DtoSession dto = new DtoSession(session);
        views.forEach(v -> v.getFirstDtoSession(dto));
    }

    /**
     * Saves the game and the last message
     *
     * @param message          last message received by a client or sent by the server
     * @param isParseCompleted indicates if the elaboration of message is completed
     */
    public void saveGame(Message message, boolean isParseCompleted) {
        if (state != GameState.LOBBY) {
            SaveData.saveGame(this, stateController, message, isParseCompleted, log);
        }
    }

    /**
     * Restarts the server and the game, deletes the save file
     */
    public void restartGame() {
        try {
            ReloadGame.clearSavedFile();
        } catch (IOException e) {
            log.warning(() -> "[CONTROLLER] File ");
        } finally {
            log.info(() -> "[CONTROLLER] Game restarted from scratch");
            Session.getInstance().clear();
            Server.restartSession();
        }
    }

}

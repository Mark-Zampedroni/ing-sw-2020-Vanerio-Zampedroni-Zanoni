package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.mvc.controller.states.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.utility.persistency.ReloadGame;
import it.polimi.ingsw.utility.persistency.SavedData;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.util.*;
import java.util.logging.Logger;

/**
 * Controller used during all the game phases for shared information between the different states,
 * it also manage the calls to the different methods inside the different states controllers
 */
public class SessionController implements Observer<Message>  {

    private GameState state;
    private StateController stateController;
    private int gameCapacity;

    private String turnOwner;
    private final Object viewLock = new Object();
    private static Logger LOG;
    private final Session session;
    private final List<RemoteView> views;

    /**
     * Creates the controller of the game
     *
     * @param connections list of client in game
     * @param LOG general logger of the server
     */
    public SessionController(List<ServerConnection> connections, Logger LOG) {
        views = new ArrayList<>();
        SessionController.LOG = LOG;
        session = Session.getInstance();
        state = GameState.LOBBY;
        stateController = new LobbyController(this, views, LOG, connections);
    }

    /**
     * Creates the controller of the game after a restart of the server
     *
     * @param savedData information saved on disk
     * @param LOG general logger of the server
     * @param map the map of connections and username of the clients
     */
    public SessionController (Logger LOG, SavedData savedData, Map<String, ServerConnection> map) {
        LOG.info("[CONTROLLER] Reloading game data");
        views = new ArrayList<>();
        session = savedData.getSession();
        reloadValues(LOG, savedData, map);
        ReloadGame.reloadViews(this,map,views,state);
        stateController.reloadState(this,savedData,views,LOG);
        LOG.info("[CONTROLLER] Done with reload");
    }

    /**
     * Reloads all the values of the game before the failure
     *
     * @param savedData information saved on disk
     * @param LOG general logger of the server
     * @param map the map of connections and username of the clients
     */
    private void reloadValues(Logger LOG, SavedData savedData, Map<String, ServerConnection> map) {
        SessionController.LOG = LOG;
        session.loadInstance();
        state = savedData.getGameState();
        turnOwner = savedData.getTurnOwner();
        gameCapacity = savedData.getGameCapacity();
        stateController = savedData.getStateController();
    }

    /**
     * Getter for Session
     *
     * @return the current Session of the game
     */
    public Session getSession() { return session; }

    /**
     * Getter for the game state
     *
     * @return the state of the game
     */
    public GameState getState() { return state; }

    /**
     * Sets the turn owner among the players
     *
     * @param turnOwner the current turn owner
     */
    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
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
     * Returns if the game is started or not
     *
     * @return {@code true} if the game si started
     */
    public boolean isGameStarted() { return (state != GameState.LOBBY); }

    /**
     * Changes the current state of the game in a new state
     *
     * @param state the new state of the game
     */
    public void switchState(GameState state) {
        this.state = state;
        switch(state) {
            case GOD_SELECTION:
                stateController = new SelectionController(this, views, LOG);
                break;
            case GAME:
                assignFirstDTOSession();
                stateController = new TurnController(this, views, LOG);
                break;
            default:
                LOG.severe("[CONTROLLER] Tried to switch to state "+state+", but the Server doesn't support it");
                // Altri stati
        }
        synchronized(viewLock) {
            sendStateUpdate();
        }
        LOG.info("[CONTROLLER] Server switch to new state: "+state); // TEST
    }

    /**
     * Update of the changing state for the clients, broadcast
     *
     */
    private void sendStateUpdate() {
        stateController.notifyMessage(new StateUpdateMessage(MessageType.STATE_UPDATE,"SERVER","New state", state, "ALL"));
    }

    /**
     * Update of the situation according to the state of the game
     *
     */
    public void sendUpdate() {
        stateController.sendUpdate();
    }

    /**
     *Adds a client to the current game
     *
     * @param connection the connection that identifies a client
     */
    public void addUnregisteredView(ServerConnection connection) {
        synchronized(viewLock) {
            stateController.addUnregisteredView(connection);
        }
    }

    /**
     * Register a player and add it to the game
     *
     * @param username the name of the player
     * @param color the color of the player
     * @param view the view associated to the player
     */
    public void addPlayer(String username, Colors color, RemoteView view) {
        synchronized(viewLock) {
            stateController.addPlayer(username, color, view);
        }
    }

    /**
     * Remove a player from the game
     *
     * @param username the name of the player
     */
    public void removePlayer(String username) {
        synchronized(viewLock) {
            stateController.removePlayer(username);
        }
    }

    /**
     * It returns the available color
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
        synchronized(viewLock) {
            saveGame(message,false);
            LOG.info("SessionController received RemoteView message: "+message);
            stateController.parseMessage(message);
        }
    }

    /**
     * Setter for game capacity
     *
     * @param capacity desired capacity for the current game
     */
    public void setGameCapacity(int capacity) {
        gameCapacity = capacity;
    }

    /**
     * Getter for the game capacity
     *
     * @return the capacity of the game
     */
    public int getGameCapacity() {
        return gameCapacity;
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
     *
     */
    private void assignFirstDTOSession() {
        DtoSession dto = new DtoSession(session);
        views.forEach(v -> v.getFirstDTOSession(dto));
    }

    /**
     * Saves the game and the last message
     *
     * @param message last message received by a client or sent by the server
     * @param isParseCompleted indicates if the elaboration of message is completed
     */
    public void saveGame(Message message, boolean isParseCompleted) {
        if(state != GameState.LOBBY) {
            SavedData.saveGame(this, stateController, message, isParseCompleted);
        }
    }

    /**
     * Restarts the server and the game, clear the file
     *
     */
    public void restartGame() {
        boolean isDeleted = ReloadGame.clearSavedFile();
        LOG.info("[CONTROLLER] Game restarted from scratch, old save file deleted: "+isDeleted);
        Session.getInstance().clear();
        Server.restartSession();
    }

}

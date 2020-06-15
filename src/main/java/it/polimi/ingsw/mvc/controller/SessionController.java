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

public class SessionController implements Observer<Message>  {

    private GameState state;
    private StateController stateController;
    private int gameCapacity;

    private String turnOwner;

    private final Object viewLock = new Object();

    private static Logger LOG;

    private final Session session;

    private final List<RemoteView> views;

    public SessionController(List<ServerConnection> connections, Logger LOG) {
        views = new ArrayList<>();
        SessionController.LOG = LOG;
        session = Session.getInstance();
        state = GameState.LOBBY;
        stateController = new LobbyController(this, views, LOG, connections);
    }

    public SessionController (Logger LOG, SavedData savedData, Map<String, ServerConnection> map) {
        LOG.info("[CONTROLLER] Reloading game data");
        views = new ArrayList<>();
        session = savedData.getSession();
        reloadValues(LOG, savedData, map);
        ReloadGame.reloadViews(this,map,views,state);
        stateController.reloadState(this,savedData,views,LOG);
        LOG.info("[CONTROLLER] Done with reload");
    }

    private void reloadValues(Logger LOG, SavedData savedData, Map<String, ServerConnection> map) {
        SessionController.LOG = LOG;
        session.loadInstance();
        state = savedData.getGameState();
        turnOwner = savedData.getTurnOwner();
        gameCapacity = savedData.getGameCapacity();
        stateController = savedData.getStateController();
    }

    public Session getSession() { return session; }

    public GameState getState() { return state; }

    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public boolean isGameStarted() { return (state != GameState.LOBBY); }

    // Cambia stato
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

    // Update ai client per notificarli che è cambiato lo stato, uguale per tutti gli stati
    private void sendStateUpdate() {
        stateController.notifyMessage(new StateUpdateMessage(MessageType.STATE_UPDATE,"SERVER","New state", state, "ALL"));
    }

    // Update sulla situazione in base allo stato in cui si trova
    public void sendUpdate() {
        stateController.sendUpdate();
    }

    public void addUnregisteredView(ServerConnection connection) {
        synchronized(viewLock) {
            stateController.addUnregisteredView(connection);
        }
    }

    // Registra un player e lo aggiunge al gioco
    public void addPlayer(String username, Colors color, RemoteView view) {
        synchronized(viewLock) {
            stateController.addPlayer(username, color, view);
        }
    }

    // Rimuove un player registrato e lo de-registra
    public void removePlayer(String username) {
        synchronized(viewLock) {
            stateController.removePlayer(username);
        }
    }

    public List<Colors> getFreeColors() {
        return stateController.getFreeColors();
    }

    // Notifica da parte di una view che è arrivato un messaggio, come viene gestito cambia in base allo stato (parseMessage)
    public void update(Message message) {
        synchronized(viewLock) {
            saveGame(message,false);
            LOG.info("SessionController received RemoteView message: "+message);
            stateController.parseMessage(message);
        }
    }

    public void setGameCapacity(int capacity) {
        gameCapacity = capacity;
    }

    public int getGameCapacity() {
        return gameCapacity;
    }

    public List<Player> getPlayers() {
        return session.getPlayers();
    }

    private void assignFirstDTOSession() {
        DtoSession dto = new DtoSession(session);
        views.forEach(v -> v.getFirstDTOSession(dto));
    }

    public void saveGame(Message message, boolean isParseCompleted) {
        if(state != GameState.LOBBY) {
            SavedData.saveGame(this, stateController, message, isParseCompleted);
        }
    }

    public void restartGame() {
        boolean isDeleted = ReloadGame.clearSavedFile();
        LOG.info("[CONTROLLER] Game restarted from scratch, old save file deleted: "+isDeleted);
        Session.getInstance().clear();
        Server.restartSession();
    }

}

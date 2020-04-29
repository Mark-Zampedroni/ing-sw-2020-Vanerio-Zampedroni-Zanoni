package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.controller.states.LobbyController;
import it.polimi.ingsw.MVC.controller.states.SelectionController;
import it.polimi.ingsw.MVC.controller.states.StateController;
import it.polimi.ingsw.MVC.controller.states.TurnController;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.MVC.view.RemoteView;

import java.util.*;
import java.util.logging.Logger;

public class SessionController implements Observer<Message>  {

    private GameState state;
    private StateController stateController;
    private int gameCapacity;

    private String turnOwner;

    private final Object viewLock = new Object();

    private static Logger LOG;

    TurnController table;
    Session session;

    private final Map<String, RemoteView> views = new HashMap<>();

    public SessionController(List<ServerConnection> connections, Logger LOG) {
        SessionController.LOG = LOG;
        session = Session.getInstance();
        state = GameState.LOBBY;
        stateController = new LobbyController(this, views, LOG, connections);
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
                //stateController = new TurnController
                System.out.println("Inizia partita!");
                break;
            default:
                LOG.severe("Tried to switch to state "+state+", but the Server doesn't support it yet");
                // Altri stati
        }
        synchronized(viewLock) {
            sendStateUpdate(state); // Notify new state to clients
            //if(this.state != GameState.LOBBY) stateController.sendUpdate(); // Update clients info
        }

        LOG.info("Server switch to new state: "+state); // TEST
    }

    public void tryNextState() {
        stateController.tryNextState();
    }

    // Update ai client per notificarli che è cambiato lo stato, uguale per tutti gli stati
    public void sendStateUpdate(GameState newState) {
        stateController.sendBroadcastMessage(new StateUpdateMessage(MessageType.STATE_UPDATE,"SERVER","Nuovo stato", newState));
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

}

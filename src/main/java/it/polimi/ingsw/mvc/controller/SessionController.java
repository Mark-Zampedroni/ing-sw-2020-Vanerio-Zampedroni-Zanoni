package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.mvc.controller.states.*;
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
import it.polimi.ingsw.utility.serialization.dto.DtoSession;

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

    private final Map<String, RemoteView> views = new HashMap<>();

    /*
    public SessionController(List<ServerConnection> connections, Logger LOG, Session session, GameState state, StateController stateController){
        SessionController.LOG = LOG;
        this.session = session;
        this.state = state;
        this.stateController = new DiffLobbyController(stateController, this, views, LOG, connections );
    }*/

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

    public StateController getStateController() {return stateController;}

    /*
    public void diffSwitchState(StateController stateController) {
        if (stateController instanceof TurnController){
            this.state = GameState.GAME;}
        else {
            this.state = GameState.GOD_SELECTION;
        }
        this.stateController = stateController;
        stateController.setController(this);
    }*/

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
                LOG.severe("Tried to switch to state "+state+", but the Server doesn't support it yet");
                // Altri stati
        }
        synchronized(viewLock) {
            sendStateUpdate();
        }
        LOG.info("Server switch to new state: "+state); // TEST
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

    /*
    public void addPlayer(String username, RemoteView view) {
        synchronized(viewLock) {
            Colors color=null;

        for(Player p : session.getPlayers()) {
            if (p.getUsername().equals(username)){
                color=p.getColor();
            }
        }
            stateController.addPlayer(username, color, view);
        }
    }*/

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

    private void assignFirstDTOSession() {
        DtoSession dto = new DtoSession(session);
        views.values().forEach(v -> v.getFirstDTOSession(dto));
    }

}

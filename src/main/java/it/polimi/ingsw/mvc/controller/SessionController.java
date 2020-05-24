package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.mvc.controller.states.*;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.network.messages.FlagMessage;
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
import it.polimi.ingsw.utility.persistency.SaveGame;
import it.polimi.ingsw.utility.persistency.SavedDataClass;
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

    private final List<RemoteView> views;


    // Va spostato come metodo dentro ad una delle classi di caricamento.
    // qui diventa solo ReloadGame.load(this); con this (SessionController) che ha tutti gli attributi da ricaricare protected
    // mettiamo ReloadGame in questo stesso package e nel metodo assegnamo a this i valori dei vari campi
    public SessionController (Logger LOG,  SavedDataClass savedData,  Map<String, ServerConnection> map) {
        SessionController.LOG = LOG;
        session = savedData.getSession();
        session.loadInstance();
        state = savedData.getGameState();
        turnOwner = savedData.getTurnOwner();
        gameCapacity = savedData.getGameCapacity();
        stateController = savedData.getStateController();
        views = new ArrayList<>();

        System.out.println("Reloaded data"); // TEST <<-------

        // >> Dentro a ReloadGame metodo privato a parte per sto pezzo, è la logica caricamento views
        for (String name : map.keySet()){
            System.out.println("Creating view of "+name); // TEST <<-------
            map.get(name).putInLobby();
            RemoteView view = new RemoteView(map.get(name));
            view.register(name);
            views.add(view);
            if(state == GameState.GAME) { view.getFirstDTOSession(new DtoSession(savedData.getSession())); }
            view.addObserver(this);
            for (Player player : savedData.getSession().getPlayers()){
                player.getRules().addObserver(view);
            }
        }
        // <<

        System.out.println("All views created"); // TEST <<-------
        System.out.println("Session has state: "+state+", views: "+views);

        stateController.resetPreviousState(views, this, LOG);
        stateController.notifyMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, "Server", "Reconnected successfully",true, "ALL"));
        if(!savedData.getActionDone()) {
            LOG.info("Last message before save needs to be re-parsed, on it ...");
            stateController.parseMessage(savedData.getMessage());
        }
    }

    public SessionController(List<ServerConnection> connections, Logger LOG) {
        views = new ArrayList<>();
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
            System.out.println("Saving from sessionController"); // TEST <<-----
            SaveGame.saveGame(this, stateController, message, isParseCompleted);
        }
    }

}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.states.LobbyController;
import it.polimi.ingsw.controller.states.SelectionController;
import it.polimi.ingsw.controller.states.StateController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.StateUpdateMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;
import java.util.stream.Collectors;

public class SessionController implements Observer<Message>  {

    private GameState state;
    private StateController stateController;

    private final Object viewLock = new Object();

    TurnController table;
    Session session;

    private final List<ServerConnection> pendingConnections;
    private final Map<String, RemoteView> views = new HashMap<>();

    public SessionController(List<ServerConnection> pendingConnections) {
        this.pendingConnections = pendingConnections;
        session = Session.getInstance();
        switchState(GameState.LOBBY);
    }

    public List<Player> getPlayers() { return session.getPlayers(); }

    public Session getSession() { return session; }

    public GameState getState() { return state; }

    public List<Colors> getFreeColors() {
        return Arrays.asList(Colors.values())
                .stream()
                .filter(c -> !(session.getPlayers().stream().map(Player::getColor).collect(Collectors.toList())).contains(c))
                .collect(Collectors.toList());
    }

    public boolean isGameStarted() { return (state != GameState.LOBBY); }

    // Cambia stato
    public void switchState(GameState state) {
        this.state = state;
        switch(state) {
            case LOBBY:
                stateController = new LobbyController(this, views, pendingConnections);
                break;
            case GOD_SELECTION:
                stateController = new SelectionController(this, views);
                break;
            default:
                System.out.println("This state is not yet implemented or the Server doesn't use it");
                // Altri stati
        }
        synchronized(viewLock) { sendStateUpdate(); }
        System.out.println("\nNew state: "+state+"\n"); // TEST
    }

    // Update ai client per notificarli che è cambiato lo stato, uguale per tutti gli stati
    private void sendStateUpdate() {
        stateController.sendBroadcastMessage(new StateUpdateMessage(MessageType.STATE_UPDATE,"SERVER","Nuovo stato", state));
    }

    // Update sulla situazione in base allo stato in cui si trova
    public void sendUpdate() {
        stateController.sendUpdate();
    }

    // Registra un player e lo aggiunge al gioco
    public void addPlayer(String username, Colors color, RemoteView view) {
        synchronized(viewLock) {
            session.addPlayer(username, color);
            views.put(username, view);
            view.addObserver(this);
        }
    }

    // Rimuove un player registrato e lo de-registra
    public void removePlayer(String username) {
        synchronized(viewLock) {
            session.removePlayer(username);
            views.remove(username);
        }
    }

    // Notifica da parte di una view che è arrivato un messaggio, come viene gestito cambia in base allo stato (parseMessage)
    public void update(Message message) {
        synchronized(viewLock) {
            System.out.println("Stampato da SessionController: \n" + message + "\n"); // TEST
            stateController.parseMessage(message);
        }
    }

}

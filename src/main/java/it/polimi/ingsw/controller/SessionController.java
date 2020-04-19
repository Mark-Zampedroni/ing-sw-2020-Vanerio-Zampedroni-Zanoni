package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.StateUpdateMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class SessionController implements Observer<Message>  {

    private GameState state;

    private final Object viewLock = new Object();

    TableController table;
    Session session;
    String challenger;

    private final Map<String, Boolean> flagged = new HashMap<>();
    private final Map<String, RemoteView> views = new HashMap<>();
    private final List<Colors> freeColors = new ArrayList<>(Arrays.asList(Colors.values()));

    public SessionController() {
        table = new TableController(this);
        session = Session.getInstance();
        state = GameState.LOBBY;
    }

    public List<Player> getPlayers() { return session.getPlayers(); }

    public Session getSession() {
        return session;
    }

    public GameState getState() {
        return state;
    }

    public List<Colors> getFreeColors() { return freeColors; }

    public boolean isStarted() {
        return (state != GameState.LOBBY);
    }

    public void addPlayer(String username, Colors color, RemoteView view) {
        synchronized (viewLock) {
            freeColors.removeIf(c -> c == color);
            session.addPlayer(username, color);
            views.put(username, view);
            flagged.put(username, false);
            view.addObserver(this);
        }
    }

    public void removePlayer(String username) {
        synchronized (viewLock) {
            freeColors.add(session.getPlayerColor(username));
            session.removePlayer(username);
            views.remove(username);
            flagged.remove(username);
        }
    }

    public void update(Message message) {
        System.out.println("Stampato da SessionController: \n"+message+"\n"); // TEST
        switch(message.getType()) {
            case READY:
                parseReadyMessage((FlagMessage) message);
                break;
        }
        //views.get(message.getSender()).sendMessage(reply(message));
    }

    public void updateActions(String username, Message message) {
        views.get(username).updateActions(message);
    }

    public void sendAllMessage(Message message, List<ServerConnection> unregistered) {
        sendRegisteredMessage(message);
        synchronized (viewLock) {
            for (ServerConnection connection : unregistered) {
                connection.sendMessage(message);
            }
        }
    }

    public void sendRegisteredMessage(Message message) {
        synchronized (viewLock) {
            for (String player : views.keySet()) {
                views.get(player).sendMessage(message);
            }
        }
    }

    public void sendLobbyUpdate(List<ServerConnection> unregistered) {
        sendAllMessage(new LobbyUpdate("SERVER", "Update",
                        freeColors, session.getPlayers()), unregistered);

    }

    private void parseReadyMessage(FlagMessage message) {
        synchronized (viewLock) {
            if (state == GameState.LOBBY) {
                System.out.println("\nReady message: "+message+"\n");
                flagged.replace(message.getSender(), message.getFlag());
                if(views.keySet().size() > 1 && areAllReady()) {
                    setGameState(GameState.GOD_SELECTION);
                    Session.getInstance().getChallenger();

                }
            }
        }
    }

    private boolean areAllReady() {
        if(state == GameState.LOBBY) {
            for (String p : flagged.keySet()) {
                if(!flagged.get(p)) { return false; }
            }
        }
        return true;
    }

    private void setGameState(GameState state) {
        this.state = state;
        System.out.println("\nNew state: "+state+"\n");
        sendStateUpdate();
    }

    private void sendStateUpdate() {
        sendRegisteredMessage(new StateUpdateMessage(MessageType.STATE_UPDATE,"SERVER","Nuovo stato",state));
    }


    /*
    public void removePlayer(String username) {
        session.removePlayer(username);
    }*/

    // GESTIONE MESSAGGI

}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.game.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class SessionController implements Observer<ActionMessage>  {

    GameState state;

    TableController table;
    Session session;

    Map<String, RemoteView> views = new HashMap<>();
    private List<Colors> freeColors = new ArrayList<>(Arrays.asList(Colors.values()));

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
        return session.isStarted();
    }

    public void addPlayer(String username, Colors color, RemoteView view) {
        freeColors.removeIf(c -> c == color);
        session.addPlayer(username, color);
        views.put(username, view);
        view.addObserver(this);
    }

    public void removePlayer(String username) {
        freeColors.add(session.getPlayerColor(username));
        session.removePlayer(username);
        views.remove(username);
    }

    public void update(ActionMessage message) {
        System.out.println("Stampato da SessionController: \n"+message+"\n"); // TEST
        views.get(message.getSender()).parseMessage(reply(message));

    }

    public void updateActions(String username, Message message) {
        views.get(username).updateActions(message);
    }

    public Message reply(Message message) {
        return (new Message(MessageType.ACTION,"SERVER","Action request received"));
    }


    /*
    public void removePlayer(String username) {
        session.removePlayer(username);
    }*/

    // GESTIONE MESSAGGI

}

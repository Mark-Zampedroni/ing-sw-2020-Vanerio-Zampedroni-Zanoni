package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class SessionController implements Observer<ActionMessage>  {

    TableController table;
    List<Player> players;
    Session session;

    Map<String, RemoteView> views = new HashMap<>();
    private List<Colors> freeColors = new ArrayList<>(Arrays.asList(Colors.values()));

    public SessionController() {
        table = new TableController(this);
        session = Session.getInstance();
    }

    public List<Player> getPlayers() { return players; }

    public Session getSession() {
        return session;
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
        // GESTISCE MESSAGGIO CHE ARRIVA DA UNA VIRTUAL VIEW (controlla username messaggio per sapere chi)
        System.out.println("Stampato da SessionController: \n"+message+"\n"); // TEST
    }

    public void updateActions(String username, Message message) {
        views.get(username).updateActions(message);
    }

    /*
    public void removePlayer(String username) {
        session.removePlayer(username);
    }*/

    // GESTIONE MESSAGGI

}

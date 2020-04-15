package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class SessionController {

    TableController table;
    List<Player> players;
    Session session;

    public SessionController() {
        table = new TableController(this);
        session = Session.getInstance();
    }

    public List<Player> getPlayers() { return players; }

    public Session getSession() {
        return session;
    }

    public boolean isStarted() {
        return session.isStarted();
    }

    /*
    public void addPlayer(String username) {
        session.addPlayer(username);
    }

    public void removePlayer(String username) {
        session.removePlayer(username);
    }*/

    // GESTIONE MESSAGGI

}

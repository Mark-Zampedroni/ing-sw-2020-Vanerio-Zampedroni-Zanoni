package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class SessionController {

    TableController table;
    List<Player> players;

    public SessionController() {
        table = new TableController(this);
    }

    public List<Player> getPlayers() { return players; }

    // GESTIONE MESSAGGI

}

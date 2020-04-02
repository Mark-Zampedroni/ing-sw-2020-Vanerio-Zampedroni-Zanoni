package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class TableController {

    SessionController session;
    TurnController turn;

    public TableController(SessionController session) {
        this.session = session;
    }

    public void initTurn() {
        turn = new TurnController(session.getPlayers());
    }

}

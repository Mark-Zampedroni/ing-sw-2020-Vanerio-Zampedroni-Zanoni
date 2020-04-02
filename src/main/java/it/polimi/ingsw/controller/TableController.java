package it.polimi.ingsw.controller;

public class TableController {

    SessionController session;
    TurnController turn;

    public TableController(SessionController session) {
        this.session = session;
    }

    public void startTurns() {
        turn = new TurnController(session.getPlayers());
    }

}

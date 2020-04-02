package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.message.Reply;
import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.exceptions.actions.WrongActionException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    List<Player> players;
    List<Action> possibleActions;
    Player currentPlayer;
    Worker currentWorker;
    GodRules currentRules;
    ActionController actionControl;
    int currentIndex;

    public TurnController(List<Player> players) {
        this.players = players;
        initTurn();
    }

    public void initTurn() {
        currentPlayer = players.get(currentIndex);
        currentRules = currentPlayer.getRules();
        actionControl = new ActionController(currentPlayer);
        clearActions();
    }

    public Reply response(String msg) { return new Reply(); } // Da mettere in server

    private void clearActions() {
        possibleActions = new ArrayList<>();
        possibleActions.add(Action.SELECT_WORKER);
    }

    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        initTurn();
    }

    public void validateType(Action type) throws WrongActionException {
        if(!possibleActions.contains(type)) { throw new WrongActionException("You can't do that action"); }
    }

    public void executeAction(Worker worker, Action type) throws CantActException, WrongActionException {
        validateType(type);
        possibleActions = actionControl.act(worker,type);
    }

    public void executeAction(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        validateType(type);
        possibleActions = actionControl.act(worker,position,type);
    }

    public Reply selectWorker(Worker worker) {
        try {
            executeAction(worker, Action.SELECT_WORKER);
            currentWorker = worker;
            return response("Worker selected");
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    public Reply build(Position position) {
        try {
            executeAction(currentWorker, position, Action.BUILD);
            return response("Built in "+position);
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    public Reply move(Position position) {
        try {
            executeAction(currentWorker,position,Action.MOVE);
            return response("Worker moved to "+position);
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    public Reply endTurn() {
        try {
            validateType(Action.END_TURN);
            Reply reply = response("Turn passed");
            passTurn();
            return reply;
        } catch(WrongActionException e) { return response(e.getMessage()); }
    }

}


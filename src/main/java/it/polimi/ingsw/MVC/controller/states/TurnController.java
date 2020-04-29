package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.ActionController;
import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.MVC.model.rules.GodRules;

import java.util.ArrayList;
import java.util.List;

/* Controller della sequenza delle azioni, invoca i metodi di ActionController per richiederne l'esecuzione */
/* possibleActions = { WIN } => ha vinto ; possibleActions.isEmpty() => ha perso */
public class TurnController {

    List<Player> players;
    List<Action> possibleActions;

    Player currentPlayer;

    Worker currentWorker;

    GodRules currentRules;

    ActionController actionControl;
    SessionController controller;

    int currentIndex;

    public TurnController(SessionController controller) {
        this.controller = controller;
        this.players = controller.getPlayers();
        initTurn();
    }

    /*
        Initializes new turn environment
     */
    public void initTurn() {
        currentPlayer = players.get(currentIndex);
        currentRules = currentPlayer.getRules();
        currentRules.clear(); // Resets god flags
        actionControl = new ActionController(currentPlayer);
        clearActions();
    }

    /*
        Passes Turn to next Player in list
     */
    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        initTurn();
    }

    /*
        DUMMY CLASS / SERVER -> CLIENT REPLY TO REQUESTS
     */
    public Message response(String msg) { return new Message(MessageType.ACTION, "TEST", "test"); } // Da mettere in server

    /*
        Set possibleActions to first action default, [SELECT_WORKER]
     */
    private void clearActions() {
        possibleActions = new ArrayList<>();
        possibleActions.add(Action.SELECT_WORKER);
    }

    /*
        Throws exception if Action is not in possibleActions
     */
    public void validateType(Action type) throws WrongActionException {
        if(!possibleActions.contains(type)) { throw new WrongActionException("You can't do that action"); }
    }


    /*
        Deletes Actions with empty candidates List
     */
    public void fixPossibleActions() { // Checks only move and build actions
        actionControl.setCandidates(currentWorker,possibleActions);
        possibleActions.removeIf(action -> actionControl.getCandidates(action).isEmpty());
    }

    /*
        Executes Action [SELECT_WORKER] and returns following possible Actions as List
     */
    public void executeAction(Worker worker, Action type) throws CantActException, WrongActionException {
        validateType(type);
        possibleActions = actionControl.act(worker,type);
        currentWorker = worker;
        fixPossibleActions(); // Creates candidate lists in ActionController and filters possibleActions list
    }

    /*
        Executes Action [ADD_WORKER] and returns following possible Actions as List
     */
    public void executeAction(Position position, Action type) throws CantActException, WrongActionException {
        validateType(type);
        possibleActions = actionControl.act(position, type);
        fixPossibleActions();
    }

    /*
        Executes Action [BUILD/MOVE] and returns following possible Actions as List
    */
    public void executeAction(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        validateType(type);
        possibleActions = actionControl.act(worker, position, type);
        fixPossibleActions(); // Creates candidate lists in ActionController and filters possibleActions list
    }

    /*
        Reply to [SELECT_WORKER] request
     */
    public Message selectWorker(Worker worker) {
        try {
            executeAction(worker, Action.SELECT_WORKER);
            currentWorker = worker;
            return response("Worker selected");
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    /*
        Reply to [ADD_WORKER] request
     */
    public Message addWorker(Position position) {
        try {
            executeAction(position, Action.ADD_WORKER);
            return response("Worker added to "+position);
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    /*
        Reply to [BUILD] request
     */
    public Message build(Position position) {
        try {
            executeAction(currentWorker, position, Action.BUILD);
            return response("Built in "+position);
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    /*
        Reply to [MOVE] request
     */
    public Message move(Position position) {
        try {
            executeAction(currentWorker, position, Action.MOVE);
            return response("Worker moved to "+position);
        } catch(CantActException | WrongActionException e) { return response(e.getMessage()); }
    }

    /*
        Reply to [END_TURN] request
     */
    public Message endTurn() {
        try {
            validateType(Action.END_TURN);
            Message reply = response("Turn passed");
            passTurn();
            return reply;
        } catch(WrongActionException e) { return response(e.getMessage()); }
    }

}


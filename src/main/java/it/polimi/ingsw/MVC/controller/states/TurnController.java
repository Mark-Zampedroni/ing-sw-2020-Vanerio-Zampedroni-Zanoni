package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.states.actionControl.ActionController;
import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.view.RemoteView;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/* Controller della sequenza delle azioni, invoca i metodi di ActionController per richiederne l'esecuzione */
/* possibleActions = { WIN } => ha vinto ; possibleActions.isEmpty() => ha perso */
public class TurnController extends StateController {

    List<Player> players;
    Map<Action, List<DTOposition>> possibleActions;

    Player currentPlayer;

    Worker currentWorker;

    ActionController actionControl;
    SessionController controller;

    int currentIndex;
    int turnCounter;

    public TurnController(SessionController controller, Map<String, RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        this.controller = controller;
        currentIndex = controller.getPlayers().indexOf(Session.getInstance().getPlayerByName(controller.getTurnOwner()));
        this.players = controller.getPlayers();
        initTurn();
    }

    public List<DTOposition> dtoConversion(List<Position> positions) {
        return positions.stream().map(DTOposition::new).collect(Collectors.toList());
    }

    @Override
    public void parseMessage(Message message) {
        if(message.getSender().equals(controller.getTurnOwner())) {
            switch (message.getType()) {
                case ACTION:
                    parseActionMessage((ActionMessage) message);
                    break;
            }
        } else {
            LOG.warning("A player who is not the turn owner sent a message : "+message);
        }
    }

    public void parseActionMessage(ActionMessage message) {
        if(possibleActions.containsKey(message.getAction()) &&
          (possibleActions.get(message.getAction()).stream().anyMatch(p -> message.getPosition().equals(p)) ||
           message.getAction() == Action.END_TURN)) {
            Position requestedPosition = null;
            if(message.getPosition() != null) { requestedPosition = new Position(message.getPosition().getX(), message.getPosition().getY()); }
            try {
                executeAction(requestedPosition, message.getAction());
            } catch(WrongActionException e) { System.out.println(Arrays.toString(e.getStackTrace())); }
        }
        else {
            LOG.warning("Client sent impossible action: "+message.getAction()+" in "+message.getPosition());
            views.get(message.getSender()).updateActions(possibleActions, message.getSender());
        }
    }

    @Override
    public void tryNextState() {/* END_GAME */}

    public void setCurrentWorker(Worker newCurrentWorker) {
        this.currentWorker = newCurrentWorker;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /*
        Initializes new turn environment
     */
    public void initTurn() {
        System.out.println("INIT TURN CALL");
        currentWorker = null;
        possibleActions = new HashMap<>();
        currentPlayer = players.get(currentIndex);
        controller.setTurnOwner(currentPlayer.getUsername());
        currentPlayer.getRules().clear();
        System.out.println("New player turn! "+currentPlayer); // TEST
        actionControl = new ActionController(this, currentPlayer);
        if(turnCounter < controller.getGameCapacity()) {
            possibleActions.put(Action.ADD_WORKER, getCandidates(Action.ADD_WORKER));
        }
        else {
            possibleActions.put(Action.SELECT_WORKER, getCandidates(Action.SELECT_WORKER));
        }
        sendUpdate();
        turnCounter++;
    }


    @Override
    public void sendUpdate() {
        fixPossibleActions();
        if(possibleActions.containsKey(Action.WIN)){
            System.out.println("\nWon\n");
            //controller.switchState(GameState.END_GAME);
        }
        else if(possibleActions.keySet().isEmpty()){
            currentPlayer.loss();
            System.out.println("\nLoser\n");
            passTurn();
            //RIMOZIONE EFFETTI DEI SUI NEMICI
        }
        else {
            views.values().forEach(v -> v.updateActions(possibleActions, currentPlayer.getUsername()));
        }
    }

    private List<DTOposition> getCandidates(Action type) {
        return dtoConversion(actionControl.getCandidates(currentWorker, type));
    }


    /*
        Passes Turn to next Player in list
     */
    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        if (players.get(currentIndex).isLoser()) {
            passTurn();
        } else {
            initTurn();
        }
    }

     /*
        Deletes Actions with empty candidates List
     */
    public void fixPossibleActions() { // Checks only move and build actions
        List<Action> impossibleActions = new ArrayList<>();
        for(Action action : possibleActions.keySet()) {
            if(possibleActions.get(action).isEmpty() && action != Action.END_TURN && action != Action.WIN) {
                impossibleActions.add(action);
            }
        }
        impossibleActions.forEach(action -> possibleActions.remove(action));
    }

     /*
        If possible, executes action on model
     */
    public void executeAction(Position position, Action type) throws WrongActionException {
        // WORKS ON PREVIOUS CANDIDATES
        if(type == Action.END_TURN) { passTurn(); }
        else {
            List<Action> candidates = actionControl.act(currentWorker, position, type);
            System.out.println("Candidates: " + candidates);
            // CREATES NEW CANDIDATES FOR NEXT ACTION
            possibleActions.clear();
            candidates.forEach(action -> possibleActions.put(action, getCandidates(action)));
            sendUpdate();
        }
    }



}


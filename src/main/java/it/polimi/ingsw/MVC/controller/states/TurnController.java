package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.states.actionControl.ActionController;
import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.view.RemoteView;
import it.polimi.ingsw.network.messages.game.ActionRequestMessage;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
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


    /**
     *
     *  COSE DA FARE:
     *
     *  1) AGGIUNGERE AL CLIENT DI RISPONDERE AL MESSAGGIO ACTION_REQUEST (MessageType)
     *
     *  2) RICEVUTA LA RISPOSTA CONTROLLARE CHE LA COPPIA AZIONE-POSIZIONE RICHIESTE SIANO NELLA LISTA DI QUELLE POSSIBILI
     *  3) CONTROLLARE ANCHE SU CLIENT CHE AZIONE-POSIZIONE SIA NELLA MAPPA ARRIVATA CON ACTION_REQUEST
     *
     *  4) ESEGUIRE L'AZIONE INVOCANDO executeAction(Position position, Action type) - (più comodo leggersi i DTO?)
     *  4.1) MAGARI SCRIVERE NEL DTOPOSITION UN .EQUALS(POSITION) PER FARE I CONFRONTI CROSS-CLASSE
     *
     *  5) TROVARE BUGO (non so se ci siano Bughi, sicuramente almeno qualcosa non funzionerà)
     *
     */

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
        // Arrivano i messaggini
    }

    @Override
    public void tryNextState() {/* END_GAME */}

    public Worker getCurrentWorker() {
        return currentWorker;
    }

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
        views.values().forEach(v -> v.updateActions(possibleActions, currentPlayer.getUsername()));
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
        initTurn();
    }

    /*
        DUMMY CLASS / SERVER -> CLIENT REPLY TO REQUESTS
     */
    public Message response(String msg) { return new Message(MessageType.ACTION, "TEST", "test"); } // Da mettere in server


    /*
        Throws exception if Action is not in possibleActions
     */
    public void validateType(Action type) throws WrongActionException {
        if(!possibleActions.containsKey(type)) { throw new WrongActionException("You can't do that action"); }
    }


    /*
        Deletes Actions with empty candidates List
     */
    public void fixPossibleActions() { // Checks only move and build actions
        List<Action> impossibleActions = new ArrayList<>();
        for(Action action : possibleActions.keySet()) {
            if(possibleActions.get(action).isEmpty()) {
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
        validateType(type); // Checks if type is in Map<Action>
        List<Action> candidates = actionControl.act(currentWorker,position,type);
        // CREATES NEW CANDIDATES FOR NEXT ACTION
        possibleActions.clear();
        candidates.forEach(action -> possibleActions.put(action, getCandidates(action)));
        fixPossibleActions(); // Remove Action candidates with no Position
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


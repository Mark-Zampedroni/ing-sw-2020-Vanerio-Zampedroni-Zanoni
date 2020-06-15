package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.states.actionControl.ActionController;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.mvc.model.rules.SpecialPower;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.dto.DtoPosition;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/* Controller della sequenza delle azioni, invoca i metodi di ActionController per richiederne l'esecuzione */
/* possibleActions = { WIN } => ha vinto ; possibleActions.isEmpty() => ha perso */
public class TurnController extends StateController implements Serializable {

    private static final long serialVersionUID = 3510638136440918631L;
    private final List<Player> players;
    private Map<Action, List<DtoPosition>> possibleActions;

    private Player currentPlayer;

    private Worker currentWorker;

    private ActionController actionControl;

    private boolean isSpecialPowerActive;

    private int currentIndex;
    private int turnCounter;
    private int counter;

    public TurnController(SessionController controller, List<RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        this.controller = controller;
        currentIndex = controller.getPlayers().indexOf(Session.getInstance().getPlayerByName(controller.getTurnOwner()));
        this.players = controller.getPlayers();
        initTurn();
    }

    @Override
    public void parseMessage(Message message) {
        if(message.getSender().equals(controller.getTurnOwner())) {
            if(message.getType() == MessageType.ACTION && !checkSpecialPowerRequest(message)) {
                parseActionMessage((ActionMessage) message);
            }
        } else {
            LOG.warning("A player who is not the turn owner sent a message : "+message);
        }
    }

    private boolean checkSpecialPowerRequest(Message message) {
        if(((ActionMessage) message).getAction() == Action.SPECIAL_POWER) {
            try {
                ((SpecialPower) currentPlayer.getRules()).toggleSpecialPower();
                isSpecialPowerActive = ((EventRules) currentPlayer.getRules()).getEvent();
                sendUpdate();
                return true;
            } catch (ClassCastException e) {
                LOG.warning("Player " + currentPlayer.getUsername() + " requested special power but " + currentPlayer.getGod() + " doesn't have one");
            }
        }
        return false;

    }

    public void parseActionMessage(ActionMessage message) {
        if(possibleActions.containsKey(message.getAction()) &&
          (possibleActions.get(message.getAction()).stream().anyMatch(p -> message.getPosition().equals(p)) ||
           message.getAction() == Action.END_TURN) && message.getAction() != Action.SPECIAL_POWER) {
            Position requestedPosition = null;
            if(message.getPosition() != null) {
                requestedPosition = new Position(message.getPosition().getX(), message.getPosition().getY());
            }
            try {
                executeAction(requestedPosition, message.getAction());
            } catch(WrongActionException e) { LOG.severe(Arrays.toString(e.getStackTrace())); }
        }
        else {
            LOG.warning("Client sent impossible action request: "+message.getAction()+" on "+message.getPosition());
            notifyBoardUpdate(possibleActions, message.getSender());
        }
    }

    @Override
    public void tryNextState() { controller.switchState(GameState.END_GAME); }

    public void setCurrentWorker(Worker newCurrentWorker) {
        this.currentWorker = newCurrentWorker;
    }

    /*
        Initializes new turn environment
     */
    private void initTurn() {
        initValues();
        if(turnCounter < controller.getGameCapacity()) {
            possibleActions.put(Action.ADD_WORKER, getCandidates(Action.ADD_WORKER));
        }
        else {
            possibleActions.put(Action.SELECT_WORKER, getCandidates(Action.SELECT_WORKER));
        }
        sendUpdate();
        turnCounter++;
    }

    private void initValues() {
        isSpecialPowerActive = false;
        currentWorker = null;
        possibleActions = new HashMap<>();
        currentPlayer = players.get(currentIndex);
        controller.setTurnOwner(currentPlayer.getUsername());
        currentPlayer.getRules().clear();
        actionControl = new ActionController(this, currentPlayer);
    }

    @Override
    public void sendUpdate() {
        removePreventedActions();
        if(possibleActions.containsKey(Action.WIN)){

            System.out.println(controller.getTurnOwner()+" won!"); // <--- TEST
            LOG.info(controller.getTurnOwner()+" won");

            notifyVictory(currentPlayer.getUsername());
            //waitTime();
            controller.restartGame();
            //a livello client viene dato il tasto per rigiocare che rimanda alla prima schermata, oppure si esce con tasto che chiude finestra
        }
        else if(possibleActions.keySet().isEmpty()) {

            // players non va toccato come lista! Altrimenti sminchia gli aggiornamenti;
            // basta settare .loss() e filtrare la lista per i giocatori non perdenti per controllare se qualcuno ha vinto.
            // Il turno lo passa già al prossimo non perdente.

            // Così dovrebbe essere ok, non ho testato

            System.out.println(controller.getTurnOwner()+" lost!"); // <--- TEST
            LOG.info(controller.getTurnOwner()+" lost");

            currentPlayer.loss();
            notifyLose(currentPlayer.getUsername());
            List<String> notLosers = players.stream().filter(p -> !p.isLoser()).map(Player::getUsername).collect(Collectors.toList());
            if(notLosers.size() == 1) {
                notifyVictory(notLosers.get(0));
            } else {
                passTurn();
            }
        }
        else {
            notifyBoardUpdate(possibleActions,currentPlayer.getUsername());
        }
    }

    private void notifyVictory(String winnerName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, winnerName, true);
        notifyMessage(message);
    }

    private void notifyLose(String loserName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, loserName, false);
        notifyMessage(message);
    }

    private List<DtoPosition> getCandidates(Action type) {
        return dtoConversion(actionControl.getCandidates(currentWorker, type));
    }

    public List<DtoPosition> dtoConversion(List<Position> positions) {
        return positions.stream().map(DtoPosition::new).collect(Collectors.toList());
    }

    /*
        Passes Turn to next Player in list
     */
    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        if (players.get(currentIndex).isLoser()) {
            counter++;
            passTurn();
        } else if(counter == players.size()-1){
            tryNextState();
        }
        else{
            counter = 0;
            initTurn();
        }
    }

     /*
        Deletes Actions with empty candidates List
     */
    private void removePreventedActions() {
        Map<Action, List<DtoPosition>> actionsWithCandidates = new HashMap<>();
        possibleActions.keySet().stream()
                .filter(a -> Action.getNullPosActions().contains(a) || !possibleActions.get(a).isEmpty())
                .forEach(a -> actionsWithCandidates.put(a,possibleActions.get(a)));
        possibleActions = actionsWithCandidates;
    }

     /*
        If possible, executes action on model
     */
    private void executeAction(Position position, Action type) throws WrongActionException {
        if(type == Action.END_TURN) { passTurn(); }
        else {
            List<Action> candidates = actionControl.act(currentWorker, position, type);
            possibleActions.clear();
            candidates.forEach(action -> possibleActions.put(action, getCandidates(action)));
            sendUpdate();
        }
    }

    private void notifyBoardUpdate(Map<Action, List<DtoPosition>> actionCandidates, String turnOwner) {
        controller.saveGame(null,true);
        views.forEach(w -> w.updateActions(actionCandidates,turnOwner,isSpecialPowerActive));
    }

}


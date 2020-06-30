package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.mvc.model.rules.SpecialPower;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller state used during the game. Manages the playing order of the players and the queries on the actions
 */

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

    /**
     * Creates the state for the "game" phase
     *
     * @param views      list of the {@link RemoteView remoteViews} of the players playing
     * @param log        logger where any controller event will be stored
     * @param controller controller of the MVC
     */
    public TurnController(SessionController controller, List<RemoteView> views, Logger log) {
        super(controller, views, log);
        this.controller = controller;
        currentIndex = controller.getPlayers().indexOf(Session.getInstance().getPlayerByName(controller.getTurnOwner()));
        this.players = controller.getPlayers();
        initTurn();
    }

    /**
     * Parse the view request
     *
     * @param message the message received by the client
     */
    @Override
    public void parseMessage(Message message) {
        if (message.getSender().equals(controller.getTurnOwner())) {
            if (message.getType() == MessageType.ACTION && !checkSpecialPowerRequest(message)) {
                parseActionMessage((ActionMessage) message);
            }
        } else {
            log.warning(() -> "A player who is not the turn owner sent a message : " + message);
        }
    }

    /**
     * Checks if the action is a {@link Action special_power}, if it is then it's toggled in the rules of the player
     * sending the message
     *
     * @param message message received
     * @return {@code true} if is a specialPower request
     */
    private boolean checkSpecialPowerRequest(Message message) {
        if (((ActionMessage) message).getAction() == Action.SPECIAL_POWER) {
            try {
                ((SpecialPower) currentPlayer.getRules()).toggleSpecialPower();
                isSpecialPowerActive = ((EventRules) currentPlayer.getRules()).getEvent();
                sendUpdate();
                return true;
            } catch (ClassCastException e) {
                log.warning("Player " + currentPlayer.getUsername() + " requested special power but " + currentPlayer.getGod() + " doesn't have one");
            }
        }
        return false;

    }

    /**
     * Reads the type of the {@link Action action} contained in the
     * {@link ActionMessage message} and executes it
     *
     * @param message the message containing the action
     */
    public void parseActionMessage(ActionMessage message) {
        if (possibleActions.containsKey(message.getAction()) &&
                (possibleActions.get(message.getAction()).stream().anyMatch(p -> message.getPosition().isSameAs(p)) ||
                        message.getAction() == Action.END_TURN) && message.getAction() != Action.SPECIAL_POWER) {
            Position requestedPosition = null;
            if (message.getPosition() != null)
                requestedPosition = new Position(message.getPosition().getX(), message.getPosition().getY());
            try {
                executeAction(requestedPosition, message.getAction());
            } catch (WrongActionException e) {
                log.severe(Arrays.toString(e.getStackTrace()));
            }
        } else {
            log.warning("Client sent impossible action request: " + message.getAction() + " on " + message.getPosition());
            notifyBoardUpdate(possibleActions, message.getSender());
        }
    }

    /**
     * Changes the state of the controller to "END_GAME"
     */
    @Override
    public void tryNextState() {
        controller.switchState(GameState.END_GAME);
    }

    /**
     * Sets the worker used for this turn
     *
     * @param newCurrentWorker worker currently used
     */
    public void setCurrentWorker(Worker newCurrentWorker) {
        this.currentWorker = newCurrentWorker;
    }

    /**
     * Initializes a new turn
     */
    private void initTurn() {
        initValues();
        if (turnCounter < controller.getGameCapacity()) {
            possibleActions.put(Action.ADD_WORKER, getCandidates(Action.ADD_WORKER));
        } else {
            possibleActions.put(Action.SELECT_WORKER, getCandidates(Action.SELECT_WORKER));
        }
        sendUpdate();
        turnCounter++;
    }

    /**
     * Initialize all the values
     */
    private void initValues() {
        isSpecialPowerActive = false;
        currentWorker = null;
        possibleActions = new EnumMap<>(Action.class);
        currentPlayer = players.get(currentIndex);
        controller.setTurnOwner(currentPlayer.getUsername());
        currentPlayer.getRules().clear();
        actionControl = new ActionController(this, currentPlayer);
    }

    /**
     * Updates all the players on the current turn owner and its possible actions.
     * If the player lost removes it from the Model and notifies the views.
     * If the player won notifies the views
     */
    @Override
    public void sendUpdate() {
        removePreventedActions();
        if (possibleActions.containsKey(Action.WIN)) handleWinner();
        else if (possibleActions.keySet().isEmpty()) handleLoser();
        else notifyBoardUpdate(possibleActions, currentPlayer.getUsername());
    }

    /**
     * Notifies the views of the current player victory, then
     * restarts the {@link SessionController controller} and disconnects the clients
     */
    private void handleWinner() {
        log.info(controller.getTurnOwner() + " won");
        notifyVictory(currentPlayer.getUsername());
        controller.restartGame();
    }

    /**
     * Notifies the views of the current player loss, removes it from the Model and
     * if only one player remains, notify its victory and restarts the {@link SessionController controller}.
     * Otherwise passes the turn to the next player
     */
    private void handleLoser() {
        log.info(controller.getTurnOwner() + " lost");
        currentPlayer.setLoser();
        notifyLose(currentPlayer.getUsername());
        List<String> notLosers = players.stream().filter(p -> !p.isLoser()).map(Player::getUsername).collect(Collectors.toList());
        if (notLosers.size() == 1) {
            notifyVictory(notLosers.get(0));
            controller.restartGame();
        } else {
            passTurn();
        }
    }

    /**
     * Sends the message containing the winner name
     *
     * @param winnerName name of the winner player
     */
    private void notifyVictory(String winnerName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, winnerName, true);
        notifyMessage(message);
    }

    /**
     * Sends the message containing the loser name
     *
     * @param loserName name of the loser
     */
    private void notifyLose(String loserName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, loserName, false);
        notifyMessage(message);
    }

    /**
     * Creates a list of possible {@link DtoPosition positions} for a specific action
     *
     * @return a list of DtoPosition where is possible to perform the action
     */
    private List<DtoPosition> getCandidates(Action type) {
        return dtoConversion(actionControl.getCandidates(currentWorker, type));
    }

    /**
     * Converts the list of positions to a list of {@link DtoPosition DtoPositions}
     *
     * @return a list of DtoPosition where is possible to perform the action
     */
    public List<DtoPosition> dtoConversion(List<Position> positions) {
        return positions.stream().map(DtoPosition::new).collect(Collectors.toList());
    }

    /**
     * Passes the turn to next player
     */
    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        if (players.get(currentIndex).isLoser()) passTurn();
        else initTurn();
    }

    /**
     * Blocks any action that doesn't have any position as viable candidate
     */
    private void removePreventedActions() {
        Map<Action, List<DtoPosition>> actionsWithCandidates = new EnumMap<>(Action.class);
        possibleActions.keySet().stream()
                .filter(a -> Action.getNullPosActions().contains(a) || !possibleActions.get(a).isEmpty())
                .forEach(a -> actionsWithCandidates.put(a, possibleActions.get(a)));
        possibleActions = actionsWithCandidates;
    }

    /**
     * If possible, executes the action through the {@link ActionController actionController}
     *
     * @param position position where the player wants perform the action
     * @param type     type of the action performed
     */
    private void executeAction(Position position, Action type) throws WrongActionException {
        if (type == Action.END_TURN) {
            passTurn();
        } else {
            List<Action> candidates = actionControl.act(currentWorker, position, type);
            possibleActions.clear();
            candidates.forEach(action -> possibleActions.put(action, getCandidates(action)));
            sendUpdate();
        }
    }

    /**
     * Saves the game and updates the views
     *
     * @param turnOwner        name of the current turn owner
     * @param actionCandidates possible actions and possible position for the actions
     */
    private void notifyBoardUpdate(Map<Action, List<DtoPosition>> actionCandidates, String turnOwner) {
        controller.saveGame(null, true);
        views.forEach(w -> w.updateActions(actionCandidates, turnOwner, isSpecialPowerActive));
    }

}


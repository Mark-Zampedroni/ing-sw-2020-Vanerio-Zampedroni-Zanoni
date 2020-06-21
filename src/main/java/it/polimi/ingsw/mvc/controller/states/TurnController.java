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
 * Controller used during the game, it manages the order of the player and the action performed
 * by them
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
    private int counter;

    /**
     * Creates the controller for the "game" phase
     *
     * @param views      list of the {@link RemoteView remoteViews} of the players for the updates
     * @param log        general logger of the server
     * @param controller general controller for the session
     */
    public TurnController(SessionController controller, List<RemoteView> views, Logger log) {
        super(controller, views, log);
        this.controller = controller;
        currentIndex = controller.getPlayers().indexOf(Session.getInstance().getPlayerByName(controller.getTurnOwner()));
        this.players = controller.getPlayers();
        initTurn();
    }

    /**
     * Method that reads the message and menage what to do depending on the content
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
     * Check if the action is a {@link Action special_power} action
     *
     * @param message where is contained the type of the message
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
     * Method that reads the type of the {@link Action action} contained in the
     * {@link ActionMessage message} and calls the method for perform that
     *
     * @param message the message containing the action
     */
    public void parseActionMessage(ActionMessage message) {
        if (possibleActions.containsKey(message.getAction()) &&
                (possibleActions.get(message.getAction()).stream().anyMatch(p -> message.getPosition().isSameAs(p)) ||
                        message.getAction() == Action.END_TURN) && message.getAction() != Action.SPECIAL_POWER) {
            Position requestedPosition = null;
            if (message.getPosition() != null) {
                requestedPosition = new Position(message.getPosition().getX(), message.getPosition().getY());
            }
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
     * Method that change the game state in "End_Game" in case of winning
     */
    @Override
    public void tryNextState() {
        controller.switchState(GameState.END_GAME);
    }

    /**
     * Setter for the worker currently used
     *
     * @param newCurrentWorker worker currently used
     */
    public void setCurrentWorker(Worker newCurrentWorker) {
        this.currentWorker = newCurrentWorker;
    }

    /**
     * Initialize the turn of the game when the previous player ends
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
     * Initialize all the values in the turnController
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
     * Sends to all the users the {@link Player player} name and the possible
     * actions, in case of winning or loosing manage what to do
     */
    @Override
    public void sendUpdate() {
        removePreventedActions();
        if (possibleActions.containsKey(Action.WIN)) {
            handleWinner();
        } else if (possibleActions.keySet().isEmpty()) {
            handleLoser();
        } else {
            notifyBoardUpdate(possibleActions, currentPlayer.getUsername());
        }
    }

    /**
     * Manages the case where there is a winner, notify all the players of that and
     * restart the {@link SessionController controller} for a new game
     */
    private void handleWinner() {
        log.info(controller.getTurnOwner() + " won");
        notifyVictory(currentPlayer.getUsername());
        controller.restartGame();
    }

    /**
     * Manages the case where there is a loser, notify all the players of that and,
     * if remains only one player, notify the victory and restart the {@link SessionController controller} for a new game
     * else remove the losing player and pass to the next player
     */
    private void handleLoser() {
        log.info(controller.getTurnOwner() + " lost");
        currentPlayer.loss();
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
     * Send the message containing the winner
     *
     * @param winnerName name of the winner player
     */
    private void notifyVictory(String winnerName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, winnerName, true);
        notifyMessage(message);
    }

    /**
     * Send the message containing the loser player
     *
     * @param loserName name of the loser
     */
    private void notifyLose(String loserName) {
        Message message = messageBuilder(MessageType.WIN_LOSE_UPDATE, loserName, false);
        notifyMessage(message);
    }

    /**
     * Creates a list of possible {@link DtoPosition position} for a specific action
     *
     * @return a list of DtoPosition where is possible to perform the action
     */
    private List<DtoPosition> getCandidates(Action type) {
        return dtoConversion(actionControl.getCandidates(currentWorker, type));
    }

    /**
     * Converts the list of position in a list of {@link DtoPosition DtoPosition}
     *
     * @return a list of DtoPosition where is possible to perform the action
     */
    public List<DtoPosition> dtoConversion(List<Position> positions) {
        return positions.stream().map(DtoPosition::new).collect(Collectors.toList());
    }

    /**
     * Passes Turn to next Player in list
     */
    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        if (players.get(currentIndex).isLoser()) {
            counter++;
            passTurn();
        } else if (counter == players.size() - 1) {
            tryNextState();
        } else {
            counter = 0;
            initTurn();
        }
    }

    /**
     * Deletes Actions with empty candidates List
     */
    private void removePreventedActions() {
        Map<Action, List<DtoPosition>> actionsWithCandidates = new EnumMap<>(Action.class);
        possibleActions.keySet().stream()
                .filter(a -> Action.getNullPosActions().contains(a) || !possibleActions.get(a).isEmpty())
                .forEach(a -> actionsWithCandidates.put(a, possibleActions.get(a)));
        possibleActions = actionsWithCandidates;
    }

    /**
     * If possible, executes action on model through the {@link ActionController actionController}
     *
     * @param position position where the player wants perform the action
     * @param type     of the action performed
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
     * Sends to all the player all the information about the turn
     *
     * @param turnOwner        player currently active in turn
     * @param actionCandidates possible actions and possible position for the actions
     */
    private void notifyBoardUpdate(Map<Action, List<DtoPosition>> actionCandidates, String turnOwner) {
        controller.saveGame(null, true);
        views.forEach(w -> w.updateActions(actionCandidates, turnOwner, isSpecialPowerActive));
    }

}


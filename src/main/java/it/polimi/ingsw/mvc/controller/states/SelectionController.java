package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.mvc.view.RemoteView;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller used during the phase when the players choose the god for the current game
 * and for the choice of the starter player
 */
public class SelectionController extends StateController implements Serializable {

    private static final long serialVersionUID = -1391531620876546345L;
    private final List<String> chosenGod = new ArrayList<>();
    private final Session session;
    private final Player challenger;
    private int turn;
    private GameState state;

    /**
     * Creates the controller for the selection phase
     *
     * @param views list of the {@link RemoteView remoteViews} of the players for the updates
     * @param LOG general logger of the server
     * @param controller general controller for the session
     */
    public SelectionController(SessionController controller, List<RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        session = Session.getInstance();
        challenger = session.getPlayerByName(session.getChallenger());
        state = GameState.CHALLENGER_SELECTION;
        notifyMessage(messageBuilder(MessageType.SELECTION_UPDATE, challenger.getUsername()));
        turn = session.getPlayers().indexOf(challenger);
        controller.setTurnOwner(challenger.getUsername());
    }

    /**
     * Reads messages from clients and manage what to do depending on the content
     *
     * @param message the message received
     */
    @Override
    public void parseMessage(Message message) {
        if(message.getSender().equals(controller.getTurnOwner())) {
            switch (message.getType()) {
                case GODS_UPDATE:
                    parseAddMessage(message);
                    break;
                case GODS_SELECTION_UPDATE:
                    parseGodChoiceMessage(message);
                    break;
                case STARTER_PLAYER:
                    parseStarterPlayerMessage(message);
                    break;
                default:
                    LOG.warning("Wrong message type : "+message);
            }
        }
        else {
            LOG.warning("A player who is not the turn owner sent a message : "+message);
        }
    }

    /**
     * Reads challenger selection message and add the gods chosen in the list of the gods of the game
     *
     * @param message the message received
     */
    private void parseAddMessage(Message message) {
        if (state == GameState.CHALLENGER_SELECTION){
            if (Gods.isValid(message.getInfo()) && !chosenGod.contains(message.getInfo())) {
                chosenGod.add(message.getInfo());
                notifyMessage(messageBuilder(MessageType.GODS_UPDATE, message.getInfo(), true));
                if (chosenGod.size() == controller.getPlayers().size()) {
                    state = GameState.GOD_SELECTION;
                    askNextSelection();
                }
            } else {
                notifyMessage(messageBuilder(MessageType.SELECTION_UPDATE,  challenger.getUsername()));
            }
        }
    }

    /**
     * Reads the choice of the player in the message and assign the god to the player
     *
     * @param message the message received
     */
    private void parseGodChoiceMessage(Message message) {
        if (state == GameState.GOD_SELECTION) {
            if (chosenGod.contains(message.getInfo())) {
                chosenGod.remove(message.getInfo());
                assignGod(message.getSender(), message.getInfo());
                if (!chosenGod.isEmpty()) {
                    notifyMessage(messageBuilder(MessageType.GODS_UPDATE, message.getInfo(), false));
                    askNextSelection();
                } else {
                    state = GameState.STARTER_SELECTION;
                    controller.setTurnOwner(challenger.getUsername());
                    notifyMessage(messageBuilder(MessageType.GODS_UPDATE, message.getInfo(), false));
                }
            } else {
                notifyMessage(messageBuilder(MessageType.GODS_SELECTION_UPDATE,"Turn notify", message.getSender()));
            }
        }
    }

    /**
     * Reads name of the starter player and pass to the next phase "Game"
     *
     * @param message the message received
     */
    private void parseStarterPlayerMessage(Message message) {
        if (state == GameState.STARTER_SELECTION) {
            for (Player player : controller.getPlayers()) {
                if (player.getUsername().equals(message.getInfo())) {
                    player.setStarter();
                    controller.setTurnOwner(message.getInfo());
                    tryNextState();
                }
            }
        }
    }

    /**
     * Changes the state of the controller to "Game"
     */
    public void tryNextState() {
        controller.switchState(GameState.GAME);
    }


    /**
     * Sends a message for ask the next choice of the god by the players
     */
    private void askNextSelection() {
        turn = (turn + 1) % controller.getGameCapacity();
        Player turnOwner = session.getPlayers().get(turn);
        controller.setTurnOwner(turnOwner.getUsername());
        if (turnOwner.getGod() == null) {
            notifyMessage(messageBuilder(MessageType.GODS_SELECTION_UPDATE,  turnOwner.getUsername()));
        }
    }

    /**
     * Assigns the god to the player that choose it
     *
     * @param player the player that choose the god
     * @param god the god chosen by the player
     */
    private void assignGod(String player, String god){
        Player user = session.getPlayerByName(player);
        if (user != null && user.getGod() == null) {
            user.setGod(Gods.valueOf(god));
            views.forEach(view -> connectModelView(view, user.getRules()));
        }
    }

    /**
     * Associates the rules to the specific view of the player
     *
     * @param view the view of the player
     * @param rules the rules of the god chosen by the player
     */
    private void connectModelView(RemoteView view, GodRules rules) {
        rules.addObserver(view);
    }
}


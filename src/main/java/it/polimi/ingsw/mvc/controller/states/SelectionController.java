package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller state used during the selection of each player god and the starter player
 */
public class SelectionController extends StateController implements Serializable {

    private static final long serialVersionUID = -1391531620876546345L;
    private final List<String> chosenGod = new ArrayList<>();
    private final Session session;
    private final Player challenger;
    private int turn;
    private GameState state;

    /**
     * Creates the state
     *
     * @param views      list of the {@link RemoteView RemoteViews}
     * @param log        logger where the events will be stored
     * @param controller controller of the MVC
     */
    public SelectionController(SessionController controller, List<RemoteView> views, Logger log) {
        super(controller, views, log);
        session = Session.getInstance();
        challenger = session.getPlayerByName(session.getChallenger());
        state = GameState.CHALLENGER_SELECTION;
        notifyMessage(messageBuilder(MessageType.SELECTION_UPDATE, challenger.getUsername()));
        turn = session.getPlayers().indexOf(challenger);
        controller.setTurnOwner(challenger.getUsername());
    }

    /**
     * Parse the view request
     *
     * @param message the message received
     */
    @Override
    public void parseMessage(Message message) {
        if (message.getSender().equals(controller.getTurnOwner())) {
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
                    log.warning(() -> "Wrong message type : " + message);
            }
        } else {
            log.warning(() -> "A player who is not the turn owner sent a message : " + message);
        }
    }

    /**
     * Reads challenger selection message and add the gods chosen to the list of the gods of the game
     *
     * @param message the message received
     */
    private void parseAddMessage(Message message) {
        if (state == GameState.CHALLENGER_SELECTION) {
            if (Gods.isValid(message.getInfo()) && !chosenGod.contains(message.getInfo())) {
                chosenGod.add(message.getInfo());
                notifyMessage(messageBuilder(MessageType.GODS_UPDATE, message.getInfo(), true));
                if (chosenGod.size() == controller.getPlayers().size()) {
                    state = GameState.GOD_SELECTION;
                    askNextSelection();
                }
            } else {
                notifyMessage(messageBuilder(MessageType.SELECTION_UPDATE, challenger.getUsername()));
            }
        }
    }

    /**
     * Assigns the god requested (through message) to the player
     *
     * @param message the message received
     */
    private void parseGodChoiceMessage(Message message) {
        if (state == GameState.GOD_SELECTION) {
            if (chosenGod.contains(message.getInfo())) {
                chosenGod.remove(message.getInfo());
                assignGod(message.getSender(), message.getInfo());
                if (!chosenGod.isEmpty()) nextSelection(message.getInfo());
                else closeSelection(message.getInfo());
            } else {
                notifyMessage(messageBuilder(MessageType.GODS_SELECTION_UPDATE, "Turn notify", message.getSender()));
            }
        }
    }

    /**
     * Notifies the next player that the controller is waiting its choice
     *
     * @param info god chosen on the previous input
     */
    private void nextSelection(String info) {
        notifyMessage(messageBuilder(MessageType.GODS_UPDATE, info, false));
        askNextSelection();
    }

    /**
     * Notifies the challenger that the controller is waiting for the starter player choice
     *
     * @param info god chosen on the previous input
     */
    private void closeSelection(String info) {
        state = GameState.STARTER_SELECTION;
        controller.setTurnOwner(challenger.getUsername());
        notifyMessage(messageBuilder(MessageType.GODS_UPDATE, info, false));
    }

    /**
     * Saves the game of the starter player and switches to the next state
     *
     * @param message the reply containing the name of the starter player
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
     * Changes the state of the controller to "GAME"
     */
    public void tryNextState() {
        controller.switchState(GameState.GAME);
    }


    /**
     * Updates the turn owner and notifies the views
     */
    private void askNextSelection() {
        turn = (turn + 1) % controller.getGameCapacity();
        Player turnOwner = session.getPlayers().get(turn);
        controller.setTurnOwner(turnOwner.getUsername());
        if (turnOwner.getGod() == null)
            notifyMessage(messageBuilder(MessageType.GODS_SELECTION_UPDATE, turnOwner.getUsername()));
    }

    /**
     * Assigns the god to the player who chose it
     *
     * @param player the player that choose the god
     * @param god    the god chosen by the player
     */
    private void assignGod(String player, String god) {
        Player user = session.getPlayerByName(player);
        if (user != null && user.getGod() == null) {
            user.setGod(Gods.valueOf(god));
            views.forEach(view -> connectModelView(view, user.getRules()));
        }
    }

    /**
     * Sets each View as observer of the Model
     *
     * @param view  the view of the player
     * @param rules the rules of the god chosen by the player
     */
    private void connectModelView(RemoteView view, GodRules rules) {
        rules.addObserver(view);
    }
}


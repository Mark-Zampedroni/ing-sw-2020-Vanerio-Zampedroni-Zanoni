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

public class SelectionController extends StateController implements Serializable {

    private static final long serialVersionUID = -1391531620876546345L;
    private final List<String> chosenGod = new ArrayList<>();
    private final Session session;
    private final Player challenger;
    private int turn;
    private GameState state;


    public SelectionController(SessionController controller, Map<String, RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        session = Session.getInstance();
        challenger = session.getPlayerByName(session.getChallenger());
        state = GameState.CHALLENGER_SELECTION;
        notifyMessage(messageBuilder(MessageType.SELECTION_UPDATE, challenger.getUsername()));
        turn = session.getPlayers().indexOf(challenger);
        controller.setTurnOwner(challenger.getUsername());
    }

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

    public void tryNextState() {
        controller.switchState(GameState.GAME);
    }

    private void askNextSelection() {
        turn = (turn + 1) % controller.getGameCapacity();
        Player turnOwner = session.getPlayers().get(turn);
        controller.setTurnOwner(turnOwner.getUsername());
        if (turnOwner.getGod() == null) {
            notifyMessage(messageBuilder(MessageType.GODS_SELECTION_UPDATE,  turnOwner.getUsername()));
        }
    }

    private void assignGod(String player, String god){
        Player user = session.getPlayerByName(player);
        if (user != null && user.getGod() == null) {
            user.setGod(Gods.valueOf(god));
            views.values().forEach(view -> connectModelView(view, user.getRules()));
        }
    }

    private void connectModelView(RemoteView view, GodRules rules) {
        rules.addObserver(view);
    }



}


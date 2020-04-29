package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.MVC.view.RemoteView;

import java.util.*;
import java.util.logging.Logger;

public class SelectionController extends StateController {


    private List<String> chosenGod = new ArrayList<>();
    private Session session;
    private Player challenger;
    private int turn;
    private GameState state;


    public SelectionController(SessionController controller, Map<String, RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        session = Session.getInstance();
        challenger = session.getPlayerByName(session.getChallenger());
        state = GameState.CHALLENGER_SELECTION;
        sendBroadcastMessage(new Message(MessageType.CHALLENGER_SELECTION, "SERVER", challenger.getUsername()));
        turn = session.getPlayers().indexOf(challenger);
        controller.setTurnOwner(challenger.getUsername());
    }

    @Override
    public void parseMessage(Message message) {
        if(message.getSender().equals(controller.getTurnOwner())) {
            switch (message.getType()) {
                case GOD_MANAGEMENT:
                    parseAddMessage(message);
                    break;
                case GOD_PLAYERCHOICE:
                    parseGodChoiceMessage(message);
                    break;
                case STARTER_PLAYER:
                    parseStarterPlayerMessage(message);
                default:
                    System.out.println("[Warning] Wrong message type");
            }
        }
        else {
            LOG.warning("A player who is not the turn owner sent a message : "+message);
        }
    }

    private void parseAddMessage(Message message) {
        if (state == GameState.CHALLENGER_SELECTION){
            if (Gods.isValid(message.getInfo())) {
                chosenGod.add(message.getInfo());
                sendBroadcastMessage(new FlagMessage(MessageType.GOD_MANAGEMENT, "SERVER", message.getInfo(), true));
                if (chosenGod.size() == controller.getPlayers().size()) {
                    state = GameState.GOD_SELECTION;
                    askNextSelection();
                }
            } else {
                sendBroadcastMessage(new Message(MessageType.CHALLENGER_SELECTION, "SERVER", challenger.getUsername()));
            }
        }
    }
    private void parseGodChoiceMessage(Message message) {
        if (state == GameState.GOD_SELECTION) {
            if (chosenGod.contains(message.getInfo())) {
                chosenGod.remove(message.getInfo());
                assignGod(message.getSender(), message.getInfo());
                if (chosenGod.size() < controller.getPlayers().size()) {
                    sendBroadcastMessage(new FlagMessage(MessageType.GOD_MANAGEMENT, "SERVER", message.getInfo(), false));
                    askNextSelection(); //Spostare send update sopra
                }else{
                    state = GameState.STARTER_SELECTION;
                    controller.setTurnOwner(challenger.getUsername());
                    sendBroadcastMessage(new FlagMessage(MessageType.GOD_MANAGEMENT, "SERVER", message.getInfo(), false));
                }
            } else {
                views.get(message.getSender()).sendMessage(new Message(MessageType.GOD_PLAYERCHOICE, "SERVER", "choice"));
            }
        }
    }

    private void parseStarterPlayerMessage(Message message) {
        if (state == GameState.STARTER_SELECTION) {
            for (Player player : controller.getPlayers()) {
                if (player.getUsername().equals(message.getInfo())) {
                    player.setStarter();
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
            sendBroadcastMessage(new Message(MessageType.GOD_PLAYERCHOICE, "SERVER", turnOwner.getUsername()));
        }
    }

    private void assignGod(String player, String god){
        Player user = session.getPlayerByName(player);
        if (user != null && user.getGod() == null) {
            user.setGod(Gods.valueOf(god));
        }
    }

}


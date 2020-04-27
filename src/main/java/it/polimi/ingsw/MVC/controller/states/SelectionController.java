package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.SessionController;
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

    public SelectionController(SessionController controller, Map<String, RemoteView> views, Logger LOG) {
        super(controller, views, LOG);
        session = Session.getInstance();
        challenger = session.getPlayerByName(session.getChallenger());
        sendBroadcastMessage(new Message(MessageType.GOD_START, "SERVER", challenger.getUsername()));
        turn = session.getPlayers().indexOf(challenger);
    }

    @Override
    public void parseMessage(Message message) {
        switch (message.getType()) {
            case GOD_ADD:
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

    @Override
    public void sendUpdate() {
        //sendBroadcastMessage(new Message(MessageType.GOD_REMOVE,"SERVER", ""));
    }


    private void parseAddMessage(Message message) {
        if (message.getSender().equals(challenger.getUsername())) { // Manca condizione god contenuto in enum
            chosenGod.add(message.getInfo());
            sendBroadcastMessage(new Message(MessageType.GOD_ADD,"SERVER", message.getInfo()));
            if(chosenGod.size() == controller.getPlayers().size()){
                askNextSelection();
            }
        }
        else{sendBroadcastMessage(new Message(MessageType.GOD_START, "SERVER", challenger.getUsername()));}
    }

    private void parseGodChoiceMessage(Message message) {
        if (chosenGod.contains(message.getInfo())) {
            chosenGod.remove(message.getInfo());
            assignGod(message.getSender(),message.getInfo());
            sendBroadcastMessage(new Message(MessageType.GOD_REMOVE,"SERVER", message.getInfo()));
            if (chosenGod.size() < controller.getPlayers().size()) {
                askNextSelection();
            }
        } else {
            views.get(message.getSender()).sendMessage(new Message(MessageType.GOD_PLAYERCHOICE, "SERVER", "choice"));
        }
    }

    private void parseStarterPlayerMessage(Message message) {
        for (Player player : controller.getPlayers()) {
            if (player.getUsername().equals(message.getInfo())) {
                tryNextState();
            }
        }
    }

    public void tryNextState() {
        controller.switchState(GameState.FIRST_TURN);
    }

    private void askNextSelection() {
        turn = (turn + 1) % controller.getGameCapacity();
        Player turnOwner = session.getPlayers().get(turn);
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
/*
    private void parseGodMessage (Message message){
        System.out.println(message);
        System.out.println(challenger.getUsername());
        if(message.getSender().equals(challenger.getUsername())) {
            System.out.println("Challenger");
            if (godMap.containsKey(message.getInfo())) {
                godMap.remove(message.getInfo());
                godMap.get("chosen").add(message.getInfo());
                if (godMap.get("chosen").size() < controller.getGameCapacity()) {
                    sendUpdate();
                } else {
                    sendBroadcastMessage(new GodUpdate("SERVER", "update", godMap));
                    askNextSelection();
                }
            }
            else { sendUpdate(); }
        }
    }

    private void parseGodChoiceMessage(Message message){
        if(!choosignStarter){
            if (godMap.get("chosen").contains(message.getInfo())) {
                assignGod(message.getSender(), message.getInfo());
                if (godMap.get("chosen").size() > 0) {
                    askNextSelection();
                } else {
                    views.get(challenger.getUsername()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "starter"));
                    choosignStarter = true;
                }
            } else {
                views.get(message.getSender()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "choice"));
            }
        }
        else{
            if(views.containsKey(message.getInfo())){
                //message.getInfo contiene lo starting player Si potrebbe riordinare la lista dei player avendo questo in prima posizione.
                tryNextState();
            }
            else{views.get(session.getChallenger()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "starter"));}
        }
    }

    private void startGodMap(){
        ArrayList<String> list = new ArrayList<>();
        for(Gods god: Gods.values()) {
            godMap.put(god.toString(),god.getDescription());
        }
        godMap.put("chosen",list);
    }

 */


    /*
    private void assignGod(String player, String god) {
        Player user = session.getPlayerByName(player);
        if (user != null && user.getGod() == null) {
            user.setGod(Gods.valueOf(god));
            godMap.get("chosen").remove(god);
            sendBroadcastMessage(new Message(MessageType.GOD_CHOICE, "SERVER", god));
        }
    }

     */


package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.GodUpdate;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SelectionController extends StateController {


    private Map<String, ArrayList<String>> GodMap= new HashMap<>();
    boolean flag; // Momentanea

    public SelectionController(SessionController controller, Map<String,RemoteView> views) {
        super(controller, views);
        startGodMap();
        sendUpdate();
    }

    @Override
    public void parseMessage(Message message) {
        switch(message.getType()) {
            case GOD_UPDATE:
                parseGodMessage(message);
                break;
            case GOD_CHOICE:
                parseGodChoiceMessage(message);
                break;
            default:
                System.out.println("[Warning] Wrong message type");
        }
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new GodUpdate("SERVER", Session.getInstance().getChallenger(),GodMap));
    }

    private void parseGodMessage (Message message){
        if(message.getSender().equals(Session.getInstance().getChallenger())) {
            if (GodMap.containsKey(message.getInfo())){
                GodMap.keySet().removeIf(text -> text.equals(message.getInfo()));
                GodMap.get("chosen").add(message.getInfo());
                if (GodMap.get("chosen").size() < controller.getPlayers().size()) {
                    sendUpdate();
                } else {
                    sendBroadcastMessage(new GodUpdate("SERVER", "update",GodMap));
                    views.get(Session.getInstance().getChallenger()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","starter"));
                }
            }
            else {sendUpdate();}
        }
    }

    private void parseGodChoiceMessage(Message message){
        if(message.getSender().equals(Session.getInstance().getChallenger()) && !flag){
            if(views.containsKey(message.getInfo())){
                flag=true;
                sendBroadcastMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "available"));
                views.get(message.getInfo()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","choice"));
            }
            else {views.get(Session.getInstance().getChallenger()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","starter"));}
        }
        else {
            if (GodMap.get("chosen").size() > 0) {
                if (GodMap.get("chosen").contains(message.getInfo())) {
                    for(Player user: controller.getPlayers()){
                        if(user.getUsername().equals(message.getSender()) && user.getGod() == null) {
                            for (Gods god : Gods.values()) {
                                if (message.getInfo().equals(god.toString())) {
                                    user.setGod(god);
                                }
                            }
                        }
                    }
                    GodMap.get("chosen").remove(message.getInfo());
                    sendBroadcastMessage(new Message(MessageType.GOD_CHOICE, "SERVER", message.getInfo()));
                    for(Player user: controller.getPlayers()){
                        if(user.getGod() == null){
                            views.get(user.getUsername()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","choice"));
                            break;
                        }
                    }
                }
                else{
                    views.get(message.getSender()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","choice"));
                }
            }
            else{
                tryNextState();
            }

        }
    }

    private void startGodMap(){
        ArrayList<String> list = new ArrayList<>();
        for(Gods god: Gods.values()) {
            GodMap.put(god.toString(),god.getDescription());
        }
        GodMap.put("chosen",list);
    }

    public void tryNextState() {
        controller.switchState(GameState.FIRST_TURN);
    }

}

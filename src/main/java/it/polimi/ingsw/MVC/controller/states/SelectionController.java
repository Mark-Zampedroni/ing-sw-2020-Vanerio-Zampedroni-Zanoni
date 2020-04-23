package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.GodUpdate;
import it.polimi.ingsw.MVC.view.RemoteView;

import java.util.ArrayList;
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
                    for(Player player: controller.getPlayers()){
                        if(!player.isChallenger()){
                            views.get(player.getUsername()).sendMessage(new Message(MessageType.GOD_CHOICE,"SERVER","choice"));
                            break;
                        }
                    }
                }
            }
            else {sendUpdate();}
        }
    }

    private void parseGodChoiceMessage(Message message){
        if(!flag){
            if (GodMap.get("chosen").contains(message.getInfo())) {
                for (Player user : controller.getPlayers()) {
                    if (user.getUsername().equals(message.getSender()) && user.getGod() == null) {
                        for (Gods god : Gods.values()) {
                            if (message.getInfo().equals(god.toString())) {
                                user.setGod(god);
                            }
                        }
                    }
                }
                GodMap.get("chosen").remove(message.getInfo());
                sendBroadcastMessage(new Message(MessageType.GOD_CHOICE, "SERVER", message.getInfo()));
                if (GodMap.get("chosen").size() > 0) {
                    for (Player user : controller.getPlayers()) {
                        if (user.getGod() == null && (!user.isChallenger() || GodMap.get("chosen").size() == 1)) {
                            views.get(user.getUsername()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "choice"));
                            break;
                        }
                    }
                } else {
                    views.get(Session.getInstance().getChallenger()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "starter"));
                    flag = true;
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
            else{views.get(Session.getInstance().getChallenger()).sendMessage(new Message(MessageType.GOD_CHOICE, "SERVER", "starter"));}
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

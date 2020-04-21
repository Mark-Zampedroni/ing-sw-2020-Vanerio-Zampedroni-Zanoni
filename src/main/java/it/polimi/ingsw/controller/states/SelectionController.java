package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.GodChoice;
import it.polimi.ingsw.net.messages.lobby.GodUpdate;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SelectionController extends StateController {


    private Map<String, ArrayList<String>> GodMap= new HashMap<>();
    String players = new String();

    public SelectionController(SessionController controller, Map<String,RemoteView> views) {
        super(controller, views);
        startGodMap();
        sendUpdate();
    }

    @Override
    public void parseMessage(Message message) {
        switch(message.getType()) {
            case GOD_UPDATE:
                parseGodMessage((GodUpdate) message);
                break;
            case GOD_CHOICE:
                parseGodChoiceMessage(message);
            default:
                System.out.println("[Warning] Wrong message type");
        }
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new GodUpdate("SERVER", Session.getInstance().getChallenger(),GodMap));
    }

    private void parseGodMessage (GodUpdate message){
        if(message.getSender().equals(Session.getInstance().getChallenger())) {
            if (GodMap.containsKey(message.getInfo())){
                if (GodMap.get("chosen").size() < Session.getInstance().getPlayers().size()) {
                    GodMap.keySet().removeIf(text -> text.equals(message.getInfo()));
                    GodMap.get("chosen").add(message.getInfo());
                    sendUpdate();
                } else {
                    views.get(Session.getInstance().getChallenger()).sendMessage(new GodChoice("SERVER","starter" , new ArrayList<>(views.keySet())));
                }
            }
            else {sendUpdate();}
        }
    }

    private void parseGodChoiceMessage(Message message){
        
    }

    private void startGodMap(){
        ArrayList<String> list = new ArrayList<>();
        for(Gods god: Arrays.asList(Gods.values())) {
            GodMap.put(god.toString(),god.getDescription());
        }
        GodMap.put("chosen",list);
    }

}

package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.GodUpdate;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SelectionController extends StateController {

    public SelectionController(SessionController controller, Map<String,RemoteView> views) {
        super(controller, views);
        sendUpdate();
    }

    @Override
    public void parseMessage(Message message) {
        switch(message.getType()) {
            case GOD_UPDATE:
                parseGodMessage((GodUpdate) message);
                break;
            default:
                System.out.println("[Warning] Wrong message type");
        }
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new GodUpdate("SERVER", Session.getInstance().getChallenger(),getGodsList()));
    }

    private void parseGodMessage (GodUpdate message){
        if(message.getSender().equals(Session.getInstance().getChallenger())) {
            if (message.getGods().get("chosen").size() < Session.getInstance().getPlayers().size() - 1) {
                message.getGods().keySet().removeIf(text -> text.equals(message.getInfo()));
                message.getGods().get("chosen").add(message.getInfo());
                sendBroadcastMessage(new GodUpdate("SERVER", Session.getInstance().getChallenger(), message.getGods()));
            }
            else{
                System.out.println("\nChosen Gods:\n");
                for(String already: message.getGods().get("chosen")){
                    System.out.println(already + "\t" );
                }
                controller.switchState(GameState.GOD_ASSIGNMENT);
            }
        }
    }


    private Map<String, ArrayList<String>> getGodsList(){
        Map<String, ArrayList<String>> GodMap = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        for(Gods god: Arrays.asList(Gods.values())) {
            GodMap.put(god.toString(),god.getDescription());
        }
        GodMap.put("chosen",list);
        return GodMap;
    }


}

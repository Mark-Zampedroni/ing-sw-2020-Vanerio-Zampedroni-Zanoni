package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
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
        System.out.println("SELECTION CONTROLLER: "+message);
    }

    @Override
    public void sendUpdate() {
        for (String player : views.keySet()) {
            if (Session.getInstance().getChallenger().equals(player)) {
               // views.get(player).sendMessage(new GodUpdate(MessageType.GOD_UPDATE, "SERVER", "Available Gods: \n" + getGodsList() + "\nYou are the challenger\n", true, null));
            } else {
                views.get(player).sendMessage(new FlagMessage(MessageType.GOD_UPDATE, "SERVER", "Available Gods: \n" + getGodsList() + "\nThe challenger is " + Session.getInstance().getChallenger(), false));
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

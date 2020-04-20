package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class LobbyController extends StateController {

    private final List<ServerConnection> pendingConnections;
    private final List<Colors> freeColors;
    private final List<String> readyPlayers;

    public LobbyController(SessionController controller, Map<String,RemoteView> views, List<ServerConnection> pendingConnections) {
        super(controller, views);
        freeColors = new ArrayList<>(Arrays.asList(Colors.values()));
        readyPlayers = new ArrayList<>();
        this.pendingConnections = pendingConnections;
    }

    @Override
    public void parseMessage(Message message) {
        switch(message.getType()) {
            case READY:
                parseReadyMessage(message);
                break;
            default:
                System.out.println("[Warning] Wrong message type");
        }
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new LobbyUpdate("SERVER", "Update", controller.getFreeColors(), controller.getPlayers()));
    }

    private void parseReadyMessage(Message message) {
        System.out.println("\nReady message: "+message+"\n"); // TEST
        if(!readyPlayers.contains(message.getSender())) {
            readyPlayers.add(message.getSender());
        }
        if(views.keySet().size() > 1 && readyPlayers.size() == views.keySet().size()) {
            controller.switchState(GameState.GOD_SELECTION);
        }
        System.out.println("Ready people: "+readyPlayers); // TEST
    }

    @Override
    public void sendBroadcastMessage(Message message) {
        super.sendBroadcastMessage(message);
        for (ServerConnection c : pendingConnections) {
            c.sendMessage(message);
        }
    }

}

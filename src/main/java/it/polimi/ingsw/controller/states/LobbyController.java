package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;
import java.util.stream.Collectors;

public class LobbyController extends StateController {

    private final List<ServerConnection> pendingConnections;
    private final List<String> readyPlayers;

    public LobbyController(SessionController controller, Map<String,RemoteView> views, List<ServerConnection> pendingConnections) {
        super(controller, views);
        readyPlayers = new ArrayList<>();
        this.pendingConnections = pendingConnections;
    }

    @Override
    public void parseMessage(Message message) {
        if(message.getType() == MessageType.READY) {
            parseReadyMessage(message);
        }
        else {
                System.out.println("[Warning] Wrong message type");
        }
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new LobbyUpdate("SERVER", "Update", controller.getFreeColors(), controller.getPlayers()));
    }

    private void parseReadyMessage(Message message) {
        System.out.println("\nReady message: "+message+"\n"); // TEST
        readyPlayers.removeIf(p -> !views.containsKey(p));
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

    @Override
    public List<Colors> getFreeColors() {
        return Arrays.asList(Colors.values())
                .stream()
                .filter(c -> !(controller.getPlayers().stream().map(Player::getColor).collect(Collectors.toList())).contains(c))
                .collect(Collectors.toList());
    }

}

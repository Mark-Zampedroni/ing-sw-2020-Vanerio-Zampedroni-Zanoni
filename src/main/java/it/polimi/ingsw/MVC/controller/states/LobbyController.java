package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.ConnectionLevel;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.MVC.view.RemoteView;

import java.util.*;
import java.util.stream.Collectors;

public class LobbyController extends StateController {

    private final List<ServerConnection> pendingConnections;

    public LobbyController(SessionController controller, Map<String,RemoteView> views, List<ServerConnection> pendingConnections) {
        super(controller, views);
        this.pendingConnections = pendingConnections;
    }

    @Override
    public void parseMessage(Message message) {
        System.out.println("[Warning] Clients tried to send a message while in LOBBY");
    }

    @Override
    public void sendUpdate() {
        sendBroadcastMessage(new LobbyUpdate("SERVER", "Update", controller.getFreeColors(), controller.getPlayers()));
    }


    @Override
    public void sendBroadcastMessage(Message message) {
        super.sendBroadcastMessage(message);
        sendAllPending(message);
    }

    private void sendAllPending(Message message) {
        pendingConnections.stream().filter(c -> c.getConnectionLevel() == ConnectionLevel.PENDING).forEach(c -> c.sendMessage(message));
    }

    @Override
    public List<Colors> getFreeColors() {
        return Arrays.stream(Colors.values())
                .filter(c -> !(controller.getPlayers().stream().map(Player::getColor).collect(Collectors.toList())).contains(c))
                .collect(Collectors.toList());
    }

    @Override
    public void tryNextState() {
        controller.switchState(GameState.GOD_SELECTION);
    }

}

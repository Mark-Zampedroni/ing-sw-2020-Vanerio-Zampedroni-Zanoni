package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.ReconnectionMessage;
import it.polimi.ingsw.network.messages.lobby.ReconnectionUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DiffLobbyController extends LobbyController {
    public DiffLobbyController(SessionController controller, List<RemoteView> views, Logger LOG, List<ServerConnection> unregistered) {
        super(controller, views, LOG, unregistered);
    } // Altrimenti con il resto commentato non compila
/*
    StateController nextStateController;
    List<Player> playerList;

    public DiffLobbyController(StateController stateController, SessionController controller, Map<String, RemoteView> views, Logger LOG, List<ServerConnection> unregistered){
        super(controller, views, LOG, unregistered);
        nextStateController = stateController;
        playerList = controller.getSession().getPlayers();
        System.out.println("Sono in different lobby");
    }

    @Override
    public void parseMessage(Message message) {
        if(message.getType() == MessageType.RECONNECTION) {
            reconnectConnection((ReconnectionMessage) message);
        }
    }

    private void reconnectConnection(ReconnectionMessage message) {

        String requestedUsername = message.getInfo();
        RemoteView view = pendingConnections.get(message.getSender());

        for(Player p : playerList) {
            if (!p.getUsername().equals(requestedUsername)) {
                view.sendMessage(createReconnectionReply("This username were not in last game", false));
                LOG.warning("A player tried to register with the wrong username " + requestedUsername + "\n");
                return;
            }
        }
        if (view != null && !view.getRegistered()) { // Anti-cheat
            if (views.containsKey(requestedUsername)) { // Anti-cheat
                view.sendMessage(createReconnectionReply("This username is already in use", false));
                LOG.warning("A player tried to register with the already in use username " + requestedUsername + "\n");
            } else {
                playerList.removeIf(p -> p.getUsername().equals(requestedUsername));
                confirmReconnection(message.getSender(), requestedUsername, view);
                view.sendMessage(createReconnectionReply(requestedUsername, true));
                sendUpdate();
                if (views.size() == controller.getGameCapacity()) {
                    startGame();
                }
            }
        } else {
            LOG.severe("An already registered view requested registration, it was blocked with no further action");
        }
    }

    private Message createReconnectionReply(String info, boolean success) {
        return new FlagMessage(MessageType.RECONNECTION, "SERVER", info, success);
    }

    private void confirmReconnection(String token, String user, RemoteView view) {
        view.register(user);
        LOG.info(user + " registered\n");
        controller.addPlayer(user, view);
        pendingConnections.remove(token);
    }

    private void startGame() {
        List<ServerConnection> copy = new ArrayList<>(freshConnections);
        copy.forEach(c -> c.denyConnection("The game has started, you were disconnected"));
        freshConnections.clear();
        tryNextState();
    }

    @Override
    public void sendUpdate() { // Invece di controller.getPlayers() un DTO
        sendBroadcastMessage(new ReconnectionUpdate("SERVER", "Update", playerList));
    }


    @Override
    public void sendBroadcastMessage(Message message) {
        super.sendBroadcastMessage(message);
        sendAllPending(message);
    }

    private void sendAllPending(Message message) {
        for (String player : pendingConnections.keySet()) {
            pendingConnections.get(player).sendMessage(message);
        }
    }


    @Override
    public void tryNextState() {
        controller.diffSwitchState(nextStateController);
    }

    @Override
    public void addUnregisteredView(ServerConnection connection) {
        RemoteView newView = new RemoteView(connection);
        newView.addObserver(controller);
        pendingConnections.put(connection.getToken(), newView);
    }

    @Override
    public void removePlayer(String username) {
        super.removePlayer(username);
        pendingConnections.remove(username);
    }

    @Override
    public void addPlayer(String username, Colors color, RemoteView view) {
        Session.getInstance().addPlayer(username, color);
        view.register(username);
        views.put(username, view);
    }
    */
}

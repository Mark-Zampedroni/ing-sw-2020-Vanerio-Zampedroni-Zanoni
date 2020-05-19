package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LobbyController extends StateController implements Serializable {

    private static final long serialVersionUID = 6213786388958970675L;
    transient final Map<String, RemoteView> pendingConnections;
    transient final List<ServerConnection> freshConnections;

    public LobbyController(SessionController controller, Map<String, RemoteView> views, Logger LOG, List<ServerConnection> unregistered) {
        super(controller, views, LOG);
        this.pendingConnections = new HashMap<>();
        this.freshConnections = unregistered;
    }


    @Override
    public void parseMessage(Message message) {
        if(message.getType() == MessageType.REGISTRATION) {
            registerConnection((RegistrationMessage) message);
        }
    }


    private void registerConnection(RegistrationMessage message) {

        String requestedUsername = message.getInfo();
        Colors requestedColor = message.getColor();
        RemoteView view = pendingConnections.get(message.getSender());

        if (view != null && !view.getRegistered()) { // Anti-cheat
            if (views.containsKey(requestedUsername)) { // Anti-cheat
                notifyMessage(createRegistrationReply("This username is already in use", false, message.getSender()));
                LOG.warning("A player tried to register with the already in use username " + requestedUsername + "\n");
            } else if (!getFreeColors().contains(requestedColor)) { // Anti-cheat
                notifyMessage(createRegistrationReply("Color " + requestedColor + " is not available", false, message.getSender()));
                LOG.warning("A player tried to register with the already in use color " + requestedColor + "\n");
            } else {
                confirmRegistration(message.getSender(), requestedUsername, requestedColor, view);
                notifyMessage(createRegistrationReply(requestedUsername, true, message.getSender()));
                sendUpdate();
                if (views.size() == controller.getGameCapacity()) {
                    startGame();
                }
            }
        } else {
            LOG.severe("An already registered view requested registration, it was blocked with no further action");
        }
    }

    private Message createRegistrationReply(String info, boolean success, String recipient) {
        return new FlagMessage(MessageType.REGISTRATION, "SERVER", info, success, recipient);
    }

    private void confirmRegistration(String token, String user, Colors color, RemoteView view) {
        view.register(user);
        LOG.info(user + " registered\n");
        controller.addPlayer(user, color, view);
        pendingConnections.remove(token);
    }

    private void startGame() {
        List<ServerConnection> copy = new ArrayList<>(freshConnections);
        copy.forEach(c -> c.denyConnection("The game has started, you were disconnected "));
        freshConnections.clear();
        tryNextState();
    }

    @Override
    public void sendUpdate() {
        notifyMessage(new LobbyUpdate("SERVER", "Update", controller.getFreeColors(), controller.getPlayers(), "ALL"));
    }


    @Override
    public void notifyMessage(Message message) {
        super.notifyMessage(message);
        sendAllPending(message);
    }

    private void sendAllPending(Message message) {
        for (String player : pendingConnections.keySet()) {
            pendingConnections.get(player).sendMessage(message);
        }
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

}

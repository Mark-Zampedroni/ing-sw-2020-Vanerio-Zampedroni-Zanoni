package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LobbyController extends StateController implements Serializable {

    private static final long serialVersionUID = 6213786388958970675L;
    transient final List<RemoteView> pendingConnections;
    transient final List<ServerConnection> freshConnections;

    public LobbyController(SessionController controller, List<RemoteView> views, Logger LOG, List<ServerConnection> unregistered) {
        super(controller, views, LOG);
        this.pendingConnections = new ArrayList<>();
        this.freshConnections = unregistered;
    }


    @Override
    public void parseMessage(Message message) {
        if(message.getType() == MessageType.REGISTRATION_UPDATE) {
            registerConnection((RegistrationMessage) message);
        }
    }


    private void registerConnection(RegistrationMessage message) {

        String requestedUsername = message.getInfo();
        Colors requestedColor = message.getColor();
        RemoteView sender = pendingConnections.stream().filter(v -> v.hasName(message.getSender())).findFirst().orElse(null);

        if (sender != null && !sender.getRegistered()) { // Anti-cheat
            if (Session.getInstance().getPlayerByName(requestedUsername) != null) { // Anti-cheat
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE,"This username is already in use", false, message.getSender()));
                LOG.warning("A player tried to register with the already in use username " + requestedUsername + "\n");
            } else if (!getFreeColors().contains(requestedColor)) { // Anti-cheat
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE,"Color " + requestedColor + " is not available", false, message.getSender()));
                LOG.warning("A player tried to register with the already in use color " + requestedColor + "\n");
            } else {
                confirmRegistration(message.getSender(), requestedUsername, requestedColor, sender);
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE,requestedUsername, true, message.getSender()));
                sendUpdate();
                if (views.size() == controller.getGameCapacity()) {
                    startGame();
                }
            }
        } else {
            LOG.severe("An already registered view requested registration, it was blocked with no further action");
        }
    }

    private void confirmRegistration(String token, String user, Colors color, RemoteView view) {
        view.register(user);
        LOG.info(user + " registered\n");
        controller.addPlayer(user, color, view);
        pendingConnections.removeIf(v -> v.hasName(token));
    }

    private void startGame() {
        new ArrayList<>(freshConnections).forEach(c -> c.denyConnection("The game has started, you were disconnected "));
        freshConnections.clear();
        tryNextState();
    }

    @Override
    public void sendUpdate() {
        notifyMessage(messageBuilder("Lobby update"));
    }


    @Override
    public void notifyMessage(Message message) {
        super.notifyMessage(message);
        sendAllPending(message);
    }

    private void sendAllPending(Message message) {
        for (RemoteView customer : pendingConnections) {
            customer.sendMessage(message);
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
        pendingConnections.add(newView);
    }


    @Override
    public void addPlayer(String username, Colors color, RemoteView view) {
        Session.getInstance().addPlayer(username, color);
        view.register(username);
        views.add(view);
    }

}

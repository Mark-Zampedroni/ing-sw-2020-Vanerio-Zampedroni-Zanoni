package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller used during the initial phase of the game, it manages the choice of the username and the color
 * by the clients, it manages also the creation of the views of the clients
 */
public class LobbyController extends StateController implements Serializable {

    private static final long serialVersionUID = 6213786388958970675L;
    final transient List<RemoteView> pendingConnections;
    final transient List<ServerConnection> freshConnections;

    /**
     * Creates the controller for the initial phase
     *
     * @param views        list of the {@link RemoteView remoteViews} of the players for the updates
     * @param log          general logger of the server
     * @param controller   general controller for the session
     * @param unregistered list of clients trying to connect
     */
    public LobbyController(SessionController controller, List<RemoteView> views, Logger log, List<ServerConnection> unregistered) {
        super(controller, views, log);
        this.pendingConnections = new ArrayList<>();
        this.freshConnections = unregistered;
    }


    /**
     * Reads the message and, in case of {@link Message registrationMessage}
     * calls the method for the registration
     *
     * @param message the message received by the client
     */
    @Override
    public void parseMessage(Message message) {
        if (message.getType() == MessageType.REGISTRATION_UPDATE) {
            registerConnection((RegistrationMessage) message);
        }
    }


    /**
     * Controls if the chosen name and the chosen color are avaiable, if not it sends a
     * new request and an error message. If they are correct, register the connection and create a player.
     *
     * @param message the message containing all the information about the player
     */
    private void registerConnection(RegistrationMessage message) {

        String requestedUsername = message.getInfo();
        Colors requestedColor = message.getColor();
        RemoteView sender = pendingConnections.stream().filter(v -> v.hasName(message.getSender())).findFirst().orElse(null);

        if (sender != null && !sender.getRegistered()) { // Anti-cheat
            if (Session.getInstance().getPlayerByName(requestedUsername) != null) { // Anti-cheat
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE, "This username is already in use", false, message.getSender()));
                log.warning(() -> "A player tried to register with the already in use username " + requestedUsername + "\n");
            } else if (!getFreeColors().contains(requestedColor)) { // Anti-cheat
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE, "Color " + requestedColor + " is not available", false, message.getSender()));
                log.warning(() -> "A player tried to register with the already in use color " + requestedColor + "\n");
            } else {
                confirmRegistration(message.getSender(), requestedUsername, requestedColor, sender);
                notifyMessage(messageBuilder(MessageType.REGISTRATION_UPDATE, requestedUsername, true, message.getSender()));
                sendUpdate();
                if (views.size() == controller.getGameCapacity()) {
                    startGame();
                }
            }
        } else {
            log.severe("An already registered view requested registration, it was blocked with no further action");
        }
    }

    /**
     * It remove a registered player by the pending list and add the player and his info
     * to the {@link SessionController controller} list
     *
     * @param view  the view associated to the player
     * @param color the color of the player
     * @param user  the name of the player
     * @param token the string associated with the view
     */
    private void confirmRegistration(String token, String user, Colors color, RemoteView view) {
        view.register(user);
        log.info(() -> user + " registered\n");
        controller.addPlayer(user, color, view);
        pendingConnections.removeIf(v -> v.hasName(token));
    }

    /**
     * Send a deny connection to all pending connections in lobby after the filling of the lobby,
     * than it change the game state to "God_Selection"
     */
    private void startGame() {
        new ArrayList<>(freshConnections).forEach(c -> c.denyConnection("The game has started, you were disconnected "));
        freshConnections.clear();
        tryNextState();
    }

    /**
     * Send an update in broadcast to all players
     */
    @Override
    public void sendUpdate() {
        notifyMessage(messageBuilder());
    }

    /**
     * Send an update to all the players and all the connection pending
     *
     * @param message the message containing the update
     */
    @Override
    public void notifyMessage(Message message) {
        super.notifyMessage(message);
        sendAllPending(message);
    }

    /**
     * Send an update to all the connections in pending list
     *
     * @param message the message containing the update
     */
    private void sendAllPending(Message message) {
        for (RemoteView customer : pendingConnections) {
            customer.sendMessage(message);
        }
    }

    /**
     * It returns the available color
     *
     * @return the list of the not chosen colors
     */
    @Override
    public List<Colors> getFreeColors() {
        return Arrays.stream(Colors.values())
                .filter(c -> !(controller.getPlayers().stream().map(Player::getColor).collect(Collectors.toList())).contains(c))
                .collect(Collectors.toList());
    }

    /**
     * Method that change the game state in "God_Selecion" when the lobby is full
     */
    @Override
    public void tryNextState() {
        controller.switchState(GameState.GOD_SELECTION);
    }

    /**
     * Add a {@link ServerConnection connection} to the list of the connections of the players,
     * create the view linked to the connection
     */
    @Override
    public void addUnregisteredView(ServerConnection connection) {
        RemoteView newView = new RemoteView(connection);
        newView.addObserver(controller);
        pendingConnections.add(newView);
    }

    /**
     * Adds a player in the lobby to the {@link Session session}
     *
     * @param username name of the player
     * @param color    color chosen by the player
     * @param view     {@link RemoteView remoteView} associated to the player
     */
    @Override
    public void addPlayer(String username, Colors color, RemoteView view) {
        Session.getInstance().addPlayer(username, color);
        view.register(username);
        views.add(view);
    }

    /**
     * Removes a player in the lobby
     *
     * @param username name of the player
     */
    @Override
    public synchronized void removePlayer(String username) {
        if (Session.getInstance().getPlayerByName(username) != null) {
            Session.getInstance().removePlayer(username);
            views.removeIf(v -> v.hasName(username));
        }
    }

}

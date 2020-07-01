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
 * Controller state on lobby
 */
public class LobbyController extends StateController implements Serializable {

    private static final long serialVersionUID = 6213786388958970675L;
    final transient List<RemoteView> pendingConnections;
    final transient List<ServerConnection> freshConnections;

    /**
     * Creates the state
     *
     * @param views        list of the {@link RemoteView RemoteViews}
     * @param log          logger where any controller event will be stored
     * @param controller   controller of the MVC
     * @param unregistered list of connections not yet registered
     */
    public LobbyController(SessionController controller, List<RemoteView> views, Logger log, List<ServerConnection> unregistered) {
        super(controller, views, log);
        this.pendingConnections = new ArrayList<>();
        this.freshConnections = unregistered;
    }

    /**
     * Reads the request and, in case of {@link Message registrationMessage},
     * tries to register the connection
     *
     * @param message the request received by the connection
     */
    @Override
    public void parseMessage(Message message) {
        if (message.getType() == MessageType.REGISTRATION_UPDATE) {
            registerConnection((RegistrationMessage) message);
        }
    }

    /**
     * Tries to register a connection.
     * Checks if the chosen name and the chosen color are available, if not replies error message.
     * If they are both available registers the connection and adds the player to the model.
     *
     * @param message the registration request message
     */
    private void registerConnection(RegistrationMessage message) {

        String requestedUsername = message.getInfo();
        Colors requestedColor = message.getColor();
        RemoteView sender = pendingConnections.stream().filter(v -> v.hasName(message.getSender())).findFirst().orElse(null);

        if (sender != null && !sender.isRegistered()) { // Anti-cheat
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
        } else log.severe("An already registered view requested registration, it was blocked with no further action");
    }

    /**
     * Removes a registered connection, deletes its player from the model
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
     * Closes all the pending connections and starts the game
     */
    private void startGame() {
        new ArrayList<>(freshConnections).forEach(c -> c.denyConnection("The game has started, you were disconnected "));
        freshConnections.clear();
        tryNextState();
    }

    /**
     * Updates the views
     */
    @Override
    public void sendUpdate() {
        notifyMessage(messageBuilder());
    }

    /**
     * Updates all the connections
     *
     * @param message the message containing the update
     */
    @Override
    public void notifyMessage(Message message) {
        super.notifyMessage(message);
        sendAllPending(message);
    }

    /**
     * Updates all the pending connections
     *
     * @param message the message containing the update
     */
    private void sendAllPending(Message message) {
        for (RemoteView customer : pendingConnections) {
            customer.sendMessage(message);
        }
    }

    /**
     * Returns a list of available colors
     *
     * @return list of available colors
     */
    @Override
    public List<Colors> getFreeColors() {
        return Arrays.stream(Colors.values())
                .filter(c -> !(controller.getPlayers().stream().map(Player::getColor).collect(Collectors.toList())).contains(c))
                .collect(Collectors.toList());
    }

    /**
     * Changes the state of the controller to "GOD_SELECTION"
     */
    @Override
    public void tryNextState() {
        controller.switchState(GameState.GOD_SELECTION);
    }

    /**
     * Creates a view for the connection and sets the controller to observe it
     */
    @Override
    public void addUnregisteredView(ServerConnection connection) {
        RemoteView newView = new RemoteView(connection);
        newView.addObserver(controller);
        pendingConnections.add(newView);
    }

    /**
     * Adds a player to the {@link Session Model}
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
     * Removes a player from the {@link Session Model}, deletes its view
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

package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.persistency.SaveData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class of methods and variables shared by the {@link SessionController Controller} states.
 */
public abstract class StateController implements Serializable {

    private static final long serialVersionUID = -7974027435942352531L;

    private static final String SENDER = "SERVER";

    protected transient List<RemoteView> views;
    protected transient SessionController controller;
    protected transient Logger log;

    /**
     * Creates a state
     *
     * @param controller controller of the MVC
     * @param log        logger where the events will be stored
     * @param views      list of all the {@link RemoteView RemoteViews} connected
     */
    public StateController(SessionController controller, List<RemoteView> views, Logger log) {
        this.controller = controller;
        this.views = views;
        this.log = log;
    }

    /**
     * Reloads the values of the controller from a saved data file
     *
     * @param controller controller of the MVC
     * @param log        logger where the events will be stored
     * @param views      list of all the connections
     * @param saveData   previous game data
     */
    public void reloadState(SessionController controller, SaveData saveData, List<RemoteView> views, Logger log) {
        restorePreviousState(views, controller, log);
        log.info(() -> "[STATE CONTROLLER] Notifying successfull reconnection to " + views);
        notifyMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, "Server", "Reconnected successfully", true, "ALL"));
        if (!saveData.getActionDone()) {
            log.info(() -> "Last message before save is being re-parsed");
            parseMessage(saveData.getMessage());
        }
    }

    /**
     * Restores the values of the state
     *
     * @param views             list of all the connections
     * @param log               logger where the events will be stored
     * @param sessionController controller of the MVC
     */
    private void restorePreviousState(List<RemoteView> views, SessionController sessionController, Logger log) {
        this.log = log;
        this.views = views;
        controller = sessionController;
    }

    /**
     * Parses the view request
     *
     * @param message the message received
     */
    public abstract void parseMessage(Message message);

    /**
     * Tries to change state to the one specified in the implementation
     */
    public abstract void tryNextState();

    /**
     * Notifies all the views with a message
     *
     * @param message the message that each view will receive
     */
    public void notifyMessage(Message message) {
        controller.saveGame(message, true);
        views.forEach(w -> w.sendMessage(message));
    }

    /**
     * Removes the player with the username from the game, if the game is open closes it
     *
     * @param username the name of the player to remove
     */
    public synchronized void removePlayer(String username) {
        if (Session.getInstance().getPlayerByName(username) == null) return;
        boolean willGameClose = !Session.getInstance().getPlayerByName(username).isLoser();
        views.removeIf(v -> v.hasName(username));
        if (willGameClose) {
            notifyMessage(new Message(MessageType.DISCONNECTION_UPDATE, "Server", username + " disconnected, the game was closed", "ALL"));
            controller.restartGame();
        }
    }

    /**
     * Builds a message with the specified values
     *
     * @param type      the type of the message
     * @param info      the information inside the message
     * @param recipient the receiver of the message
     */
    protected Message messageBuilder(MessageType type, String info, String recipient) {
        return new Message(type, SENDER, info, recipient);
    }

    /**
     * Builds a message with the specified values
     *
     * @param type the type of the message
     * @param info the information inside the message
     */
    protected Message messageBuilder(MessageType type, String info) {
        return messageBuilder(type, info, "ALL");
    }

    /**
     * Builds a {@link FlagMessage flagMessage} with the specified values
     *
     * @param type      the type of the message
     * @param info      the information inside the message
     * @param recipient the receiver of the message
     * @param flag      passes a boolean value
     */
    protected Message messageBuilder(MessageType type, String info, boolean flag, String recipient) {
        return new FlagMessage(type, SENDER, info, flag, recipient);
    }

    /**
     * Builds a {@link FlagMessage flagMessage} with the specified values
     *
     * @param type the type of the message
     * @param info the information inside the message
     * @param flag passes a boolean value
     */
    protected Message messageBuilder(MessageType type, String info, boolean flag) {
        return messageBuilder(type, info, flag, "ALL");
    }

    /**
     * Builds a message with the specified values
     *
     * @param info the information inside the message
     */
    protected Message messageBuilder(String info) {
        return new LobbyUpdate(SENDER, info, controller.getFreeColors(), controller.getPlayers(), "ALL");
    }

    /**
     * Builds a message with the specified values
     */
    protected Message messageBuilder() {
        return messageBuilder("Lobby update");
    }

    /**
     * Placeholder method, it shouldn't be called.
     * It's not implemented by all the states. It may be (but shouldn't) called from any state.
     */
    public void addPlayer(String username, Colors color, RemoteView view) {
        log.warning(() -> "[STATE_CONTROLLER] addPlayer on " + username + " " + color + " " + view + " called on wrong state");
    }

    /**
     * Placeholder method, it shouldn't be called.
     * It's not implemented by all the states. It may be (but shouldn't) called from any state.
     *
     * @return the list of available colors (empty if not overrided)
     */
    public List<Colors> getFreeColors() {
        log.warning(() -> "[STATE_CONTROLLER] getFreeColors called on wrong state");
        return new ArrayList<>();
    }

    /**
     * Placeholder method, it shouldn't be called.
     * It's not implemented by all the states. It may be (but shouldn't) called from any state.
     */
    public void addUnregisteredView(ServerConnection connection) {
        log.warning(() -> "[STATE_CONTROLLER] addUnregisteredView on " + connection + " called on wrong state");
    }

    /**
     * Placeholder method, it shouldn't be called.
     * It's not implemented by all the states. It may be (but shouldn't) called from any state.
     */
    public void sendUpdate() {
        log.warning("This state can't send updates");
    }

}

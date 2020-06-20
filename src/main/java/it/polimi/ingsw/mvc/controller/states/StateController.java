package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.persistency.SavedData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract class containing the general methods shared by all the controllers of the different game phases
 */
public abstract class StateController implements Serializable {

    private static final long serialVersionUID = -7974027435942352531L;

    protected transient List<RemoteView> views;
    protected transient SessionController controller;
    protected transient Logger LOG;

    /**
     * Creates a controller that change its state during the game
     *
     * @param controller that is currently active
     * @param LOG general LOG of the server
     * @param views list of all the {@link RemoteView remoteview} for comunication with the players
     */
    public StateController(SessionController controller, List<RemoteView> views, Logger LOG) {
        this.controller = controller;
        this.views = views;
        this.LOG = LOG;
    }

    /**
     * Reset conditions and values to a situation prior to a failure
     *
     * @param controller the controller that is referred to
     * @param LOG logger of the server
     * @param views list of all the connections
     * @param savedData all the data about the previous game
     */
    public void reloadState(SessionController controller, SavedData savedData, List<RemoteView> views, Logger LOG) {
        resetPreviousState(views, controller, LOG);
        LOG.info("[STATE CONTROLLER] Notifying successfull reconnection to "+views);
        notifyMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, "Server", "Reconnected successfully",true, "ALL"));
        if(!savedData.getActionDone()) {
            LOG.info("Last message before save is being re-parsed");
            parseMessage(savedData.getMessage());
        }
    }

    /**
     * Reset the values inside the controller after a the rest
     *
     * @param views list of all the connections
     * @param LOG logger of the server
     * @param sessionController the controller that is referred to
     */
    private void resetPreviousState(List<RemoteView> views,SessionController sessionController, Logger LOG) {
        this.LOG = LOG;
        this.views = views;
        controller = sessionController;
    }

    /**
     * Sends updates to clients, placeholder for make sendUpdate() callable by subclasses
     */
    public void sendUpdate() {
        LOG.warning("This state can't send updates");
    }

    /**
     * Reads messages from clients, placeholder to make sendUpdate() callable by subclasses
     *
     * @param message the message received
     */
    public abstract void parseMessage(Message message);

    /**
     * Change the game state, placeholder for the subclasses
     */
    public abstract void tryNextState();

    /**
     * Sends to all the view the message passed, save the game before sending
     *
     * @param message the message to send
     */
    public void notifyMessage(Message message) {
        controller.saveGame(message,true);
        views.forEach(w -> w.sendMessage(message));
    }

    /**
     * Removes the player with the username by the game and close the game if the game state is after lobby
     *
     * @param username the name of the player to remove
     */
    public synchronized void removePlayer(String username) {
        if(Session.getInstance().getPlayerByName(username) == null) { return; }
        boolean willGameClose = !Session.getInstance().getPlayerByName(username).isLoser();
        views.removeIf(v -> v.hasName(username));
        if(willGameClose) {
            notifyMessage(new Message(MessageType.DISCONNECTION_UPDATE, "Server", username + " disconnected, the game was closed", "ALL"));
            controller.restartGame();
        }
    }

    /**
     * Creates a message for the client
     *
     * @param type the type of the message
     * @param info the information inside the message
     * @param recipient the receiver of the message
     */
    protected Message messageBuilder(MessageType type, String info, String recipient) {
        return new Message(type,"SERVER",info,recipient);
    }

    /**
     * Creates a message for the client
     *
     * @param type the type of the message
     * @param info the information inside the message
     */
    protected Message messageBuilder(MessageType type, String info) {
        return messageBuilder(type,info,"ALL");
    }

    /**
     * Creates a {@link FlagMessage flagMessage} for the client
     *
     * @param type the type of the message
     * @param info the information inside the message
     * @param recipient the receiver of the message
     * @param flag passes a boolean value
     */
    protected Message messageBuilder(MessageType type, String info, boolean flag, String recipient) {
        return new FlagMessage(type,"SERVER",info,flag,recipient);
    }

    /**
     * reates a {@link FlagMessage flagMessage} for the client
     *
     * @param type the type of the message
     * @param info the information inside the message
     * @param flag passes a boolean value
     */
    protected Message messageBuilder(MessageType type, String info, boolean flag) {
        return messageBuilder(type,info,flag,"ALL");
    }

    /**
     * Creates a message for the client
     *
     * @param info the information inside the message
     * @param recipient the receiver of the message
     */
    protected Message messageBuilder(String info,String recipient) {
        return new LobbyUpdate("SERVER",info,controller.getFreeColors(), controller.getPlayers(),recipient);
    }

    /**
     * Creates a message for the client
     *
     * @param info the information inside the message
     */
    protected Message messageBuilder(String info) {
        return messageBuilder(info,"ALL");
    }

    /**
     * Placeholder to make addPlayer() callable by subclass LobbyController
     */
    public void addPlayer(String username, Colors color, RemoteView view) {
        LOG.warning("[STATE_CONTROLLER] addPlayer called on wrong state");
    }

    /**
     * Placeholder to make getFreeColors() callable by subclass LobbyController
     *
     * @return the list of available colors
     */
    public List<Colors> getFreeColors() {
        LOG.warning("[STATE_CONTROLLER] getFreeColors called on wrong state");
        return new ArrayList<>();
    }

    /**
     * Placeholder to make addUnregisteredView() callable by subclass LobbyController
     */
    public void addUnregisteredView(ServerConnection connection) {
        LOG.warning("[STATE_CONTROLLER] addUnregisteredView called on wrong state");
    }

}

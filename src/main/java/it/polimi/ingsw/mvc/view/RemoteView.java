package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionUpdateMessage;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.observer.Observable;
import it.polimi.ingsw.utility.observer.Observer;

import java.util.List;
import java.util.Map;

/**
 * Remote view, posing as View in the MVC pattern
 */
public class RemoteView extends Observable<Message> implements Observer<DtoSession> {

    private final ServerConnection connection;
    private boolean registered;
    private DtoSession dtoSession;

    /**
     * Constructor
     *
     * @param connection the connection to the client
     */
    public RemoteView(ServerConnection connection) {
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
    }

    /**
     * Registers an username.
     * It's used as signature in the exchange of messages
     *
     * @param username new username of the RemoteView
     */
    public void register(String username) {
        if (!registered) {
            connection.setName(username);
            registered = true;
        }
    }

    /**
     * Checks if the remoteView has the given username
     *
     * @param name username to check
     * @return {@code true} if the remoteView name is the one specified
     */
    public boolean hasName(String name) {
        return connection.getUsername().equals(name);
    }

    /**
     * Checks if an username was already assigned
     *
     * @return {@code true} if the remoteView has an username
     */
    public boolean getRegistered() {
        return registered;
    }

    /**
     * Sends a message on the connection of the remoteView
     *
     * @param message message to send
     */
    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    /**
     * Updates the remoteView (Observer) when anything in the Model (Observable) is updated
     *
     * @param dtoSession updated Model
     */
    @Override
    public void update(DtoSession dtoSession) {
        this.dtoSession = dtoSession;
    }

    /**
     * Called before the first update of the Model, assigns the variable
     * dtoSession if it's null
     *
     * @param dtoSession fresh model
     */
    public void getFirstDTOSession(DtoSession dtoSession) {
        if (this.dtoSession == null) {
            this.dtoSession = dtoSession;
        }
    }

    /**
     * Updates the possible actions the client connected to the remoteView can do
     *
     * @param actionCandidates     possible actions
     * @param turnOwner            name of the current turn owner
     * @param isSpecialPowerActive {@code true} if the passive god's special power is activated
     */
    public void updateActions(Map<Action, List<DtoPosition>> actionCandidates, String turnOwner, boolean isSpecialPowerActive) {
        sendMessage(new ActionUpdateMessage("SERVER", turnOwner, actionCandidates, dtoSession, isSpecialPowerActive, "ALL"));
    }

    /**
     * Inner class used to forward the messages from the connection to the RemoteView
     * using a Observer/Observable pattern
     */
    private class MessageReceiver implements Observer<Message> {
        /**
         * Update from the Observable (Connection)
         *
         * @param message information about the change
         */
        @Override
        public void update(Message message) {
            if (message.getSender() != null && message.getSender().equals(connection.getUsername())) { //Anti-cheat
                handleInput(message);
            }
        }

        /**
         * Notifies the remoteView of the message received from the connection
         *
         * @param message message received
         */
        private void handleInput(Message message) {
            RemoteView.this.notify(message);
        }
    }

}

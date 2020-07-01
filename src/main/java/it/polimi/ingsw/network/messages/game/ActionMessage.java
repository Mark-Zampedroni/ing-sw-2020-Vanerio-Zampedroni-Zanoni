package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

/**
 * Message used to send an action on a connection
 */
public class ActionMessage extends Message implements Serializable {

    private static final long serialVersionUID = 1392822996168131758L;
    private final Action action;
    private final DtoPosition position;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param action chosen action
     * @param position chosen position
     * @param content info
     * @param recipient the recipient of the message
     */
    public ActionMessage(String sender, String content, Action action, DtoPosition position, String recipient) {
        super(MessageType.ACTION, sender, content, recipient);
        this.action = action;
        this.position = position;
    }

    /**
     * Getter for the position
     *
     * @return the position
     */
    public DtoPosition getPosition() {
        return position;
    }

    /**
     * Getter for the action
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }

}


package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class StateUpdateMessage extends Message implements Serializable {

    private static final long serialVersionUID = 1605170428705161467L;
    private final GameState state;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param info information
     * @param state state
     * @param type type of the message
     * @param recipient the recipient of the message
     */
    public StateUpdateMessage(MessageType type, String sender, String info, GameState state, String recipient) {
        super(type, sender, info, recipient);
        this.state = state;
    }


    /**
     * Getter for the state
     *
     * @return the state
     */
    public GameState getState() {
        return state;
    }
}

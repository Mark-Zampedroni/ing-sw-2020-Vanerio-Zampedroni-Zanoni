package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;

public class StateUpdateMessage extends Message {

    private final GameState state;

    public StateUpdateMessage(MessageType type, String sender, String info, GameState state) {
        super(type, sender, info);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}

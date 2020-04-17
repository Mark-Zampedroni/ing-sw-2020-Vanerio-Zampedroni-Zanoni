package it.polimi.ingsw.net.messages;

import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;

public class StateUpdateMessage extends Message {

    private GameState state;

    public StateUpdateMessage(MessageType type, String sender, String info, GameState state) {
        super(type, sender, info);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}

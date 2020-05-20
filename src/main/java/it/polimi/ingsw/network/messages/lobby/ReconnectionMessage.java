package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;

public class ReconnectionMessage extends Message {

    //Inviare solo il nome del player

    public ReconnectionMessage(String sender, String playerName, String recipient) {
        super(MessageType.RECONNECTION, sender, playerName, recipient);
    }
}

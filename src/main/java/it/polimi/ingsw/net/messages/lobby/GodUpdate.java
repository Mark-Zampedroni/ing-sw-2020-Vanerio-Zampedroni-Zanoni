package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.Message;

import java.util.List;

public class GodUpdate extends Message {

    public GodUpdate(MessageType type, String sender, String info) {
        super(MessageType.GOD_UPDATE, sender, info);
    }
}

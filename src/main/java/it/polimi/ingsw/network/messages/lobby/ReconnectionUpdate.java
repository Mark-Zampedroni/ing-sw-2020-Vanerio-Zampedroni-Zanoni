package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.MessageType;
import java.util.List;

public class ReconnectionUpdate extends Message {

    //Waiting for other players..., Done!

    private final List<Player> playerList;

    public ReconnectionUpdate(String sender, String info, List<Player> players, String recipient) {
        super(MessageType.RECONNECTION_UPDATE, sender, info, recipient);
        playerList=players;
    }


    public List<Player> getPlayers() { return playerList; }
}

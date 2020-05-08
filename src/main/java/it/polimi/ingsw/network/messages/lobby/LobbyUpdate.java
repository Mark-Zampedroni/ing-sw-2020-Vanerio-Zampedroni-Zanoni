package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyUpdate extends Message { // Anche per quando un player si flagga ready

    private final List<Colors> colors;
    private final Map<String, Colors> registeredPlayers;

    public LobbyUpdate(String sender, String info, List<Colors> colors, List<Player> players) {
        super(MessageType.LOBBY_UPDATE, sender, info);
        this.colors = colors;
        registeredPlayers = new HashMap<>();
        for(Player p : players) {
            registeredPlayers.put(p.getUsername(),p.getColor());
        }
    }

    public List<Colors> getColors() {
        return colors;
    }
    public Map<String,Colors> getPlayers() { return registeredPlayers; }
}

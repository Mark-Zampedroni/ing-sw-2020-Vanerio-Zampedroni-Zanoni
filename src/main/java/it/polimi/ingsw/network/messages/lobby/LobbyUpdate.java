package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyUpdate extends Message implements Serializable {
    private static final long serialVersionUID = 6088218796433616304L; // Anche per quando un player si flagga ready

    private final List<Colors> colors;
    private final Map<String, Colors> registeredPlayers;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param info information
     * @param colors list of colors
     * @param players list of players
     * @param recipient the recipient of the message
     */
    public LobbyUpdate(String sender, String info, List<Colors> colors, List<Player> players, String recipient) {
        super(MessageType.LOBBY_UPDATE, sender, info, recipient);
        this.colors = colors;
        registeredPlayers = new HashMap<>();
        for (Player p : players) {
            registeredPlayers.put(p.getUsername(), p.getColor());
        }
    }

    /**
     * Getter for the colors
     *
     * @return a list of colors
     */
    public List<Colors> getColors() {
        return colors;
    }

    /**
     * Getter for a map of players and their colors
     *
     * @return tha map
     */
    public Map<String, Colors> getPlayers() {
        return registeredPlayers;
    }
}

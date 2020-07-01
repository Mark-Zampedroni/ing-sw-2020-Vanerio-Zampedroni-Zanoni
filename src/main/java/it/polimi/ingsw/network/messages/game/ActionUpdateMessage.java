package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Message used to send a map of actions to their possible positions and a  flag on a connection
 */
public class ActionUpdateMessage extends FlagMessage implements Serializable {

    private static final long serialVersionUID = 3928828614516772694L;
    private final Map<Action, List<DtoPosition>> possibleActions;
    private final DtoSession gameUpdate;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param isSpecialPowerActive defines if a special power is active
     * @param possibleActions a map containing the possible actions and their positions
     * @param turnOwner current player
     * @param gameUpdate the change of the game
     * @param recipient the recipient of the message
     */
    public ActionUpdateMessage(String sender, String turnOwner, Map<Action, List<DtoPosition>> possibleActions, DtoSession gameUpdate, boolean isSpecialPowerActive, String recipient) {
        super(MessageType.TURN_UPDATE, sender, turnOwner, isSpecialPowerActive, recipient);
        this.possibleActions = possibleActions;
        this.gameUpdate = gameUpdate;
    }

    /**
     * Getter for a map of possible actions and their positions
     *
     * @return the map
     */
    public Map<Action, List<DtoPosition>> getPossibleActions() {
        return possibleActions;
    }

    /**
     * Getter for the changes in the game
     *
     * @return the changes
     */
    public DtoSession getGameUpdate() {
        return gameUpdate;
    }
}

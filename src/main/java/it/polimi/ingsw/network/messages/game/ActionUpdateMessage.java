package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ActionUpdateMessage extends FlagMessage implements Serializable {

    private static final long serialVersionUID = 3928828614516772694L;
    private final Map<Action, List<DtoPosition>> possibleActions;
    private final DtoSession gameUpdate;

    public ActionUpdateMessage(String sender, String turnOwner, Map<Action, List<DtoPosition>> possibleActions, DtoSession gameUpdate, boolean isSpecialPowerActive, String recipient) {
        super(MessageType.TURN_UPDATE, sender, turnOwner, isSpecialPowerActive, recipient);
        this.possibleActions = possibleActions;
        this.gameUpdate = gameUpdate;
    }

    public Map<Action, List<DtoPosition>> getPossibleActions() {
        return possibleActions;
    }

    public DtoSession getGameUpdate() {
        return gameUpdate;
    }
}

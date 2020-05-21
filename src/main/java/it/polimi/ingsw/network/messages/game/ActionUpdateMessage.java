package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;
import it.polimi.ingsw.utility.serialization.dto.DtoSession;

import java.util.List;
import java.util.Map;

public class ActionUpdateMessage extends Message {

    private final Map<Action, List<DtoPosition>> possibleActions;
    private final DtoSession gameUpdate;

    public ActionUpdateMessage(String sender, String turnOwner, Map<Action, List<DtoPosition>> possibleActions, DtoSession gameUpdate, String recipient) {
        super(MessageType.TURN_UPDATE, sender, turnOwner, recipient);
        this.possibleActions = possibleActions;
        this.gameUpdate = gameUpdate;
    }

    public Map<Action, List<DtoPosition>> getPossibleActions() {
        return possibleActions;
    }
    public DtoSession getGameUpdate() { return gameUpdate; }
}
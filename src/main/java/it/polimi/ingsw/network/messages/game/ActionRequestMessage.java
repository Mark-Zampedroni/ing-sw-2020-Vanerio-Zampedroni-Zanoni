package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;

import java.util.List;
import java.util.Map;

public class ActionRequestMessage extends Message {

    Map<Action, List<DTOposition>> possibleActions;
    DTOsession gameUpdate;

    public ActionRequestMessage(String sender, String turnOwner, Map<Action, List<DTOposition>> possibleActions, DTOsession gameUpdate) {
        super(MessageType.TURN_UPDATE, sender, turnOwner);
        this.possibleActions = possibleActions;
        this.gameUpdate = gameUpdate;
    }

    public Map<Action, List<DTOposition>> getPossibleActions() {
        return possibleActions;
    }
    public DTOsession getGameUpdate() { return gameUpdate; }
}

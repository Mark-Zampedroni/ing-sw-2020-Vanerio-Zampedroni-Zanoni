package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

// event is true when additional movement turn has been occurred
public class ArtemisRules extends EventRule {

    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        ArrayList<ActionType> actions = new ArrayList<>();
        if (!this.getEvent()) {
            actions.add(ActionType.MOVE); }
        actions.add(ActionType.BUILD);
        this.setEvent(true);
        this.setPos(position);
        return actions;
    }

    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.oldPosition(worker,position);
    }

}
package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.AthenaRules;
import it.polimi.ingsw.rules.EventRule;

import java.util.ArrayList;

public class ArthemisMoveAction extends MoveAction{

    //metodo funziona per Artemide, vedere se funziona anche per altri
    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        EventRule rule = (EventRule) worker.getMaster().getRules();
        ArrayList<ActionType> actions = new ArrayList<>();
        if (!rule.getEvent()) {
        actions.add(ActionType.MOVE); }
        actions.add(ActionType.BUILD);
        rule.setEvent(true);
        rule.setPos(position);
        return actions;
    }
}

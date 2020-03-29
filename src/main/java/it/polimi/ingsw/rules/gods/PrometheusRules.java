package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRule;

import java.util.ArrayList;

public class PrometheusRules extends EventRule {
//event true if the worker has built before movement
    @Override

    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.height(worker, position);
    }
//Rivedere
    @Override
    public ArrayList<ActionType> executeBuild(Position position, Worker worker) {
        ArrayList<ActionType> nextAction;
        nextAction = super.executeBuild(position, worker);
        if (!this.getEvent()) {
            this.setEvent(true);
            nextAction = new ArrayList<>();
            nextAction.add(ActionType.MOVE);}
        return nextAction;
    }

}
package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class PrometheusRules extends EventRule {
//event true if the worker has built before movement
    @Override

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (getEvent() && Board.getTile(worker.getPosition()).getHeight() < Board.getTile(position).getHeight()) {
            throw new MoveGodPowerException("Not Allowed due to Prometheus' power");
        }
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
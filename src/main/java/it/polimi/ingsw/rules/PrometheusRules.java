package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class PrometheusRules extends EventRule {
//event true if the worker has built before movement
    @Override

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (getEvent() && Board.getTile(worker.getPosition()).getHeight() < Board.getTile(position).getHeight()) {
            throw new PrometheusException("Not Allowed due to Prometheus' power");
        }
    }

}
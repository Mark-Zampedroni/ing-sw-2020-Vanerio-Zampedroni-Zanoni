package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class PrometheusException extends CantMoveException {
    public PrometheusException(String message) { super(message); }
}

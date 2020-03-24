package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class MoveOnWorkerException extends CantMoveException {
    public MoveOnWorkerException(String message) { super(message);}
}

package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class MoveOnAllyWorkerException extends CantMoveException {
    public MoveOnAllyWorkerException(String message) { super(message); }
}

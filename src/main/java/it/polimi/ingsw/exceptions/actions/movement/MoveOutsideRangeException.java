package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class MoveOutsideRangeException extends CantMoveException {
    public MoveOutsideRangeException(String message) { super(message); }
}

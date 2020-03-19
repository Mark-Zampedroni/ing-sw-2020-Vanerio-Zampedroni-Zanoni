package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class OutofBorderException extends CantMoveException {
    public OutofBorderException(String message) { super(message); }
}

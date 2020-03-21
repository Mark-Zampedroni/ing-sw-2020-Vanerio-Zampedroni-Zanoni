package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class SamePlayerException extends CantMoveException {
    public SamePlayerException(String message) { super(message); }
}

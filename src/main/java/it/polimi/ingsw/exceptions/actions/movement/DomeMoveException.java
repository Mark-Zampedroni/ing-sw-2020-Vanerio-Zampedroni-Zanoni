package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class DomeMoveException extends CantMoveException {
    public DomeMoveException(String message) { super(message);}
}

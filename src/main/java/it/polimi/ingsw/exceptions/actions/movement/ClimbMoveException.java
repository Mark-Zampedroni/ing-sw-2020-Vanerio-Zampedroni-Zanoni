package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class ClimbMoveException extends CantMoveException {
    public ClimbMoveException(String message) { super(message);}
}

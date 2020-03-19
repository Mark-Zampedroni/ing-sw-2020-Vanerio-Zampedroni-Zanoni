package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class MinotaurException extends CantMoveException {
    public MinotaurException(String message) { super(message); }
}

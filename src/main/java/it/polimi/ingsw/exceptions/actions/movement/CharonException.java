package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class CharonException extends CantMoveException {
    public CharonException(String message) { super(message); }

}

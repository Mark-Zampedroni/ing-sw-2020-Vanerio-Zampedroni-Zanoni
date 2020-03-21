package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class ArtemisException extends CantMoveException {
    public ArtemisException(String message) { super(message); }
}

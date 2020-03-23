package it.polimi.ingsw.exceptions.actions.movement;

import it.polimi.ingsw.exceptions.actions.CantMoveException;

public class TritonException extends CantMoveException {
    public TritonException(String message) { super(message); }

}

package it.polimi.ingsw.exceptions.actions;

public class SamePlayerException extends CantMoveException {
    public SamePlayerException(String message) { super(message); }
}

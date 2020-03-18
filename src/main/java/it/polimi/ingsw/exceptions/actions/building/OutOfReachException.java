package it.polimi.ingsw.exceptions.actions.building;

import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class OutOfReachException extends CantBuildException {
    public OutOfReachException(String message) { super(message); }
}
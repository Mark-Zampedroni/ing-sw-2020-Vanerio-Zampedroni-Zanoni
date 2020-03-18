package it.polimi.ingsw.exceptions.actions.building;

import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class AlreadyOccupiedException extends CantBuildException {
    public AlreadyOccupiedException(String message) { super(message); }
}

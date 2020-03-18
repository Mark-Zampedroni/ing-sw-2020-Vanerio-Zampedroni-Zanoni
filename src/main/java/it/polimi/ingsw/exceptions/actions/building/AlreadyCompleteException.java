package it.polimi.ingsw.exceptions.actions.building;

import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class AlreadyCompleteException extends CantBuildException {
    public AlreadyCompleteException(String message) { super(message); }
}

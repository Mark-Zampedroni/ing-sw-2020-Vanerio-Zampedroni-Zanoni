package it.polimi.ingsw.exceptions.actions.building;

import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class DemeterException extends CantBuildException {
    public DemeterException(String message) { super(message); }
}

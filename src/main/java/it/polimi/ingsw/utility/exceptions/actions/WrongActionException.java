package it.polimi.ingsw.utility.exceptions.actions;

/**
 * Exception thrown if a player tries to execute an action not available
 */
public class WrongActionException extends Exception {

    /**
     * Constructor
     *
     * @param message contains the information about the exception
     */
    public WrongActionException(String message) {
        super(message);
    }
}

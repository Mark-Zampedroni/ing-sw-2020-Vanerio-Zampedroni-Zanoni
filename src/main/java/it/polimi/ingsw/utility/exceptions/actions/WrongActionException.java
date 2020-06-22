package it.polimi.ingsw.utility.exceptions.actions;

/**
 * Exception thrown if the player makes a wrong action
 */
public class WrongActionException extends Exception {

    /**
     * Constructor for the exception
     *
     * @param message contains the information about the exception
     */
    public WrongActionException(String message) {
        super(message);
    }
}

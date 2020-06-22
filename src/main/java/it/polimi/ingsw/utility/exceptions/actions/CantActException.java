package it.polimi.ingsw.utility.exceptions.actions;

/**
 * Exception thrown if the worker can't perform the action
 */
public class CantActException extends Exception {

    /**
     * Constructor for the exception
     *
     * @param message contains the information about the exception
     */
    public CantActException(String message) {
        super(message);
    }
}

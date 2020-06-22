package it.polimi.ingsw.utility.exceptions.net;

/**
 * Exception thrown if the connection fails
 */
public class FailedConnectionException extends Exception {

    /**
     * Constructor for the exception
     *
     * @param message contains the information about the exception
     */
    public FailedConnectionException(String message) {
        super(message);
    }
}


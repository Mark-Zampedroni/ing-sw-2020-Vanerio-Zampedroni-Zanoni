package it.polimi.ingsw.utility.exceptions.actions;

/**
 * Exception thrown if the {@link it.polimi.ingsw.mvc.model.player.Worker worker} can't perform the action requested
 */
public class CantActException extends Exception {

    /**
     * Constructor
     *
     * @param message contains the information about the exception
     */
    public CantActException(String message) {
        super(message);
    }
}

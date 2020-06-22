package it.polimi.ingsw.utility.exceptions.utility;

/**
 * Exception thrown if a class is not instantiable
 */
public class NotInstantiableClass extends Exception {

    /**
     * Constructor for the exception
     */
    public NotInstantiableClass() {
        super("This class can't have instances");
    }
}

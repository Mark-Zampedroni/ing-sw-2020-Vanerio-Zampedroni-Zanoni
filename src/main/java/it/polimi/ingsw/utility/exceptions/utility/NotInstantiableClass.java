package it.polimi.ingsw.utility.exceptions.utility;

/**
 * Exception thrown if the constructor of a class not instantiable is called
 */
public class NotInstantiableClass extends Exception {

    /**
     * Constructor
     */
    public NotInstantiableClass() {
        super("This class can't have instances");
    }
}

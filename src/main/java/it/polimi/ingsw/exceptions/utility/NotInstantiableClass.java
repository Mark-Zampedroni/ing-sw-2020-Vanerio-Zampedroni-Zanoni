package it.polimi.ingsw.exceptions.utility;

public class NotInstantiableClass extends Exception {
    public NotInstantiableClass() { super("This class can't have instances"); }
}

package it.polimi.ingsw.exceptions;

public class NotInstantiableClass extends Exception {
    public NotInstantiableClass(String message) { super(message); }
    public NotInstantiableClass() { super("This class can't have instances"); }
}

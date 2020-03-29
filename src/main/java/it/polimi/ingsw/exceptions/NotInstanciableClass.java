package it.polimi.ingsw.exceptions;

public class NotInstanciableClass extends Exception {
    public NotInstanciableClass(String message) { super(message); }
    public NotInstanciableClass() { super("This class can't have instances"); }
}

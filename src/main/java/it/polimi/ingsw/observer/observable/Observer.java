package it.polimi.ingsw.observer.observable;

/**
 * Observer Interface, is necessary for the communication between Model, Controller, View
 */
public interface Observer<T> {

    /**
     * Method that change the situation in a class that implements the interface
     *
     * @param message identifies information about the change
     */
        void update(T message);
}

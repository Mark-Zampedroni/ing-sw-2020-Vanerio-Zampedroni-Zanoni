package it.polimi.ingsw.utility.observer;

/**
 * Observer Interface, is necessary for the communication between Model, Controller, View.
 * Implements the Observer-Observable pattern
 */
public interface Observer<T> {

    /**
     * Method called by the {@link Observable observed object} when a defined event takes place
     *
     * @param message information about the change
     */
    void update(T message);

}
package it.polimi.ingsw.utility.observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * It's necessary for the communication between Model, Controller, View.
 * It saves a list of observer; implements the Observer-Observable pattern
 */
public class Observable<T> implements Serializable {

    private transient List<Observer<T>> observers;

    /**
     * Adds an {@link Observer observer} to the list of {@link Observer observers}
     *
     * @param observer identifies the {@link Observer observer}
     */
    public synchronized void addObserver(Observer<T> observer) {
        if (observers == null)
            observers = new ArrayList<>();
        observers.add(observer);
    }

    /**
     * Removes an {@link Observer observer} from the list of observers
     *
     * @param observer identifies the {@link Observer observer}
     */
    public synchronized void removeObserver(Observer<T> observer) {
        if (observers != null)
            observers.remove(observer);
    }

    /**
     * Calls the update method of all the {@link Observer observers}
     *
     * @param message information about the change
     */
    public synchronized void notify(T message) {
        if (observers != null) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }

}
package it.polimi.ingsw.utility.observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Observable Superclass, is necessary for the communication between Model, Controller, View.
 * It contains a list of observer.
 */
public class Observable<T> implements Serializable {

    private transient List<Observer<T>> observers;

    /**
     * Adds an {@link Observer observer} to the list of {@link Observer observers}
     *
     * @param observer identifies the {@link Observer observer}
     */
    public synchronized void addObserver(Observer<T> observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    /**
     * Removes an {@link Observer observer} from the list of {@link Observer observers}
     *
     * @param observer identifies the {@link Observer observer}
     */
    public synchronized void removeObserver(Observer<T> observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    /**
     * Call the {@link Observer update} method in all the {@link Observer observers} in the list
     *
     * @param message identifies information about the change
     */
    public synchronized void notify(T message) {
        if (observers != null) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }

}
package it.polimi.ingsw.observer.observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable Superclass, is necessary for the communication between Model, Controller, View.
 * It contains a list of observer.
 */
public class Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    /**
     * Adds a {@link Observer observer} in the list of the {@link Observer observers}
     *
     * @param observer identifies the {@link Observer observer}
     */
    public void addObservers(Observer<T> observer){
        observers.add(observer);
    }

    /**
     * Call the {@link Observer update} method in all the {@link Observer observers} in the list
     *
     * @param message identifies information about the change
     */
    public void notify(T message){
        for(Observer<T> observer: observers){
            observer.update(message);
        }
    }

}
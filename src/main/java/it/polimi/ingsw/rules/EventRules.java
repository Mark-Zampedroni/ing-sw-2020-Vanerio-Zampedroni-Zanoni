package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.map.Position;

import java.io.Serializable;

public abstract class EventRules extends CommonRules implements Serializable {

    private boolean event;
    private Position pos;

    /**
     * Getter for event
     *
     * @return a copy of {@link Boolean event}
     */
    public boolean getEvent() {return event;}

    /**
     * Setter for event
     *
     * @param event {@link Boolean boolean} variable used as a trigger for some god's powers
     */
    public void setEvent(boolean event) {this.event = event;}

    /**
     * Getter for pos
     *
     * @return a copy of {@link Position pos}
     */
    public Position getPos() {return pos;}

    /**
     * Setter for pos
     *
     * @param pos copy of a {@link Position parameter} with the purpose of keeping track of the worker's previous position
     */
    public void setPos(Position pos) {this.pos = pos;}

    /**
     * Method used for clearing previous value of {@link Boolean event}
     */
    @Override
    public void clear() { event = false; }
}

package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.map.Position;

import java.io.Serializable;

/**
 * Rules shared by the gods which rely on flags to choose the possible actions
 */
public abstract class EventRules extends CommonRules implements Serializable {

    private static final long serialVersionUID = -3930788283599303166L;
    private boolean event;
    private Position pos;

    /**
     * Getter for event
     *
     * @return a copy of {@link Boolean event}
     */
    public boolean getEvent() {
        return event;
    }

    /**
     * Setter for event
     *
     * @param event {@link Boolean boolean} variable used as a trigger for some god's powers
     */
    public void setEvent(boolean event) {
        this.event = event;
    }

    /**
     * Getter for pos
     *
     * @return a copy of {@link Position pos}
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Setter for pos
     *
     * @param pos copy of a {@link Position parameter} with the purpose of keeping track of the worker's previous position
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * Method used to clear the value of {@link Boolean event}
     */
    @Override
    public void clear() {
        event = false;
    }
}

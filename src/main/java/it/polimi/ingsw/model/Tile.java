package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class Tile {

    private Tower height;
    private boolean dome;
    private boolean hasWorker;

    public Tile() {
        height = Tower.GROUND; //GROUND
        dome = false;
    }

    public void placeDome() {
        dome = true;
    }

    // Dome Exception is checked in rules
    public void increaseHeight() {
        if(!dome) {

        }
    }

    // Dome is on getHeight level
    public boolean isComplete() {
        return dome;
    }

    // Does not include the Dome
    public Tower getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return "Tower su Tile è alta fino a " + height;
    }
}

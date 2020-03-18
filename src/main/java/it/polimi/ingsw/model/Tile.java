package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class Tile {

    private Tower height;
    private boolean completed;

    public Tile() {
        height = Tower.GROUND; //GROUND
        completed = false;
    }

    public void increaseHeight() {
        height = Tower.increase(height);
        if(height == Tower.DOME) {
                completed = true;
            }
    }

    public boolean getCompleted() {
        return completed;
    }

    public Tower getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return "Tower su Tile è alta fino a " + height;
    }
}

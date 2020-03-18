package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import static it.polimi.ingsw.constants.Height.*;

// MARK -- OK

public class Tile {

    private int height;
    private boolean dome;

    public Tile() {
        height = GROUND;
        dome = false;
    }

    public void placeDome() { dome = true; }

    public boolean hasDome() { return dome; }

    public void increaseHeight() throws CantBuildException {
        if(!hasDome()) {
            if(height == TOP) { placeDome(); }
            else { height += 1; }
        }
        else {
            throw new CantBuildException("The tower is already completed.");
        }
    }

    // Does not include the Dome
    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return (hasDome()) ? "Tower is completed." : "Tower height is " + height;
    }
}

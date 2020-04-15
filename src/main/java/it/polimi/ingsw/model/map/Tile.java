package it.polimi.ingsw.model.map;
import java.io.Serializable;

import static it.polimi.ingsw.constants.Height.*;

/**
 * Tile of the game map, 25 of them and are initialized when a {@link Board board} is created
 */
public class Tile implements Serializable {

    private int height;
    private boolean dome;

    /**
     * Initializes a tile with minimum {@link it.polimi.ingsw.constants.Height height}
     */
    public Tile() {
        height = GROUND;
        dome = false;
    }

    /**
     * Places a dome on the tile
     */
    public void putDome() { dome = true; }

    /**
     * Checks if the tile has a dome
     *
     * @return {@code true} if the tile has a dome
     */
    public boolean hasDome() { return dome; }

    /**
     * Increases the tile {@link it.polimi.ingsw.constants.Height height}, if it's already reached {@link it.polimi.ingsw.constants.Height TOP}
     * instead places a dome
     */
    public void increaseHeight() {
        if(!dome) {
            if(height == TOP) { dome = true; }
            else { height += 1; }
        }
    }

    /**
     * Gets the {@link it.polimi.ingsw.constants.Height height}
     *
     * @return the {@link it.polimi.ingsw.constants.Height height}
     */
    public int getHeight() {
        return height;
    }

    /**
     * Creates a String that contains the tile variables values
     *
     * @return a String with the tile attributes
     */
    @Override
    public String toString() {
        return "Tower, height: "+height+", dome: "+dome;
    }
}

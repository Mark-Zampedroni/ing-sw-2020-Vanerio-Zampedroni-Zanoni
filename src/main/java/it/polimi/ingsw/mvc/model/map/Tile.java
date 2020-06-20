package it.polimi.ingsw.mvc.model.map;

import java.io.Serializable;

import static it.polimi.ingsw.utility.constants.Height.*;

/**
 * Tile of the game map, 25 of them and are initialized when a {@link Board board} is created
 */
public class Tile implements Serializable {

    private static final long serialVersionUID = 326927623142205908L;
    private int height;
    private boolean hasDome;

    /**
     * Initializes a tile with minimum {@link it.polimi.ingsw.utility.constants.Height height}
     */
    public Tile() {
        height = GROUND;
        hasDome = false;
    }

    /**
     * Places a dome on the tile
     */
    public void putDome() {
        hasDome = true;
    }

    /**
     * Checks if the tile has a dome
     *
     * @return {@code true} if the tile has a dome
     */
    public boolean hasDome() {
        return hasDome;
    }

    /**
     * Increases the tile {@link it.polimi.ingsw.utility.constants.Height height}, if it's already reached {@link it.polimi.ingsw.utility.constants.Height TOP}
     * instead places a dome
     */
    public void increaseHeight() {
        if(!hasDome) {
            if(height == TOP) {
                hasDome = true;
            }
            else {
                height += 1;
            }
        }
    }

    /**
     * Gets the {@link it.polimi.ingsw.utility.constants.Height height}
     *
     * @return the {@link it.polimi.ingsw.utility.constants.Height height}
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
        return "Tower, height: "+height+", dome: "+ hasDome;
    }
}

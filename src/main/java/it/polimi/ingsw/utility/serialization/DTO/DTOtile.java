package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.MVC.model.map.Tile;

import java.io.Serializable;

public class DTOtile implements Serializable {
    private int height;
    private boolean dome;

    /**
     * Initializes a tile with minimum {@link it.polimi.ingsw.utility.constants.Height height}
     */
    public DTOtile(Tile tile) {
        height = tile.getHeight();
        dome = tile.hasDome();
    }

    /**
     * Checks if the tile has a dome
     *
     * @return {@code true} if the tile has a dome
     */
    public boolean hasDome() { return dome; }

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
        return "Tower, height: "+height+", dome: "+dome;
    }
}

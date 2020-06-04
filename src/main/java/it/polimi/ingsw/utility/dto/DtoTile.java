package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.map.Tile;

import java.io.Serializable;

public class DtoTile implements Serializable {
    private static final long serialVersionUID = -1540738678773220733L;
    private final int height;
    private final boolean dome;

    /**
     * Initializes a tile with minimum {@link it.polimi.ingsw.utility.constants.Height height}
     */
    public DtoTile(Tile tile) {
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

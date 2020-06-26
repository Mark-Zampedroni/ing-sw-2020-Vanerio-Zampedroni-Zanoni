package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.map.Tile;

import java.io.Serializable;

/**
 * Dto copy of {@link Tile tile}
 * The Dto classes are created and stored in some messages exchanged on the network,
 * their methods consist of only getters and all their variables are final
 */
public class DtoTile implements Serializable {
    private static final long serialVersionUID = -1540738678773220733L;
    private final int height;
    private final boolean dome;

    /**
     * Initializes a new DtoTile
     *
     * @param tile the tile to copy as a dto
     */
    public DtoTile(Tile tile) {
        height = tile.getHeight();
        dome = tile.hasDome();
    }

    /**
     * Checks if the DtoTile has a dome
     *
     * @return {@code true} if the dtoTile has a dome
     */
    public boolean hasDome() {
        return dome;
    }

    /**
     * Gets the {@link it.polimi.ingsw.utility.constants.Height height}
     *
     * @return the {@link it.polimi.ingsw.utility.constants.Height height}
     */
    public int getHeight() {
        return height;
    }

}

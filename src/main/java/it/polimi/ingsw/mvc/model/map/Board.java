package it.polimi.ingsw.mvc.model.map;

import java.io.Serializable;

/**
 * Game map, contains 25 {@link Tile tiles} and unique for a {@link it.polimi.ingsw.mvc.model.Session session},
 * also contains the {@link it.polimi.ingsw.mvc.model.player.Worker workers} during the game
 */
public class Board implements Serializable {

    public final int WIDTH = 5;
    public final int HEIGHT = 5;

    private final Tile[][] tiles = new Tile[WIDTH][HEIGHT];
    private static final long serialVersionUID = 30756855033943045L;

    /**
     * Initializes the board calling the method {@link #clear() clear}
     */
    public Board() {
        clear();
    }

    /**
     * Replaces all the {@link Tile tiles} in the board with new {@link Tile tiles}
     */
    public void clear() {
        for (int x=0; x<WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                tiles[x][y] = new Tile();
            }
        }
    }

    /**
     * Getter for a specific {@link Tile tile} identified by its {@link Position position}
     *
     * @param position the position of the required {@link Tile tile}
     * @return the {@link Tile tile} identified by the {@link Position position}
     */
    public Tile getTile(Position position) {
        return tiles[position.getX()][position.getY()];
    }
}

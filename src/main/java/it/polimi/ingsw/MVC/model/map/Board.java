package it.polimi.ingsw.MVC.model.map;

import java.io.Serializable;

/**
 * Game map, contains 25 {@link Tile tiles} and unique for a {@link it.polimi.ingsw.MVC.model.Session session},
 * also contains the {@link it.polimi.ingsw.MVC.model.player.Worker workers} during the game
 */
public class Board implements Serializable {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

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
        for (int x=0; x<Board.WIDTH; x++) {
            for (int y=0; y<Board.HEIGHT; y++) {
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
        return tiles[position.getX()][position.getY()]; }
}
package it.polimi.ingsw.model.map;

/**
 * Game map, contains 25 {@link Tile tiles} and unique for a {@link it.polimi.ingsw.model.Session session},
 * also contains the {@link it.polimi.ingsw.model.player.Worker workers} during the game
 */
public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    /**
     * Initializes the Board, it calls the method {@link #clear() clear}
     *
     */
    public Board() {
        clear();
    }

    /**
     * It replaces all the {@link Tile tiles} in the Board with new {@link Tile tiles}
     *
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

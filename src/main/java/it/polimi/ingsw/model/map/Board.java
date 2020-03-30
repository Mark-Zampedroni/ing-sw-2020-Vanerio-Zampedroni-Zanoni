package it.polimi.ingsw.model.map;


public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    public Board() {
        clear();
    }

    public void clear() {
        for (int x=0; x<Board.WIDTH; x++) {
            for (int y=0; y<Board.HEIGHT; y++) {
                tiles[x][y] = new Tile();
            }
        }
    }

    public Tile getTile(Position position) {
        return tiles[position.getX()][position.getY()]; }

}

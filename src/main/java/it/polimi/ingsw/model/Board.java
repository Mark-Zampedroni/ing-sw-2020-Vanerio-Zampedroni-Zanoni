package it.polimi.ingsw.model;

// STEFANO

public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    public static void resetBoard() {
        for(int i = 0; i < Board.WIDTH; i++) {
            for(int j = 0; j < Board.HEIGHT; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    // PLACEHOLDER, DA IMPLEMENTARE
    public static Tile getTile(Position position) { return null; }

}

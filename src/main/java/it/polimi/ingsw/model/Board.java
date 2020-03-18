package it.polimi.ingsw.model;

// STEFANO

public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    public void resetBoard() {
        for(int i = 0; i < Board.WIDTH; i++) {
            for(int j = 0; j < Board.HEIGHT; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

}

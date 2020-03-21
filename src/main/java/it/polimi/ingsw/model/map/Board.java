package it.polimi.ingsw.model.map;


import it.polimi.ingsw.model.player.Position;

public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    public Board () {
        for (int i=0; i<5; i++) {
            for (int j=0; i<5; i++) {
                tiles [i] [j] = new Tile();
            }
        }
    }

    public static void resetBoard() {
        for(int i = 0; i < Board.WIDTH; i++) {
            for(int j = 0; j < Board.HEIGHT; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public static Tile getTile(Position position) {
        return tiles[position.getX()][position.getY()]; }

}

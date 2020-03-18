package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.actions.CantBuildException;

public class Board {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private static Tile[][] tiles = new Tile[Board.WIDTH][Board.HEIGHT];

    public static void resetBoard() {
        for(int i = 0; i < Board.WIDTH; i++) {
            for(int j = 0; j < Board.HEIGHT; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public static void buildTowerOn(Player player, int x, int y) {
        try {
            player.getRules().consentBuild(Board.tiles[x][y]); // <--- dis per validare la costruzione
            Board.tiles[x][y].increaseHeight();
            System.out.println(player+" ha costruito in ("+x+","+y+") fino a "+Board.tiles[x][y].getHeight());
        }
        catch(CantBuildException e) {
            System.out.println("Errore CantBuildException");
        }
    }

}

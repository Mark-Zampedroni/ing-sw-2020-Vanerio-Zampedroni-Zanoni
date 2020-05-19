package it.polimi.ingsw.utility.serialization.dto;

import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Position;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Board board}
 */

public class DtoBoard implements Serializable {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;
    private static final long serialVersionUID = 2479300674565274263L;

    private final DtoTile[][] tiles = new DtoTile[Board.WIDTH][Board.HEIGHT];

    /**
     * Initializes the DTOboard
     *
     * @param board indicates his equivalent in server storage
     */
    public DtoBoard(Board board) {
        for (int x=0; x<WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                tiles[x][y] = new DtoTile(board.getTile(new Position(x,y)));
            }
        }
    }


    /**
     * Getter for a specific {@link DtoTile DTOtile} identified by its {@link DtoPosition DTOposition}
     *
     * @param position the DTOposition of the required {@link DtoTile DTOtile}
     * @return the {@link DtoTile DTOtile} identified by the {@link DtoPosition DTOposition}
     */
    public DtoTile getTile(DtoPosition position) {
        return tiles[position.getX()][position.getY()];
    }

    public DtoTile getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     * Implementation of toString method for the {@link DtoBoard DTOboard}
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int x=0; x<WIDTH; x++) {
            for(int y=0; y<HEIGHT; y++) {
                b.append("(").append(x).append(",").append(y).append(")").append(" : ").append(tiles[x][y].toString()).append("\n");
            }
        }
        return b.toString();
    }

}

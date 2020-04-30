package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.MVC.model.map.Board;
import it.polimi.ingsw.MVC.model.map.Position;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Board board}
 */

public class DTOboard implements Serializable {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    private final static DTOtile[][] tiles = new DTOtile[Board.WIDTH][Board.HEIGHT];

    /**
     * Initializes the DTOboard
     *
     * @param board indicates his equivalent in server storage
     */
    public DTOboard(Board board) {
        for (int x=0; x<Board.WIDTH; x++) {
            for (int y=0; y<Board.HEIGHT; y++) {
                tiles[x][y] = new DTOtile(board.getTile(new Position(x,y)));
            }
        }
    }


    /**
     * Getter for a specific {@link DTOtile DTOtile} identified by its {@link DTOposition DTOposition}
     *
     * @param position the DTOposition of the required {@link DTOtile DTOtile}
     * @return the {@link DTOtile DTOtile} identified by the {@link DTOposition DTOposition}
     */
    public DTOtile getTile(DTOposition position) {
        return tiles[position.getX()][position.getY()]; }
}

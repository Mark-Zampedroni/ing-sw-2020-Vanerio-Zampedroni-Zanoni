package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Position;

import java.io.Serializable;

/**
 * Dto copy of {@link Board board}.
 * The Dto classes are created and stored in some messages exchanged on the network,
 * their methods consist of only getters and all their variables are final
 */
public class DtoBoard implements Serializable {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;
    private static final long serialVersionUID = 2479300674565274263L;
    private final DtoTile[][] tiles = new DtoTile[WIDTH][HEIGHT];

    /**
     * Initializes the DtoBoard
     *
     * @param board indicates the board to copy as a dto
     */
    public DtoBoard(Board board) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new DtoTile(board.getTile(new Position(x, y)));
            }
        }
    }


    /**
     * Getter for a specific {@link DtoTile DtoTile} identified by its {@link DtoPosition DtoPosition}
     *
     * @param position the DtoPosition of the required {@link DtoTile DtoTile}
     * @return the {@link DtoTile DtoTile} identified by the {@link DtoPosition DtoPosition}
     */
    public DtoTile getTile(DtoPosition position) {
        return tiles[position.getX()][position.getY()];
    }

    /**
     * Getter for a specific {@link DtoTile DtoTile} identified by its coordinates
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @return the {@link DtoTile DtoTile} in position (x,y)
     */
    public DtoTile getTile(int x, int y) {
        return tiles[x][y];
    }


}

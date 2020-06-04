package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.utility.dto.DtoBoard;
import it.polimi.ingsw.utility.dto.DtoPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class DtoBoardTest {

    Board board;

    @BeforeEach
    void setUp() {
        board = Session.getInstance().getBoard();
        board.getTile(new Position(2,3)).increaseHeight();
        board.getTile(new Position(4, 4)).putDome();
    }

    @AfterEach
    void clear() {
        board.clear();
        board = null;
    }

    /**
     * Testing if is correctly created and if getTile method works in DTO version
     */
    @Test
    void correctBoard() {
        DtoBoard dtOboard = new DtoBoard(board);
        assertEquals(board.getTile(new Position(2,3)).getHeight(), dtOboard.getTile(new DtoPosition(new Position(2,3))).getHeight());
        assertEquals(board.getTile(new Position(4, 4)).hasDome(), dtOboard.getTile(new DtoPosition(new Position(4,4))).hasDome());
    }

    /**
     * Testing if is correct toString method
     */
    @Test
    void correctWrite() {
        DtoBoard dtOboard = new DtoBoard(board);
        assertEquals (dtOboard.toString(), "(0,0) : Tower, height: 0, dome: false\n" +
                "(0,1) : Tower, height: 0, dome: false\n" +
                "(0,2) : Tower, height: 0, dome: false\n" +
                "(0,3) : Tower, height: 0, dome: false\n" +
                "(0,4) : Tower, height: 0, dome: false\n" +
                "(1,0) : Tower, height: 0, dome: false\n" +
                "(1,1) : Tower, height: 0, dome: false\n" +
                "(1,2) : Tower, height: 0, dome: false\n" +
                "(1,3) : Tower, height: 0, dome: false\n" +
                "(1,4) : Tower, height: 0, dome: false\n" +
                "(2,0) : Tower, height: 0, dome: false\n" +
                "(2,1) : Tower, height: 0, dome: false\n" +
                "(2,2) : Tower, height: 0, dome: false\n" +
                "(2,3) : Tower, height: 1, dome: false\n" +
                "(2,4) : Tower, height: 0, dome: false\n" +
                "(3,0) : Tower, height: 0, dome: false\n" +
                "(3,1) : Tower, height: 0, dome: false\n" +
                "(3,2) : Tower, height: 0, dome: false\n" +
                "(3,3) : Tower, height: 0, dome: false\n" +
                "(3,4) : Tower, height: 0, dome: false\n" +
                "(4,0) : Tower, height: 0, dome: false\n" +
                "(4,1) : Tower, height: 0, dome: false\n" +
                "(4,2) : Tower, height: 0, dome: false\n" +
                "(4,3) : Tower, height: 0, dome: false\n" +
                "(4,4) : Tower, height: 0, dome: true\n");
    }

}

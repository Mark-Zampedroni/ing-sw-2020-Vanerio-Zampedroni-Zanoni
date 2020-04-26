package it.polimi.ingsw.serializationTest.DTOtest;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Board;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.utility.serialization.DTO.DTOboard;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOtile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class DTOboardTest {

    Board board;

    @BeforeEach
    void setUp() {
        board = Session.getInstance().getBoard();
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
        board.getTile(new Position(2,3)).increaseHeight();
        board.getTile(new Position(4, 4)).putDome();
        DTOboard dtOboard = new DTOboard(board);
        assertEquals(board.getTile(new Position(2,3)).getHeight(), dtOboard.getTile(new DTOposition(new Position(2,3))).getHeight());
        assertEquals(board.getTile(new Position(4, 4)).hasDome(), dtOboard.getTile(new DTOposition(new Position(4,4))).hasDome());
    }
}

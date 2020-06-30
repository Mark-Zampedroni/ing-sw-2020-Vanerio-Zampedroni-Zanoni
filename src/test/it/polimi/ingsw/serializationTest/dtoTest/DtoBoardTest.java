package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.utility.dto.DtoBoard;
import it.polimi.ingsw.utility.dto.DtoPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

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


     //Tests if is correctly created and if getTile method works in DTO version
    @Test
    void correctBoard() {
        DtoBoard dtOboard = new DtoBoard(board);
        assertEquals(board.getTile(new Position(2,3)).getHeight(), dtOboard.getTile(new DtoPosition(new Position(2,3))).getHeight());
        assertEquals(board.getTile(new Position(4, 4)).hasDome(), dtOboard.getTile(new DtoPosition(new Position(4,4))).hasDome());
    }

}

package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanRulesTest {

    private Player player;
    private Worker worker;
    private final GodRules test = GodRules.getInstance(Gods.PAN);

   @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    @Test
    void isWinner() {
        Board board = Session.getInstance().getBoard();
        worker.setPosition(2,2);
        Position position = new Position(1, 2);
        while(board.getTile(position).getHeight()<3){board.getTile(position).increaseHeight();}
        while (board.getTile(worker.getPosition()).getHeight() < 2) {board.getTile(worker.getPosition()).increaseHeight();}
        assert test != null;
        assertTrue(test.isWinner(worker, position));
        while (board.getTile(worker.getPosition()).getHeight() < 3) {board.getTile(worker.getPosition()).increaseHeight();}
        position = new Position(2,3);
        assertTrue(test.isWinner(worker, position));
    }
}
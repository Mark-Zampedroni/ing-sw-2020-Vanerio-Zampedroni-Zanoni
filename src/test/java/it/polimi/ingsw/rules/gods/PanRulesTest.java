package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.gods.PanRules;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanRulesTest {

    Player player;
    Worker worker;
    Position position = new Position(0,0);
    GodRules test= Gods.PAN.createRules();

   @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = new Player("TestName");
        Session.getInstance().addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Session.getInstance().removePlayer(player);
    }

    @Test
    void isWinner() {
        Board board = Session.getInstance().getBoard();
        worker.setPosition(2,2);
        position = new Position(1, 2);
        while(board.getTile(position).getHeight()<3){board.getTile(position).increaseHeight();}
        while (board.getTile(worker.getPosition()).getHeight() < 2) {board.getTile(worker.getPosition()).increaseHeight();}
        assertTrue(test.isWinner(worker, position));
        while (board.getTile(worker.getPosition()).getHeight() < 3) {board.getTile(worker.getPosition()).increaseHeight();}
        position = new Position(2,3);
        assertTrue(test.isWinner(worker, position));
    }
}
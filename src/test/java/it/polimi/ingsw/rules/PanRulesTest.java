package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.PanRules;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanRulesTest {

    /*
    Player player;
    Worker worker;
    Position position;
    PanRules test= new PanRules();

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2, 2));
        position= new Position(3,3 );
        Session.getBoard().clear();
    }

    @AfterEach
    void clear() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }

    @Test
    void isWinner() {
        Board board = Session.getBoard();
        position.setValue(1, 2);
        while(board.getTile(position).getHeight()<3){board.getTile(position).increaseHeight();}
        while (board.getTile(worker.getPosition()).getHeight() < 2) {board.getTile(worker.getPosition()).increaseHeight();}
        assertTrue(test.isWinner(worker, position));
        while (board.getTile(worker.getPosition()).getHeight() < 3) {board.getTile(worker.getPosition()).increaseHeight();}
        position.setValue(2,3);
        assertTrue(test.isWinner(worker, position));
    }*/
}
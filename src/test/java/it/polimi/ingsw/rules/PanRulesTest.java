package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.PanRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanRulesTest {
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
        Board.clear();
    }
    @Test
    void isWinner() {
        Board.clear();
        position.setValue(1, 2);
        while(Board.getTile(position).getHeight()<3){Board.getTile(position).increaseHeight();}
        while (Board.getTile(worker.getPosition()).getHeight() < 2) {Board.getTile(worker.getPosition()).increaseHeight();}
        assertTrue(test.isWinner(worker, position));
        while (Board.getTile(worker.getPosition()).getHeight() < 3) {Board.getTile(worker.getPosition()).increaseHeight();}
        position.setValue(2,3);
        assertTrue(test.isWinner(worker, position));

    }
}
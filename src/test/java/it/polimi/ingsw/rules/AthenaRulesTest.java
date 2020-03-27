package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AthenaRulesTest {
    Player player;
    Worker worker;
    Position position;
    AthenaRules test= new AthenaRules();

    @BeforeEach
    void setUp() {

        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2, 3));
        position=new Position(2,2);
        Board.clear();
    }
    @Test
    void blockedByEnemy() {
        test.setEvent(true);
        Board.getTile(position).increaseHeight();
        assertTrue(test.blockedByEnemy(worker, position));

    }

}

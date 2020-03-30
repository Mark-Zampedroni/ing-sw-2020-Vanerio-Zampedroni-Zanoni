package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.HestiaRules;
import org.junit.jupiter.api.BeforeEach;

class HestiaRulesTest {
    Player player;
    Worker worker;
    Position position;
    HestiaRules test;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(3, 3));
        position = new Position(4, 4);
        test = new HestiaRules();
        Session.getBoard().clear();
    }
    /*
    @Test
    void consentBuild() {
        test.setEvent(true);
        assertThrows(BuildGodPowerException.class, ()->test.consentBuild(worker, position));
    }*/
}
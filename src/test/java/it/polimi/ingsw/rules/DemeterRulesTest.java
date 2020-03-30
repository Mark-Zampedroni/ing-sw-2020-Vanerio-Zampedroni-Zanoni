package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.DemeterRules;
import org.junit.jupiter.api.BeforeEach;

class DemeterRulesTest {
    Player player;
    Worker worker;
    Position position;
    DemeterRules test= new DemeterRules();


    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2,3));
        Session.getBoard().clear();

    }

    /*
    @Test
    void consentBuild() {
        test.setEvent(true);
        test.setPos(new Position(2,2));
        assertThrows(BuildGodPowerException.class, ()->test.consentBuild(worker, new Position(2,2)));
    }*/


}
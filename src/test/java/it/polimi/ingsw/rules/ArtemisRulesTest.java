package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ArtemisRules;
import org.junit.jupiter.api.BeforeEach;

class ArtemisRulesTest {
    Player player;
    Worker worker;
    Position position;
    ArtemisRules test= new ArtemisRules();

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
    void consentMovement() throws CantMoveException {
        test.setEvent(true);
        test.setPos(new Position(2,2));
        assertThrows(MoveGodPowerException.class, ()->test.consentMovement(worker, new Position(2,2)));
    }*/


}
package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.DemeterRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemeterRulesTest {
    Player player;
    Worker worker;
    DemeterRules test= new DemeterRules();


    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }


    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> test.consentBuild(worker, new Position(2, 2)));
        test.setEvent(true);
        test.setPos(new Position(2, 2));
        assertThrows(CantActException.class, () -> test.consentBuild(worker, new Position(2, 2)));


    }
}
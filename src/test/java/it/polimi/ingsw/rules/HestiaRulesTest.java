package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.building.BuildGodPowerException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        Board.clear();
    }
    @Test
    void consentBuild() {
        test.setEvent(true);
        assertThrows(BuildGodPowerException.class, ()->test.consentBuild(worker, position));

    }
}
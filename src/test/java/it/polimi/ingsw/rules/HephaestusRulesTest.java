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

class HephaestusRulesTest {
    Player player;
    Worker worker;
    Position position;
    HephaestusRules test;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2,2));
        position= new Position(3,3 );
        test= new HephaestusRules();
        Board.clear();


    }

    @Test
    void consentBuild() {
        test.setEvent(true);
        test.setPos(new Position(3,3));
        assertThrows(BuildGodPowerException.class, ()->test.consentBuild(worker, new Position(2,1)));
    }
}
package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.MinotaurRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinotaurRulesTest {

    /*
    Player player;
    Player player2;
    Worker worker;
    Position position;
    MinotaurRules rule;
    Worker worker2;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(3, 2));
        position = new Position(4, 3);
        rule = new MinotaurRules();
        Session.getBoard().clear();
    }

    @AfterEach
    void clear() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }*/

    /*
    @Test
    void consentMovement() {
            player2 = new Player("TestName2");
            Session.addPlayer(player2);
            worker2 = player2.getWorkers().get(0);
            assertThrows(OutofBorderException.class, ()->rule.consentMovement(worker, position));
            position.setValue(3,3);
            worker2.setPosition(new Position(3,4));
            assertThrows(MoveGodPowerException.class, ()->rule.consentMovement(worker, position));


        }*/
/*
    @Test
    void getPositionBackwards() {
        worker.setPosition(new Position(2,2));
        player2 = new Player("TestName2");
        Session.addPlayer(player2);
        worker2 = player2.getWorkers().get(0);
        worker2.setPosition(new Position(3,3));
        assertTrue(rule.getPositionBackwards(worker.getPosition(), position).equals(new Position(4,4)));

    }*/



}
package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ArtemisRules;
import it.polimi.ingsw.rules.gods.MinotaurRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinotaurRulesTest {


    Player player;
    Player player2;
    Worker worker;
    MinotaurRules test= new MinotaurRules();
    ArtemisRules test2 = new ArtemisRules();
    Worker worker2;

    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
        player2 = new Player("TestName2");
        Session.addPlayer(player2);
        worker2 = player2.getWorkers().get(0);
        player2.setRules(test2);

    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
        Session.removePlayer(player2);

    }


    @Test
    void consentMovement() {
        int i;
        Position position= new Position(0,0);
        worker.setPosition(new Position(1,1));
        worker2.setPosition(new Position(2,2));
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(2,2)));
        worker2.setPosition(position);
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position)); //is valid
        position.setValue(2,2);
        worker2.setPosition(position);
        player2.getWorkers().get(1).setPosition(3,3);
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position));
        player2.getWorkers().get(1).setPosition(4,4);
        for(i=0; i<4; i++){Session.getBoard().getTile(new Position(3,3)).increaseHeight();}
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position));


        }

    @Test
    void getPositionBackwards() {
        Position position= new Position(3,3);
        worker.setPosition(new Position(2,2));
        worker2.setPosition(new Position(3,3));
        assertTrue(test.getPositionBackwards(worker.getPosition(), position).equals(new Position(4,4)));

    }

    @Test
    void executeMove() {
        Position oldPosition = new Position (1,1);
        Position position = new Position (2,2);
        Position forwardPosition = new Position (3,3);
        worker.setPosition(oldPosition);
        worker2.setPosition(position);
        test.executeMove(worker, position);
        assertTrue(position.equals(worker.getPosition()));
        assertTrue(forwardPosition.equals(worker2.getPosition()));
    }

}
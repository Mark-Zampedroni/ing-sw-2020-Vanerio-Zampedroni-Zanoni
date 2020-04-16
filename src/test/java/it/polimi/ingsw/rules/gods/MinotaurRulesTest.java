package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;
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
    MinotaurRules test= (MinotaurRules) Gods.MINOTAUR.createRules();
    GodRules test2 = Gods.ARTEMIS.createRules();
    Worker worker2;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        player.setRules(test);
        player2 = Setupper.addPlayer("TestName2", Colors.WHITE,2);
        worker2 = player2.getWorkers().get(0);
        player2.setRules(test2);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(player2);
    }

    @Test
    void consentMovement() {
        Position position= new Position(4,4);
        worker.setPosition(new Position(3,3));
        worker2.setPosition(new Position(2,2));
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(2,2)));
        worker2.setPosition(position);
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position)); //is valid
        Position position2 = new Position(2,3);
        worker2.setPosition(position2);
        player2.getWorkers().get(1).setPosition(1,3);
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position2));
        player2.getWorkers().get(1).setPosition(1,1);
        for(int i=0; i<4; i++){Session.getInstance().getBoard().getTile(new Position(1,3)).increaseHeight();}
        assertThrows(CantActException.class, ()->test.consentMovement(worker, position2));
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
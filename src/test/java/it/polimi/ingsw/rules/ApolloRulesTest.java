package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ApolloRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApolloRulesTest {
    Player player;
    Worker worker;
    Position position;
     ApolloRules test;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2,2));
        position= new Position(3,3 );
        player.getWorkers().get(1).setPosition(position);
        test= new ApolloRules();
        Board.clear();
    }


    /*@Test
    void consentMovement() {
        assertThrows(MoveOnAllyWorkerException.class, () ->test.consentMovement(worker, position));

    }*/
}
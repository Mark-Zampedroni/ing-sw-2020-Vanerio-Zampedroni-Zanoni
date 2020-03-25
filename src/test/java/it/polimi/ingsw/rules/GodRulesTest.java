package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GodRulesTest {
    Player player;
    Worker worker;
    Position position;
    GodRules rule;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2,2));
        position= new Position(3,3 );
        rule= new GodRules();
        Board.clear();


    }

    @Test
    void consentBuild() throws CantBuildException {

        OutofReach();
        assertThrows(OutOfReachException.class, () ->rule.consentBuild(worker, position));
        AlreadyCompleted();
        assertThrows(AlreadyCompleteException.class, () -> rule.consentBuild(worker, position));
        AlreadyOccupied();
        assertThrows(AlreadyOccupiedException.class, ()-> rule.consentBuild(worker, position));

    }

     void AlreadyCompleted(){
        position.setValue(3,3);
        Board.getTile(position).placeDome();
    }

     void OutofReach(){
        position.setValue(5,5);
    }

    void AlreadyOccupied(){
        position.setValue(2,3);
        player.getWorkers().get(1).setPosition(position);

    }




    @Test
    void consentMovement() throws CantMoveException {
        OutofReach();
        assertThrows(MoveOutsideRangeException.class, () -> rule.consentMovement(worker, position));
        AlreadyCompleted();
        assertThrows(DomeMoveException.class, () -> rule.consentMovement(worker, position));
        ClimbMove();
        assertThrows(ClimbMoveException.class, () -> rule.consentMovement(worker, position));
        AlreadyOccupied();
        assertThrows(MoveOnWorkerException.class, () -> rule.consentMovement(worker, position));

    }


     void ClimbMove() {
        int i;
        position.setValue(2, 1);
        if (Board.getTile(worker.getPosition()).getHeight() < 2) {
            for (i = 0; i < Board.getTile(worker.getPosition()).getHeight() + 2; i++) {
                Board.getTile(position).increaseHeight();
            }
        }
    }



    @Test
    void isWinner(){
        position.setValue(1, 2);
        while(Board.getTile(position).getHeight()<3){Board.getTile(position).increaseHeight();}
        while (Board.getTile(worker.getPosition()).getHeight() < 2) {Board.getTile(worker.getPosition()).increaseHeight();}
        assertTrue(rule.isWinner(worker, position));
    }


}
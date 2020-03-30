package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommonRulesTest {

    /*
    Player player;
    Worker worker;
    Position position;
    CommonRules rule;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        worker.setPosition(new Position(2,2));
        position= new Position(3,3 );
        rule= new CommonRules();
        Session.getBoard().clear();
    }

    @AfterEach
    void clear() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }
*/
    /*
    @Test
    void consentBuild() throws CantBuildException {

        OutOfReach();
        assertThrows(OutOfReachException.class, () ->rule.consentBuild(worker, position));
        AlreadyCompleted();
        assertThrows(AlreadyCompleteException.class, () -> rule.consentBuild(worker, position));
        AlreadyOccupied();
        assertThrows(AlreadyOccupiedException.class, ()-> rule.consentBuild(worker, position));

    }*/
/*
     void AlreadyCompleted(){
         position.setValue(3,3);
         Session.getBoard().getTile(position).placeDome();
    }

    void OutOfReach(){
        position.setValue(5,5);
    }

    void AlreadyOccupied(){
        position.setValue(2,3);
        player.getWorkers().get(1).setPosition(position);

    }

*/

/*
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

    }*/
/*

     void ClimbMove() {
         Board board = Session.getBoard();
         position.setValue(2, 1);
         if (board.getTile(worker.getPosition()).getHeight() < 2) {
            for (int i = 0; i < board.getTile(worker.getPosition()).getHeight() + 2; i++) {
                board.getTile(position).increaseHeight();
            }
        }
    }

    @Test
    void isWinner(){
         Board board = Session.getBoard();
         position.setValue(1, 2);
         while(board.getTile(position).getHeight()<3){board.getTile(position).increaseHeight();}
         while (board.getTile(worker.getPosition()).getHeight() < 2) {board.getTile(worker.getPosition()).increaseHeight();}
         assertTrue(rule.isWinner(worker, position));
    }
*/

}
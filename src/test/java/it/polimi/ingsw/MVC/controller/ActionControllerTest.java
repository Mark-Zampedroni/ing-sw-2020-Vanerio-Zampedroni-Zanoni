package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.controller.states.TurnController;
import it.polimi.ingsw.MVC.controller.states.actionControl.ActionController;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.gods.ApolloRules;

import it.polimi.ingsw.MVC.model.rules.gods.AtlasRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;

import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import org.junit.experimental.theories.internal.SpecificDataPointsSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActionControllerTest {

        ActionController actionController;
        Player player;

        @BeforeEach
        void setUp() {
                player = new Player("Tester", Colors.WHITE);
                player.setRules(new AtlasRules());
                actionController = new ActionController(null, player);
                Session.getInstance().addPlayer(player);
                player.addWorker(new Position(2, 2));
                player.addWorker(new Position(3, 4));
        }

        @AfterEach
        void clearUp() {
                actionController = null;
                Session.getInstance().removePlayer(player);
                Session.getInstance().getBoard().clear();
                player = null;
        }

        @Test
        void wrongAct() {
                assertThrows(WrongActionException.class, ()
                        -> actionController.act(new Worker(new Position(2, 3)), new Position(2, 4), Action.WIN));
        }

        @Test
        void getCandidates() {
                Worker worker = new Worker(new Position(2, 3));
                assertTrue(actionController.getCandidates(worker, Action.WIN).isEmpty());
                assertTrue(actionController.getCandidates(worker, Action.END_TURN).isEmpty());
                List<Position> temp;
                temp = fullListMoveBuild();
                for (int i = 0; i < temp.size(); i++) {
                        assertTrue(actionController.getCandidates(worker, Action.MOVE).get(i).equals(temp.get(i)));
                }
                Session.getInstance().getBoard().getTile(new Position(1, 2)).putDome();
                for (int i = 1; i < temp.size(); i++) {
                        assertTrue(actionController.getCandidates(worker, Action.BUILD).get(i - 1).equals(temp.get(i)));
                }
        }

        @Test
        void getCandidatesSelectAdd() {
                List<Position> temp;
                temp = fullListAddSelect();

                for (int i = 0; i < temp.size(); i++) {
                        assertTrue(actionController.getCandidates(player.getWorkers().get(0), Action.ADD_WORKER).get(i).equals(temp.get(i)));
                }
                temp = new ArrayList<>();
                temp.add(new Position(2,2));
                temp.add(new Position(3,4));
                for (int i = 0; i < temp.size(); i++) {
                        assertTrue(actionController.getCandidates(player.getWorkers().get(0), Action.SELECT_WORKER).get(i).equals(temp.get(i)));
                }
        }

        @Test
        void actMoveBuildTest() {
                try {
                        assertEquals(actionController.act(player.getWorkers().get(0), new Position(2, 3), Action.MOVE).get(0),
                                        Action.BUILD);
                        assertTrue(player.getWorkers().get(0).getPosition().equals(new Position(2,3)));
                        setWinCon();
                        assertEquals(actionController.act(player.getWorkers().get(1), new Position(4, 4), Action.MOVE).get(0),
                                Action.WIN);
                        assertTrue(player.getWorkers().get(1).getPosition().equals(new Position(4,4)));
                        assertEquals(actionController.act(player.getWorkers().get(0), new Position(2, 4), Action.BUILD).get(0),
                                Action.END_TURN);
                        assertEquals(Session.getInstance().getBoard().getTile(new Position(2,4)).getHeight(), 1);
                } catch (WrongActionException e) {
                        System.out.println("Non succede nei test");
                }
        }

        List<Position> fullListAddSelect(){
                List<Position> temp = new ArrayList<>();

                temp.add(new Position(0,0));
                temp.add(new Position(0,1));
                temp.add(new Position(0,2));
                temp.add(new Position(0,3));
                temp.add(new Position(0,4));
                temp.add(new Position(1,0));
                temp.add(new Position(1,1));
                temp.add(new Position(1,2));
                temp.add(new Position(1,3));
                temp.add(new Position(1,4));
                temp.add(new Position(2,0));
                temp.add(new Position(2,1));
                temp.add(new Position(2,3));
                temp.add(new Position(2,4));
                temp.add(new Position(3,0));
                temp.add(new Position(3,1));
                temp.add(new Position(3,2));
                temp.add(new Position(3,3));
                temp.add(new Position(4,0));
                temp.add(new Position(4,1));
                temp.add(new Position(4,2));
                temp.add(new Position(4,3));
                temp.add(new Position(4,4));
                return temp;
        }

        List<Position> fullListMoveBuild(){
                List<Position> temp = new ArrayList<>();
                temp.add(new Position(1,2));
                temp.add(new Position(1,3));
                temp.add(new Position(1,4));
                temp.add(new Position(2,4));
                temp.add(new Position(3,2));
                temp.add(new Position(3,3));
                return temp;
        }

        void setWinCon(){
                Session.getInstance().getBoard().getTile(new Position(3,4)).increaseHeight();
                Session.getInstance().getBoard().getTile(new Position(3,4)).increaseHeight();
                Session.getInstance().getBoard().getTile(new Position(4,4)).increaseHeight();
                Session.getInstance().getBoard().getTile(new Position(4,4)).increaseHeight();
                Session.getInstance().getBoard().getTile(new Position(4,4)).increaseHeight();
        }
}




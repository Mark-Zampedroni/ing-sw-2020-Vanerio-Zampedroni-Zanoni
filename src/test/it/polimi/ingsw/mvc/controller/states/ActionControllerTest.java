package mvc.controller.states;

import it.polimi.ingsw.mvc.controller.states.actionControl.ActionController;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;

import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.mvc.model.rules.gods.*;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;

import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
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
        Session session;

        @BeforeEach
        void setUp() {
                player = new Player("Tester", Colors.WHITE);
                player.setRules(new PanRules());
                actionController = new ActionController(null, player);
                session = Session.getInstance();
                session.addPlayer(player);
                try {
                        actionController.act(null, new Position(2, 2), Action.ADD_WORKER);
                        actionController.act(null, new Position(3, 4), Action.ADD_WORKER);
                } catch(WrongActionException e) { e.printStackTrace(); }
        }

        @AfterEach
        void clearUp() {
                actionController = null;
                session.removePlayer(player);
                session.getBoard().clear();
                player = null;
        }

        @Test
        void wrongAct() {
                try {
                        actionController.act(null, new Position(2, 2), Action.SELECT_WORKER);
                } catch (WrongActionException e) { e.printStackTrace(); }
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
                for(int x = 0; x < 5; x++) {
                        for(int y = 0; y < 5; y++) {
                                if(!((x == 2 && y == 2) || (x == 3 && y == 4))) {
                                        temp.add(new Position(x,y));
                                }
                        }
                }
                return temp;
        }

        List<Position> fullListMoveBuild(){
                List<Position> temp = new ArrayList<>();
                for(int y=2; y<5; y++) { temp.add(new Position(1,y)); }
                temp.add(new Position(2,4));
                for(int y=2; y<4; y++) { temp.add(new Position(3,y)); }
                return temp;
        }

        void setWinCon(){
                for(int times=0; times<2; times++) { session.getBoard().getTile(new Position(3,4)).increaseHeight(); }
                for(int times=0; times<3; times++) { session.getBoard().getTile(new Position(4,4)).increaseHeight(); }
        }
}




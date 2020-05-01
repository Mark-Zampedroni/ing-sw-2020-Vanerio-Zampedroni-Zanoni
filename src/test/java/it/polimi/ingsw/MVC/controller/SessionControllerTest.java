package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionControllerTest {

    SessionController sessionController;

    @BeforeEach
    void setUp() {
        Session.getInstance().addPlayer("tester", Colors.WHITE);
        Logger logger = Logger.getLogger("testing");
        sessionController = new SessionController(null, logger);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().removePlayer("tester");
    }

    @Test
    void withoutConnection(){
        assertEquals(Session.getInstance(), sessionController.getSession());
        assertEquals(sessionController.getState(), GameState.LOBBY);
        sessionController.setTurnOwner("tester");
        assertEquals(sessionController.getTurnOwner(), "tester");
        assertFalse(sessionController.isGameStarted());
        sessionController.setGameCapacity(3);
        assertEquals(sessionController.getGameCapacity(),3);
        assertEquals(sessionController.getPlayers(), Session.getInstance().getPlayers());
        assertEquals(sessionController.getFreeColors().get(0), Colors.BLUE);
        assertEquals(sessionController.getFreeColors().get(1), Colors.BROWN);
    }
}

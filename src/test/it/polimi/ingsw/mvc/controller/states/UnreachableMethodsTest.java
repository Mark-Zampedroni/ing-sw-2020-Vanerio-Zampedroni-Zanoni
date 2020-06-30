package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class UnreachableMethodsTest {

    SessionController controller;
    SelectionController wrongState;
    Logger LOG;

    @BeforeEach
    public void setUp() {
        LOG = Logger.getLogger("UnreachableTest");
        LOG.setUseParentHandlers(false);
        controller = new SessionController(new ArrayList<>(), LOG);
        Setupper.addPlayer("UnTest",Colors.BLUE, 0);
        wrongState = new SelectionController(controller, new ArrayList<>(), LOG);
    }

    // Tests the impossible calls
    @Test
    public void impossibleCallsTest() {
        wrongState.sendUpdate();
        wrongState.addUnregisteredView(null);
        wrongState.addPlayer("UnTest", Colors.WHITE, null);
        assertEquals(new ArrayList<>(), wrongState.getFreeColors());
    }

    @AfterEach
    public void clearUp() {
        wrongState.removePlayer("UnTest");
        Session.getInstance().getBoard().clear();
    }

}

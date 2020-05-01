package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.TestClient;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FSMTest {

    private static final int testPort = 7357; // 7 3 5 7 = t e s t
    private static Server server;
    private static TestClient testClient, testClient2;


    // Un metodo solo perchè altrimenti li runna in disordine, ci deve essere continuità
    @Test
    void interactionTest() {
        // Crea server
        openServer();
        startClients();
        // [PRE-LOBBY] Requests two players
        request(() -> testClient.requestTwoPlayers());
        // [LOBBY] Register two players
        request(() -> testClient.requestRegister("TestClient", Colors.BLUE));
        removePlayerDuringLogin();
        wrongRequestLogin();
        request(() -> testClient2.requestRegister("TestClient2", Colors.WHITE));
        // [CHALLENGER SELECTION]
        challengerChooseGods();
        // [PLAYER GOD SELECTION]
        playersChooseGods();
        // [STARTER SELECTION]
        starterPlayerSelection();
        // [GAME MODE]
        placeWorkerOnBoard();
        turnFirstPlayer();
        turnSecondPlayer();
        // Pulisce per gli altri test
        request(this::disconnectClients);
    }

    private void openServer() {
        server = new Server(testPort);
    }

    private void startClients() {
        testClient = new TestClient("127.0.0.1", testPort,0);
        testClient.createConnection("127.0.0.1", testPort);
        testClient2 = new TestClient("127.0.0.1", testPort,0);
        testClient2.createConnection("127.0.0.1", testPort);
    }

    private void disconnectClients() {
        if(!Session.getInstance().getPlayers().isEmpty()) {
            Session.getInstance().removePlayer("TestClient");
            Session.getInstance().removePlayer("TestClient2");
        }
        server.interrupt();
        testClient.interrupt();
    }

    // Aggiunge delay tra i messaggi
    private void request(Runnable request) {
        try {
            Thread.sleep(200);
            request.run();
        } catch(InterruptedException e) { e.printStackTrace(); }

    }

    private void starterPlayerSelection() {
        request(() -> testClient.requestStarterPlayer("TestClient", "TestClient"));
        request(() -> testClient2.requestStarterPlayer("TestClient2", "TestClient"));
    }


    private void wrongRequestLogin() {
        request(() -> testClient2.requestRegister("TestClient", Colors.BLUE));
        request(() -> testClient2.requestRegister("TestClient2", Colors.BLUE));
    }

    private void removePlayerDuringLogin() {
        request(() -> testClient2.interrupt());
        request(() -> testClient.requestRegister("TestClient", Colors.BLUE));
    }

    private void challengerChooseGods(){
        request(() -> testClient.requestChallengerGod("TestClient", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient.requestChallengerGod("TestClient", Gods.ATHENA.toString().toUpperCase()));
        request(() -> testClient2.requestChallengerGod("TestClient2", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient2.requestChallengerGod("TestClient2", Gods.ATHENA.toString().toUpperCase()));
    }

    private void playersChooseGods(){
        request(() -> testClient.requestPlayerGod("TestClient", Gods.ATHENA.toString().toUpperCase()));
        request(() -> testClient.requestPlayerGod("TestClient", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient2.requestPlayerGod("TestClient2", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient2.requestPlayerGod("TestClient2", Gods.ATHENA.toString().toUpperCase()));
        request(() -> testClient.requestPlayerGod("TestClient", Gods.ATHENA.toString().toUpperCase()));
        request(() -> testClient.requestPlayerGod("TestClient", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient2.requestPlayerGod("TestClient2", Gods.ATLAS.toString().toUpperCase()));
        request(() -> testClient2.requestPlayerGod("TestClient2", Gods.ATHENA.toString().toUpperCase()));
    }

    private void placeWorkerOnBoard() {
        request(() -> testClient.requestAction("TestClient", Action.ADD_WORKER, new DTOposition(new Position(1,1))));
        request(() -> testClient.requestAction("TestClient", Action.ADD_WORKER, new DTOposition(new Position(2,2))));
        request(() -> testClient2.requestAction("TestClient2", Action.ADD_WORKER, new DTOposition(new Position(3,3))));
        request(() -> testClient2.requestAction("TestClient2", Action.ADD_WORKER, new DTOposition(new Position(4,4))));
    }

    private void turnSecondPlayer() {
        request(() -> testClient2.requestAction("TestClient2", Action.SELECT_WORKER, new DTOposition(new Position(3,3))));
        request(() -> testClient2.requestAction("TestClient2", Action.MOVE, new DTOposition(new Position(3,4))));
        request(() -> testClient2.requestAction("TestClient2", Action.BUILD, new DTOposition(new Position(3,3))));
        request(() -> testClient2.requestAction("TestClient2", Action.END_TURN, new DTOposition(new Position(4,4))));
    }

    private void turnFirstPlayer() {
        request(() -> testClient.requestAction("TestClient", Action.SELECT_WORKER, new DTOposition(new Position(1,1))));
        request(() -> testClient.requestAction("TestClient", Action.MOVE, new DTOposition(new Position(1,2))));
        request(() -> testClient.requestAction("TestClient", Action.BUILD, new DTOposition(new Position(1,1))));
        request(() -> testClient.requestAction("TestClient", Action.END_TURN, new DTOposition(new Position(1,1))));
    }
}
package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.TestClient;
import it.polimi.ingsw.MVC.TestServer;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import org.junit.jupiter.api.*;

class FSMTest {

    private final int testPort = 7357; // 7 3 5 7 = t e s t
    private TestServer server;
    private TestClient testClient, testClient2;
    private TestClient challengerClient, otherClient;

    @Test
    void completeGameTest() {
        // Server and clients start
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
        firstTurnPoseidon();
        secondTurnArtemis();
        thirdTurnPoseidon();
        fourthTurnArtemis();
        // Checks if artemis won
        // -> Manca il metodo, appena si fa possiamo aggiungere l'assertTrue
        // Test clearUp
        request(this::disconnectClients);
    }

    private void openServer() {
        server = new TestServer(testPort);
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
        testClient2.interrupt();
    }

    // Aggiunge delay tra i messaggi
    private void request(Runnable request) {
        try {
            Thread.sleep(180); // Time between messages
            request.run();
        } catch(InterruptedException e) { e.printStackTrace(); }

    }

    private void starterPlayerSelection() {
        request(() -> challengerClient.requestStarterPlayer(challengerClient.getUsername()));
    }

    private void wrongRequestLogin() {
        request(() -> testClient2.requestRegister("TestClient","TestClient", Colors.BLUE));
        request(() -> testClient2.requestRegister("TestClient2", Colors.BLUE));
    }

    private void removePlayerDuringLogin() {
        request(() -> testClient.interrupt()); // Disconnect connection
        testClient = new TestClient("127.0.0.1", testPort,0); // Create new connection
        testClient.createConnection("127.0.0.1", testPort);
        request(() -> testClient.requestTwoPlayers());
        request(() -> testClient.requestRegister("TestClient", Colors.BLUE));
    }

    private void challengerChooseGods(){
        request(() -> System.out.println("\nWaiting 180ms ...\n"));
        challengerClient = (testClient.isChallenger()) ? testClient : testClient2;
        otherClient = (testClient == challengerClient) ? testClient2 : testClient;

        request(() -> challengerClient.requestChallengerGod(Gods.POSEIDON.toString().toUpperCase()));
        request(() -> challengerClient.requestChallengerGod(Gods.ARTEMIS.toString().toUpperCase()));
    }

    private void playersChooseGods(){
        request(() -> otherClient.requestPlayerGod(Gods.ARTEMIS.toString().toUpperCase()));
        request(() -> challengerClient.requestPlayerGod(Gods.POSEIDON.toString().toUpperCase()));
    }

    private void placeWorkerOnBoard() {
        request(() -> challengerClient.requestAction(Action.ADD_WORKER, new DTOposition(new Position(0,0))));
        request(() -> challengerClient.requestAction(Action.ADD_WORKER, new DTOposition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
        request(() -> otherClient.requestAction(Action.ADD_WORKER, new DTOposition(new Position(1,0))));
        request(() -> otherClient.requestAction(Action.ADD_WORKER, new DTOposition(new Position(2,0))));
        request(() -> otherClient.requestAction(Action.END_TURN, null));
    }

    private void firstTurnPoseidon() {
        // Poseidon places 4 buildings to speed up Artemis win
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DTOposition(new Position(0,0))));
        request(() -> challengerClient.requestAction(Action.MOVE, new DTOposition(new Position(0,1))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DTOposition(new Position(1,1))));
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DTOposition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DTOposition(new Position(1,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DTOposition(new Position(1,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DTOposition(new Position(1,3))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
    }

    private void secondTurnArtemis() {
        // Artemis moves on the building of height 2 at (1,2)
        request(() -> otherClient.requestAction(Action.SELECT_WORKER, new DTOposition(new Position(1,0))));
        request(() -> otherClient.requestAction(Action.MOVE, new DTOposition(new Position(1,1))));
        request(() -> otherClient.requestAction(Action.MOVE, new DTOposition(new Position(1,2))));
        request(() -> otherClient.requestAction(Action.BUILD, new DTOposition(new Position(1,3))));
        request(() -> otherClient.requestAction(Action.END_TURN, null));
    }

    private void thirdTurnPoseidon() {
        // Poseidon places the last building
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DTOposition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.MOVE, new DTOposition(new Position(2,3))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DTOposition(new Position(1,3))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
    }

    private void fourthTurnArtemis() {
        // Artemis wins
        request(() -> otherClient.requestAction(Action.SELECT_WORKER, new DTOposition(new Position(1,2))));
        request(() -> otherClient.requestAction(Action.MOVE, new DTOposition(new Position(1,3))));
        // won
    }

}
package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.mvc.TestClient;
import it.polimi.ingsw.mvc.TestServer;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.dto.DtoPosition;
import org.junit.jupiter.api.*;

class FSMTest {

    private final int testPort = 7357; // 7 3 5 7 = t e s t
    private TestServer server;
    private TestClient testClient, testClient2, testClient3;
    private TestClient challengerClient, otherClient;

    private final boolean showLog = false; // True to show client log

    @Test
    void completeGameTest() {
        // Server and clients start
        openServer();
        startClients();
        // [PRE-LOBBY] Requests two players
        createGame();
        // [LOBBY] Register two players
        request(() -> testClient.requestRegister("TestClient", Colors.BLUE));
        removePlayerDuringLogin(); // Removes testClient
        request(() -> testClient.requestRegister("TestClient", Colors.BLUE));
        wrongRequestLogin(); // Tries to register with already in-use parameters
        addOtherPlayers(); // Player try to connect when the lobby is full
        request(() -> testClient2.requestRegister("TestClient2", Colors.WHITE));
        // [CHALLENGER SELECTION]
        challengerChooseGods(); // The challenger chooses which gods will be used
        addPlayerDuringGame(); // Player tries to connect at game started
        // [PLAYER GOD SELECTION]
        playersChooseGods(); // Each player chooses his god
        // [STARTER SELECTION]
        starterPlayerSelection(); // The challenger chooses the starter player
        // [GAME MODE]
        placeWorkerOnBoard(); // Both players take turns to place the workers
        firstTurnPoseidon(); // First turn for poseidon
        secondTurnArtemis(); // First turn for artemis
        thirdTurnPoseidon(); // Second turn for poseidon
        fourthTurnArtemis(); // Second turn for artemis and victory
        // Checks if artemis won
        // -> Manca il metodo, appena si fa possiamo aggiungere l'assertTrue
        // Test clearUp
        request(this::disconnectClients);
    }

    private void openServer() {
        server = new TestServer(testPort, showLog); // True to show server log
    }

    private void createGame() {
        request(() -> testClient.requestPlayersNumber("5"));
        request(() -> testClient.requestPlayersNumber("2"));
    }

    private void startClients() {
        testClient = new TestClient("127.0.0.1", testPort, showLog);
        testClient.createConnection("127.0.0.1", testPort);
        testClient2 = new TestClient("127.0.0.1", testPort, showLog);
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
        Session.getInstance().getBoard().clear();
    }

    private void addOtherPlayers() {
        testClient3 = new TestClient("127.0.0.1", testPort, showLog);
        testClient3.createConnection("127.0.0.1", testPort);
        TestClient testClient4 = new TestClient("127.0.0.1", testPort, showLog);
        testClient4.createConnection("127.0.0.1", testPort);
    }

    private void addPlayerDuringGame() {
        testClient3 = new TestClient("127.0.0.1", testPort, showLog);
        testClient3.createConnection("127.0.0.1", testPort);
    }

    // Aggiunge delay tra i messaggi
    private void request(Runnable request) {
        try {
            Thread.sleep(   300); // Time between messages
            request.run();
        } catch(InterruptedException e) { e.printStackTrace(); }

    }

    private void starterPlayerSelection() {
        request(() -> challengerClient.requestStarterPlayer(challengerClient.getUsername()));
    }

    private void wrongRequestLogin() {
        if(showLog) { System.out.println("\nTest fake username\n"); }
        request(() -> testClient2.requestRegister("TestClient","TestClient", Colors.WHITE));
        if(showLog) { System.out.println("\nTest already taken username\n"); }
        request(() -> testClient2.requestRegister("TestClient", Colors.WHITE));
        if(showLog) { System.out.println("\nTest already chosen color\n"); }
        request(() -> testClient2.requestRegister("TestClient2", Colors.BLUE));
    }

    private void removePlayerDuringLogin() {
        request(() -> testClient.interrupt()); // Disconnect connection
        testClient = new TestClient("127.0.0.1", testPort, showLog); // Create new connection
        testClient.createConnection("127.0.0.1", testPort);
    }

    private void challengerChooseGods(){
        request(() -> System.out.println("\nWaiting 180ms ...\n"));
        challengerClient = (testClient.isChallenger()) ? testClient : testClient2;
        otherClient = (testClient == challengerClient) ? testClient2 : testClient;

        request(() -> challengerClient.requestChallengerGod(Gods.POSEIDON.toString().toUpperCase()));
        request(() -> challengerClient.requestChallengerGod(Gods.POSEIDON.toString().toUpperCase())); // Not available
        request(() -> challengerClient.requestChallengerGod(Gods.ARTEMIS.toString().toUpperCase()));
    }

    private void playersChooseGods(){
        request(() -> otherClient.requestPlayerGod(Gods.ARTEMIS.toString().toUpperCase()));
        request(() -> challengerClient.requestPlayerGod(Gods.ARTEMIS.toString().toUpperCase())); // Not available
        request(() -> challengerClient.requestPlayerGod(Gods.POSEIDON.toString().toUpperCase()));
    }

    private void placeWorkerOnBoard() {
        request(() -> challengerClient.requestAction(Action.ADD_WORKER, new DtoPosition(new Position(0,0))));
        request(() -> challengerClient.requestAction(Action.ADD_WORKER, new DtoPosition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
        request(() -> otherClient.requestAction(Action.ADD_WORKER, new DtoPosition(new Position(1,0))));
        request(() -> otherClient.requestAction(Action.ADD_WORKER, new DtoPosition(new Position(2,0))));
        request(() -> otherClient.requestAction(Action.END_TURN, null));
    }

    private void firstTurnPoseidon() {
        // Poseidon places 4 buildings to speed up Artemis win
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DtoPosition(new Position(0,0))));
        request(() -> challengerClient.requestAction(Action.MOVE, new DtoPosition(new Position(0,1))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,1))));
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DtoPosition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,2))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,3))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
    }

    private void secondTurnArtemis() {
        // Artemis moves on the building of height 2 at (1,2)
        request(() -> otherClient.requestAction(Action.SELECT_WORKER, new DtoPosition(new Position(1,0))));
        request(() -> otherClient.requestAction(Action.MOVE, new DtoPosition(new Position(1,1))));
        request(() -> otherClient.requestAction(Action.MOVE, new DtoPosition(new Position(1,2))));
        request(() -> otherClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,3))));
        request(() -> otherClient.requestAction(Action.END_TURN, null));
    }

    private void thirdTurnPoseidon() {
        // Poseidon places the last building
        request(() -> challengerClient.requestAction(Action.SELECT_WORKER, new DtoPosition(new Position(2,2))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null)); // Impossible action!
        request(() -> challengerClient.requestAction(Action.MOVE, new DtoPosition(new Position(2,3))));
        request(() -> challengerClient.requestAction(Action.BUILD, new DtoPosition(new Position(1,3))));
        request(() -> challengerClient.requestAction(Action.END_TURN, null));
    }

    private void fourthTurnArtemis() {
        // Artemis wins
        request(() -> otherClient.requestAction(Action.SELECT_WORKER, new DtoPosition(new Position(1,2))));
        request(() -> otherClient.requestAction(Action.MOVE, new DtoPosition(new Position(1,3))));
        // won
    }

}
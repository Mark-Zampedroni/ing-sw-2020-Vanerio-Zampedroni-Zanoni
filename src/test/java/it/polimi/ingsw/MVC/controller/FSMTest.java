package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.MVC.TestClient;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.Colors;
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
        request(() -> testClient2.requestRegister("TestClient2", Colors.WHITE));
        // [CHALLENGER SELECTION]
        // -> Manda 2 messaggi da entrambi scegliendo 2 dei (non si sa chi sia il challenger, e così testi anche tutto)
        // [PLAYER GOD SELECTION]
        // -> Entrambi i client mandando 2 volte 2 messaggi scegliendo il proprio dio (stessa parentesi di prima)
        // [STARTER SELECTION]
        // ...
        // ...

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
}
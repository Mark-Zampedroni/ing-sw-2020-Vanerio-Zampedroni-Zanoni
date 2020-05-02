package it.polimi.ingsw.MVC;

import it.polimi.ingsw.network.server.Server;

public class TestServer extends Server {

    public TestServer(int port) {
        super(port);
        LOG.setUseParentHandlers(false);
    }
}

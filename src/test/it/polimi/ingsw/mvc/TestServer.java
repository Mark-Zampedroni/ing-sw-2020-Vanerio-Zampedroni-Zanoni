package it.polimi.ingsw.mvc;

import it.polimi.ingsw.network.server.Server;

public class TestServer extends Server {

    // Creates a server used in FSM Test
    public TestServer(int port, boolean showLog) {
        super(port,false);
        LOG.setUseParentHandlers(false);
    }
}

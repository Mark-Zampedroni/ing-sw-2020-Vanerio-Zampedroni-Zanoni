package it.polimi.ingsw.mvc;

import it.polimi.ingsw.network.server.Server;

public class TestServer extends Server {

    public TestServer(int port, boolean showLog) {
        super(port);
        LOG.setUseParentHandlers(showLog);
    }
}

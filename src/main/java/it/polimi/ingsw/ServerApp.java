package it.polimi.ingsw;

import it.polimi.ingsw.net.server.ServerConnection;

public class ServerApp {

    public static void main(String []args) {
        new ServerConnection(7654);
    }

}

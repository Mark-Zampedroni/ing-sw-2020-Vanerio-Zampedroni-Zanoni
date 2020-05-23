package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

public class ServerApp {

    //questo è qui per ora per non far bloccare tutto mentre sistemo
    //feature=false -> non ho attiva la fault tolerance
    //feature=true -> la ho attiva
    static boolean feature=false;

    public static void main(String []args) {
         new Server(7654);
    }

    public static boolean isFeature() {
        return feature;
    }
}

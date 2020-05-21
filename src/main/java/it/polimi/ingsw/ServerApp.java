package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.persistency.ReconnectionHandler;
import it.polimi.ingsw.utility.persistency.ReloadGame;

public class ServerApp {

    //questo è qui per ora per non far bloccare tutto mentre sistemo
    //feature=false -> non ho attiva la fault tolerance
    //feature=true -> la ho attiva
    static boolean feature=false;

    public static void main(String []args) {
        if(! ReloadGame.isRestartable()){
         new Server(7654);
        } else { ReloadGame.restartGame();}
    }

    public static boolean isFeature() {
        return feature;
    }
}

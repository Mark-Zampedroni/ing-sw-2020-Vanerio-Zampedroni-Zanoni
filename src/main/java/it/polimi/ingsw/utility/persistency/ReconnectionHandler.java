package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.server.Server;

import java.util.List;
import java.util.logging.Logger;

public class ReconnectionHandler {

    Logger logger;

    public ReconnectionHandler(){
        //new Server (7654, this);
        //qui mi ricreo le connession con i client che pingano e mi faccio restituire una lista di remoteViews
    }

    public List<RemoteView> getViews() {
        return null;
    }

    public Logger getLOG() {
        return logger;
    }

    public void setLOG(Logger log) {
        logger=log;
    }
}

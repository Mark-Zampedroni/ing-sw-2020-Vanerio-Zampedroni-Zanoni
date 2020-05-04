package it.polimi.ingsw.MVC.controller.states;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.MVC.view.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class StateController {

    protected final Map<String, RemoteView> views;
    protected final SessionController controller;
    protected final Logger LOG;

    public StateController(SessionController controller, Map<String, RemoteView> views, Logger LOG) {
        this.controller = controller;
        this.views = views;
        this.LOG = LOG;
    }

    public void sendUpdate() {
        LOG.warning("This state can't send updates");
    }

    public abstract void parseMessage(Message message);
    public abstract void tryNextState();

    public void sendBroadcastMessage(Message message) {
        for (String player : views.keySet()) {
            views.get(player).sendMessage(message);
        }
    }

    public List<Colors> getFreeColors() {
        LOG.warning("getFreeColors called on wrong state");
        return new ArrayList<>(); // Empty, Override in Lobby
    }


    public void addUnregisteredView(ServerConnection connection) {
        LOG.warning("addUnregisteredView called on wrong state");
    }

    public void removePlayer(String username) {
        if(views.containsKey(username)) {
            Session.getInstance().removePlayer(username);
            views.remove(username);
            // Logica chiusura gioco
        }
    }

    // Registra un player e lo aggiunge al gioco
    public void addPlayer(String username, Colors color, RemoteView view) {
        LOG.warning("addPlayer called on wrong state");
    }

}

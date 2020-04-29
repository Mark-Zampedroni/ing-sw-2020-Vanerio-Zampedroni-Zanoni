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
        LOG.warning("Free colors were asked outside LOBBY state, it shouldn't happen");
        return new ArrayList<>(); // Empty, Override in Lobby
    }

    public void addUnregisteredView(ServerConnection connection) {
        LOG.warning("View tried to register outside LOBBY state, it shouldn't happen");
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
        LOG.warning("Game tried to add a player outside LOBBY state");
    }

}

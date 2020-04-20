package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class StateController {

    protected final Map<String, RemoteView> views;
    protected final SessionController controller;

    public StateController(SessionController controller, Map<String, RemoteView> views) {
        this.controller = controller;
        this.views = views;
    }

    public abstract void sendUpdate();
    public abstract void parseMessage(Message message);

    public void sendBroadcastMessage(Message message) {
        for (String player : views.keySet()) {
            views.get(player).sendMessage(message);
        }
    }
    public List<Colors> getFreeColors() {
        return new ArrayList<>(); // Empty, Override in Lobby
    }

}

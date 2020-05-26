package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.persistency.SavedData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class StateController implements Serializable {

    private static final long serialVersionUID = -7974027435942352531L;

    protected transient List<RemoteView> views;
    protected transient SessionController controller;
    protected transient Logger LOG;

    public StateController(SessionController controller, List<RemoteView> views, Logger LOG) {
        this.controller = controller;
        this.views = views;
        this.LOG = LOG;
    }

    public void reloadState(SessionController controller, SavedData savedData, List<RemoteView> views, Logger LOG) {
        resetPreviousState(views, controller, LOG);
        LOG.info("[CONTROLLER] Notifying successfull reconnection to "+views);
        notifyMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, "Server", "Reconnected successfully",true, "ALL"));
        if(!savedData.getActionDone()) {
            LOG.info("Last message before save is being re-parsed");
            parseMessage(savedData.getMessage());
        }
    }

    private void resetPreviousState(List<RemoteView> views,SessionController sessionController, Logger LOG) {
        this.LOG = LOG;
        this.views = views;
        controller = sessionController;
    }

    public void sendUpdate() {
        LOG.warning("This state can't send updates");
    }

    public abstract void parseMessage(Message message);
    public abstract void tryNextState();

    public List<Colors> getFreeColors() {
        LOG.warning("getFreeColors called on wrong state");
        return new ArrayList<>();
    }

    public void addUnregisteredView(ServerConnection connection) {
        LOG.warning("addUnregisteredView called on wrong state");
    }

    public void notifyMessage(Message message) {
        controller.saveGame(message,true);
        views.forEach(w -> w.sendMessage(message));
    }

    public void removePlayer(String username) {
        if(Session.getInstance().getPlayerByName(username) != null) {
            Session.getInstance().removePlayer(username);
            views.removeIf(v -> v.hasName(username));
            // Logica chiusura gioco <-----------------------------------------------------------------------
        }
    }

    public void addPlayer(String username, Colors color, RemoteView view) {
        LOG.warning("addPlayer called on wrong state");
    }

    protected Message messageBuilder(MessageType type, String info, String recipient) {
        return new Message(type,"SERVER",info,recipient);
    }

    protected Message messageBuilder(MessageType type, String info) {
        return messageBuilder(type,info,"ALL");
    }

    protected Message messageBuilder(MessageType type, String info, boolean flag, String recipient) {
        return new FlagMessage(type,"SERVER",info,flag,recipient);
    }

    protected Message messageBuilder(MessageType type, String info, boolean flag) {
        return messageBuilder(type,info,flag,"ALL");
    }

    protected Message messageBuilder(String info,String recipient) {
        return new LobbyUpdate("SERVER",info,controller.getFreeColors(), controller.getPlayers(),recipient);
    }

    protected Message messageBuilder(String info) {
        return messageBuilder(info,"ALL");
    }

}

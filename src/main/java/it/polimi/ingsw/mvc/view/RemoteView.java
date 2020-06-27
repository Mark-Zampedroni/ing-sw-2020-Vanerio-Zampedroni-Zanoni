package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionUpdateMessage;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.observer.Observable;
import it.polimi.ingsw.utility.observer.Observer;

import java.util.List;
import java.util.Map;

public class RemoteView extends Observable<Message> implements Observer<DtoSession> {

    private final ServerConnection connection;
    private boolean registered;
    private DtoSession dtoSession;

    public RemoteView(ServerConnection connection) {
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
    }

    public void register(String username) {
        if (!registered) {
            connection.setName(username);
            registered = true;
        }
    }

    public boolean hasName(String name) {
        return connection.getUsername().equals(name);
    }

    public boolean getRegistered() {
        return registered;
    }



    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    @Override
    public void update(DtoSession dtoSession) {
        this.dtoSession = dtoSession;
    }

    public void getFirstDTOSession(DtoSession dtoSession) {
        if (this.dtoSession == null) {
            this.dtoSession = dtoSession;
        }
    }

    public void updateActions(Map<Action, List<DtoPosition>> actionCandidates, String turnOwner, boolean isSpecialPowerActive) {
        sendMessage(new ActionUpdateMessage("SERVER", turnOwner, actionCandidates, dtoSession, isSpecialPowerActive, "ALL"));
    }

    private class MessageReceiver implements Observer<Message> {
        @Override
        public void update(Message message) {
            if (message.getSender() != null && message.getSender().equals(connection.getUsername())) { //Anti-cheat
                handleInput(message);
            }
        }

        private void handleInput(Message message) {
            RemoteView.this.notify(message);
        }
    }

}

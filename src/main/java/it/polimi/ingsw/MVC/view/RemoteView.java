package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.network.messages.game.ActionRequestMessage;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ChangeMessage;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.observer.Observable;
import it.polimi.ingsw.utility.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteView extends Observable<Message> implements Observer<DTOsession> {

    private ServerConnection connection;
    private boolean registered;
    private String username;
    private DTOsession dtoSession;

    private class MessageReceiver implements Observer<Message> {
        @Override
        public void update(Message message) {
            if(message.getSender().equals(connection.getToken())) { //Anti-cheat
                handleInput(message);
            }
        }
    }

    public RemoteView(ServerConnection connection) {
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
    }

    public void register(String username) {
        if(!registered) {
            this.username = username;
            connection.setName(username);
            registered = true;
        }
    }

    public boolean getRegistered() {
        return registered;
    }

    // RICEVE INPUT DEL GIOCATORE - RICHIESTA AZIONE
    private void handleInput(Message message) {
        notify(message); // LA GIRA AL SESSIONCONTROLLER
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    @Override
    public void update(DTOsession dtoSession) {
        this.dtoSession = dtoSession;
    }

    public void getFirstDTOSession(DTOsession dtoSession) {
        if(this.dtoSession == null) {
            this.dtoSession = dtoSession;
        }
    }

    public void updateActions(Map<Action, List<DTOposition>> actionCandidates, String turnOwner) {
        sendMessage(new ActionRequestMessage("SERVER",turnOwner,actionCandidates,dtoSession));
    }

}

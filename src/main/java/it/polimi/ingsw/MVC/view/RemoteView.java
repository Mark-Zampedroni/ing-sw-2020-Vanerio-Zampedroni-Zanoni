package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ChangeMessage;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.observer.Observable;
import it.polimi.ingsw.utility.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class RemoteView extends Observable<Message> implements Observer<DTOsession> {

    private ServerConnection connection;
    private boolean registered;
    private String username;
    private List<Action> possibleActions;

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
        possibleActions = new ArrayList<>();
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
    public void update(DTOsession dtOsession) {
        String s = "all";
        ChangeMessage message = new ChangeMessage(username, s, dtOsession);
        // RICEVE MODEL SERIALIZZATO E NE SALVA IL PUNTATORE, VIENE MANDATO IN UPDATE ACTIONS
        // VERRA' SEMPRE ESEGUITO PRIMA SERIAL. MODEL, POI AGGIORNAMENTO AZIONI, POI INVIO
    }

    public void updateActions(Message message) {
        // RICEVE AZIONI E LE METTE IN POSSIBLEACTIONS
        // - Crea pacchetto -
        // MANDA MESSAGGIO
    }

}

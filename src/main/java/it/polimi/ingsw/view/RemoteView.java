package it.polimi.ingsw.view;

import it.polimi.ingsw.DTO.DTOsession;
import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.net.messages.game.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.game.ChangeMessage;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observable;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.serialization.ModelSerialized;

import java.util.ArrayList;
import java.util.List;

public class RemoteView extends Observable<Message> implements Observer<DTOsession> {

    private ServerConnection connection;
    private String username;
    private List<Action> possibleActions;

    private class MessageReceiver implements Observer<Message> {
        @Override
        public void update(Message message) {
            handleInput(message);
        }
    }

    public RemoteView(String username, ServerConnection connection) {
        this.username = username;
        this.connection = connection;
        possibleActions = new ArrayList<>();
        connection.addObserver(new MessageReceiver());
    }

    // RICEVE INPUT DEL GIOCATORE - RICHIESTA AZIONE
    private void handleInput(Message message) {
        System.out.println("[Arrivato messaggio da "+message.getSender()+"]"); // TEST
        notify(message); // LA GIRA AL SESSIONCONTROLLER
    }


    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    @Override
    public void update(DTOsession dtOsession) {
        String s="all";
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

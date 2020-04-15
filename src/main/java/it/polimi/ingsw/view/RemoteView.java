package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ModelSerialized;
import it.polimi.ingsw.net.messages.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observable;
import it.polimi.ingsw.observer.observable.Observer;

public class RemoteView extends Observable<ActionMessage> implements Observer<ModelSerialized> {

    private ServerConnection connection;
    private String username;

    private class MessageReceiver implements Observer<ActionMessage> {
        @Override
        public void update(ActionMessage message) {
            handleInput(message);
        }
    }

    public RemoteView(String username, ServerConnection connection) {
        this.username = username;
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
    }

    // RICEVE INPUT DEL GIOCATORE - RICHIESTA AZIONE
    private void handleInput(ActionMessage message) {
        System.out.println("[ Qui viene eseguita la mossa "+message.getAction()+" ]\n"); // TEST
        notify(message); // LA GIRA AL SESSIONCONTROLLER
    }

    private void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    public void update(ModelSerialized model) {
        // RICEVE MODEL SERIALIZZATO
        // - Crea pacchetto -
        // MANDA MESSAGGIO UPDATE
    }

    public void updateActions(Message message) {
        // RICEVE AZIONI POSSIBILI E CANDIDATI POSIZIONI
        // - Crea pacchetto -
        // MANDA MESSAGGIO
    }

}

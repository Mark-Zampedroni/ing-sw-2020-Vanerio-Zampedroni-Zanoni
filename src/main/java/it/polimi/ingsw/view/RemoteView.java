package it.polimi.ingsw.view;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.model.ModelSerialized;
import it.polimi.ingsw.net.messages.game.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observable;
import it.polimi.ingsw.observer.observable.Observer;

import java.util.ArrayList;
import java.util.List;

public class RemoteView extends Observable<ActionMessage> implements Observer<ModelSerialized> {

    private ServerConnection connection;
    private String username;
    private List<Action> possibleActions;

    private class MessageReceiver implements Observer<ActionMessage> {
        @Override
        public void update(ActionMessage message) {
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
    private void handleInput(ActionMessage message) {
        System.out.println("[ Qui viene eseguita la mossa "+message.getAction()+" ]\n"); // TEST
        notify(message); // LA GIRA AL SESSIONCONTROLLER
    }

    // TEST
    public void parseMessage(Message message) {
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    public void update(ModelSerialized model) {
        // RICEVE MODEL SERIALIZZATO E NE SALVA IL PUNTATORE, VIENE MANDATO IN UPDATE ACTIONS
        // VERRA' SEMPRE ESEGUITO PRIMA SERIAL. MODEL, POI AGGIORNAMENTO AZIONI, POI INVIO
    }

    public void updateActions(Message message) {
        // RICEVE AZIONI E LE METTE IN POSSIBLEACTIONS
        // - Crea pacchetto -
        // MANDA MESSAGGIO
    }

}

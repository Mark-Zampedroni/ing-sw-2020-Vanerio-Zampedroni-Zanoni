package it.polimi.ingsw.view;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.net.ActionMessage;
import it.polimi.ingsw.net.server.ServerConnection;
import it.polimi.ingsw.observer.observable.Observer;

//extends Observable<PlayerMove>
public class RemoteView implements Observer<ActionMessage> {

    ServerConnection connection;

    private class MessageReceiver implements Observer<ActionMessage> {
        @Override
        public void update(ActionMessage message) {
            askAction(message.getAction());
        }
    }

    public RemoteView(ServerConnection connection) {
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
    }

    private void askAction(Action action) {
        System.out.println("[Qui viene eseguita mossa "+action+" ]\n");
    }

    public void update(ActionMessage message) {

    }

}

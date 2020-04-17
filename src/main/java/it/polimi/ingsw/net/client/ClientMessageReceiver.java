package it.polimi.ingsw.net.client;

import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.observer.observable.Observable;

import java.util.List;

public class ClientMessageReceiver extends Observable<Message> implements Runnable {

    private Thread t;
    private ClientConnection connection;

    public ClientMessageReceiver(ClientConnection connection, Client controller) {
        this.connection = connection;
        this.addObserver(controller);

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        List<Message> messages;
        while (!Thread.currentThread().isInterrupted()) {
            do {
                messages = connection.getQueue(); // Consumer
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) { connection.LOG.warning("Consumer wait time Exception"); }
            } while(messages.isEmpty());
            for(Message msg : messages) {
                notify(msg);
            }
         }
    }

    public void clear() {
        t.interrupt();
    }

}

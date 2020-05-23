package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class ClientConnection implements Runnable {



    public final Logger LOG;

    private final String ip;
    private final int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket socket;
    private final List<Message> inQueue;
    private Thread t;

    private final Object queueLock = new Object();
    private ClientMessageReceiver messageReceiver;
    private boolean reconnect = false;
    private String name = "";

    private static class ClientMessageReceiver extends Observable<Message> implements Runnable {

        private final Client controller;
        private final Thread t;
        private final ClientConnection connection;

        public ClientMessageReceiver(ClientConnection connection, Client controller) {
            this.connection = connection;
            this.controller = controller;
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
                    } catch(InterruptedException e) { connection.LOG.warning("ClientReceiver wait time Exception"); }
                } while(messages.isEmpty());
                for(Message msg : messages) {
                    notify(msg);
                }
            }
            this.removeObserver(controller);
        }

        public Client getController() {
            return controller;
        }

    }

    public ClientConnection(String ip, int port, Client controller) throws IOException {
        this.ip = ip;
        this.port = port;
        inQueue = new ArrayList<>();
        LOG = controller.LOG;
        startConnection();
        messageReceiver = new ClientMessageReceiver(this, controller);
    }

    public void startConnection() throws IOException {
        socket = new Socket(ip, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        t = new Thread(this);
        t.start();
    }

    public void sendMessage(Message msg) {
        try {
            if (output != null) {
                output.writeObject(msg);
                output.reset();
                System.out.println("Wrote msg on output (clientConnection:103)");
            }
        } catch(IOException e) { LOG.severe(e.getMessage()); }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = (Message) input.readObject();
                if(msg != null) {
                    synchronized(queueLock) {
                        inQueue.add(msg);
                    }
                }
            }
            catch(ClassNotFoundException e) {
                LOG.severe(e.getMessage());
            }
            catch(IOException e) {
                LOG.severe("Exception throw, connection closed");
                disconnect();
            }
        }
        if(reconnect) {
            initReconnection();
        }
    }

    private void initReconnection() {
        System.out.println("\nServer \"crashato\"\n"); // <---- TEST
        if (reconnect) {
        inQueue.add(new Message(MessageType.RECONNECTION_UPDATE, "SELF", "Server disconnected", "ALL"));
        Client controller = messageReceiver.getController();
        startReconnectionRequests(controller);
        } else {
            System.out.println("Ma non è ancora iniziato il game quindi non riconnetto!");
        } // <---- TEST
    }

    public void disconnect() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                LOG.info("Connection closed");
            }
            t.interrupt();
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
    }

    public List<Message> getQueue() {
        List<Message> copy;
        synchronized(queueLock) {
            copy = new ArrayList<>(inQueue);
            inQueue.clear();
        }
        return copy;
    }

    private void startReconnectionRequests(Client controller) {
        while(!reconnectRequest()) {
            try {
                System.out.println("Riconnessione fallita, riprovando in 2 secondi");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LOG.warning("Reconnection wait interrupted");
            }
        }
        if(reconnect) { sendMessage(new Message(MessageType.RECONNECTION_UPDATE,name,"Reconnecting","SERVER")); }
    }

    private boolean reconnectRequest() {
        try {
            startConnection();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected void setReconnect(boolean value) {
        reconnect = value;
        if(!reconnect) { disconnect(); }
    }

    protected boolean getReconnect() {
        return reconnect;
    }

    protected void setConnectionName(String name) {
        this.name = name;
    }
}

package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ClientConnection implements Runnable {


    public final Logger log;

    private final String ip;
    private final int port;
    private final List<Message> inQueue;
    private final Object queueLock = new Object();
    private String name = "";
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private Thread t;
    private boolean reconnect;
    private boolean isDisconnected;

    /**
     * Constructor
     *
     * @param controller client
     * @param port connection port
     * @param ip server ip
     * @throws IOException if there is a problem
     */
    public ClientConnection(String ip, int port, Client controller) throws IOException {
        this.ip = ip;
        this.port = port;
        inQueue = new ArrayList<>();
        log = controller.log;
        startConnection();
        new ClientMessageReceiver(this, controller);
    }

    /**
     * Creates a connection with the server
     * @throws IOException if there is a problem in the opening of streams
     */
    public void startConnection() throws IOException {
        socket = new Socket(ip, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        t = new Thread(this);
        t.start();
        isDisconnected = false;
    }

    /**
     * Sends a message
     *
     * @param msg message to send
     */
    public void sendMessage(Message msg) {
        try {
            if (output != null) {
                output.writeObject(msg);
                output.reset();
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Task of the connection Thread;
     * Receives the messages and puts them in queue
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = (Message) input.readObject();
                if (msg != null) {
                    synchronized (queueLock) {
                        inQueue.add(msg);
                    }
                }
            } catch (ClassNotFoundException e) {
                log.severe(e.getMessage());
            } catch (IOException e) {
                log.severe("[CONNECTION] Socket closed");
                if (!isDisconnected && !reconnect)
                    inQueue.add(new Message(MessageType.DISCONNECTION_UPDATE, "SELF", "Lost connection to server", "ALL"));
                disconnect();
            }
        }
        log.info("[CONNECTION] Server crashed");
        if (reconnect)
            initReconnection();
    }

    /**
     * Starts the reconnection
     */
    private void initReconnection() {
        synchronized (queueLock) {
            log.info("[CONNECTION] Trying to reconnect ...");
            inQueue.add(new Message(MessageType.RECONNECTION_UPDATE, "SELF", "Server disconnected", "ALL"));
        }
        startReconnectionRequests();
    }

    /**
     * Disconnects a connection
     */
    public void disconnect() {
        isDisconnected = true;
        try {
            if (!socket.isClosed()) {
                socket.close();
                log.info("[CONNECTION] Connection closed");
            }
            t.interrupt();
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Gets the queue
     *
     * @return the queue
     */
    public List<Message> getQueue() {
        List<Message> copy;
        synchronized (queueLock) {
            copy = new ArrayList<>(inQueue);
            inQueue.clear();
        }
        return copy;
    }

    /**
     * Starts a reconnection request
     */
    private void startReconnectionRequests() {
        while (!reconnectRequest()) {
            waitTime();
        }
        if (reconnect) {
            sendMessage(new Message(MessageType.RECONNECTION_UPDATE, name, "Reconnecting", "SERVER"));
        }
    }

    /**
     * Checks if the reconnection request is successful
     *
     * @return {@code true} if it has started the reconnection
     */
    private boolean reconnectRequest() {
        try {
            startConnection();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if reconnecting
     *
     * @return {@code true} if reconnecting
     */
    protected boolean getReconnect() {
        return reconnect;
    }

    /**
     * Setter for the reconnection
     *
     * @param value value for the reconnection
     */
    protected void setReconnect(boolean value) {
        reconnect = value;
    }

    /**
     * Sets the name of the connection
     *
     * @param name name being set
     */
    protected void setConnectionName(String name) {
        this.name = name;
    }

    /**
     * Checks if a connection is disconnected
     *
     * @return {@code true} if disconnected
     */
    protected boolean isDisconnected() {
        return isDisconnected;
    }

    /**
     * Sets a connection as disconnected
     */
    protected void setDisconnected() {
        this.isDisconnected = true;
    }

    /**
     * Puts the thread into a sleeping state
     */
    private void waitTime() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.warning("[CONNECTION] Wait time of " + 2000 + "ms interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private static class ClientMessageReceiver extends Observable<Message> implements Runnable {

        private final transient Client controller;
        private final transient ClientConnection connection;

        public ClientMessageReceiver(ClientConnection connection, Client controller) {
            this.connection = connection;
            this.controller = controller;
            this.addObserver(controller);

            new Thread(this).start();
        }

        @Override
        public void run() {
            List<Message> messages;
            while (!Thread.currentThread().isInterrupted()) {
                do {
                    messages = connection.getQueue(); // Consumer
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        connection.log.warning("[CONNECTION_RECEIVER] ClientReceiver wait time Exception");
                        Thread.currentThread().interrupt();
                    }
                } while (messages.isEmpty());
                for (Message msg : messages) {
                    notify(msg);
                }
            }
            this.removeObserver(controller);
        }

        public Client getController() {
            return controller;
        }
    }
}
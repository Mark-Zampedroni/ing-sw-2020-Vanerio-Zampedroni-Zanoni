package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.exceptions.net.FailedConnectionException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
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


public class ClientConnection extends Thread {

    private final String ip;
    private final int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket socket;
    public final Logger LOG;
    private final List<Message> inQueue;

    private final Object queueLock = new Object();

    private class ClientMessageReceiver extends Observable<Message> implements Runnable {

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
                    } catch(InterruptedException e) { connection.LOG.warning("ClientReceiver wait time Exception"); }
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

    public ClientConnection(String ip, int port, Client controller) {
        this.ip = ip;
        this.port = port;
        inQueue = new ArrayList<>();

        LOG = Logger.getLogger("client");
        startLogging();

        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("start connection!");
                startConnection();
                new ClientMessageReceiver(this, controller);
                LOG.info("Created connection and controller");
                break;
            } catch (FailedConnectionException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void startLogging() {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH-mm-ss");
        Date date = new Date();
        try {
            FileHandler fileHandler = new FileHandler("log/client/" + dateFormat.format(date) + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOG.addHandler(fileHandler);
        } catch (IOException e) {
            LOG.severe(e.getMessage() + " couldn't be opened\n");
        }
    }

    public void startConnection() throws FailedConnectionException {
        try {
            socket = new Socket(ip, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            this.start();
        } catch(IOException e) {
            LOG.severe("Couldn't reach the server");
            throw new FailedConnectionException("Couldn't reach the server");
        }
    }

    public void sendMessage(Message msg) {
        try {
            if (output != null) {
                output.writeObject(msg);
                output.reset();
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
                        inQueue.add(msg); // Messages read in Client
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
    }

    private void disconnect() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                LOG.info("Connection closed");
            }
            this.interrupt();
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
}

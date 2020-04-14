package it.polimi.ingsw.net.client;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.exceptions.net.FailedConnectionException;
import it.polimi.ingsw.net.Message;

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

    private final String username;
    private final String ip;
    private final int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket socket;

    private final Logger LOG;

    private final List<Message> inQueue;

    private final Object queueLock = new Object();

    public ClientConnection(String username, String ip, int port) throws FailedConnectionException {
        this.username = username;
        this.ip = ip;
        this.port = port;
        LOG = Logger.getLogger(username);
        inQueue = new ArrayList<>();

        startLogging();
        startConnection();
    }

    private void startLogging() {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH-mm-ss");
        Date date = new Date();
        try {
            FileHandler fileHandler = new FileHandler("log/"+username+"/"+dateFormat.format(date)+".log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOG.addHandler(fileHandler);
        } catch(IOException e) { LOG.severe(e.getMessage() + " couldn't be opened\n"); }
    }

    public void startConnection() throws FailedConnectionException {
        try {
            socket = new Socket(ip, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            sendMessage(new Message(MessageType.REGISTRATION, username, "Connection request as " + username));
            this.start();
        } catch(IOException e) {
            LOG.severe(e.getMessage());
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
                System.out.println(msg); // TEST
                if(msg != null) {
                    synchronized(queueLock) {
                        inQueue.add(msg); // Messages to read from controller
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
                System.out.println("Connection closed");
            }
            this.interrupt();
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
    }

    public List<Message> getQueue() {
        List<Message> copy;
        synchronized(queueLock) {
            copy = new ArrayList<>(List.copyOf(inQueue));
            inQueue.clear();
        }
        return copy;
    }


}

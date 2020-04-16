package it.polimi.ingsw.net.server;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.RegistrationReply;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Map<String, ServerConnection> connections;

    private final Object connectionsLock = new Object();

    private SessionController sessionController;

    public static final Logger LOG = Logger.getLogger("Server");

    public Server(int port) {
        connections = new HashMap<>();
        startLogging();
        sessionController = new SessionController(); // Controller
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
        this.start(); // Starts thread
    }

    private void startLogging() {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH-mm-ss");
        Date date = new Date();
        try {
            FileHandler fileHandler = new FileHandler("log/server/"+dateFormat.format(date)+".log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOG.addHandler(fileHandler);
        } catch(IOException e) { LOG.severe(e.getMessage() + " couldn't be opened\n"); }
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                LOG.info("Waiting for request ...\n"); // TEST
                Socket client = serverSocket.accept();
                LOG.info("Socket created\n"); // TEST
                new ServerConnection(this, client);
            } catch (IOException e) {
                Server.LOG.warning(e.getMessage());
            }
        }
    }

    public void onDisconnection(ServerConnection connection) {
        String user = null;
        synchronized(connectionsLock) {
            for (Map.Entry<String, ServerConnection> entry : connections.entrySet()) {
                if (entry.getValue().equals(connection)) {
                    user = entry.getKey();
                }
            }
        }
        if(user != null) {
            LOG.info(user + " disconnected\n");
            if(true) { // Game in lobby - felice e non sconnette tutti
                synchronized (connectionsLock) {
                    connections.remove(user);
                }
                LOG.info(user + " removed from lobby\n");
            }
            sessionController.removePlayer(user);
        }
        // Gestione disconnessione giocatore
        // 1) in lobby esce
        // 2) altrimenti notifica tutti e chiude la partita
    }

    public void registerConnection(ServerConnection connection, String user, Colors color) {
        synchronized(connectionsLock) {
            if(connections.containsKey(user)) {
                connection.sendMessage(new RegistrationReply(MessageType.KO_NAME, "SERVER", "This username is already in use"));
                LOG.info("A player tried to connect with the already in use username "+user+"\n");
                connection.disconnect();
            }
            else if(!sessionController.getFreeColors().contains(color)) {
                connection.sendMessage(new RegistrationReply(MessageType.KO_COLOR, "SERVER", "Color "+color+
                                       " is not available", sessionController.getFreeColors()));
                LOG.info("A player tried to connect with the already in use username "+user+"\n");
                connection.disconnect();
            }
            else if(sessionController.isStarted()) {
                connection.sendMessage(new RegistrationReply(MessageType.KO, "SERVER", "A game has already started"));
                LOG.info(user + " tried to connect, but the game has already started\n");
                connection.disconnect();
            }
            else if(false) { // is lobby full ?
                // do nothing atm
            }
            else {
                connections.put(user, connection);
                LOG.info(user + " connected\n");
                sessionController.addPlayer(user, color, new RemoteView(user,connection));
                connection.sendMessage(new RegistrationReply(MessageType.OK, "SERVER", "Connection successful"));
            }
        }
    }
}

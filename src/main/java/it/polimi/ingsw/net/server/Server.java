package it.polimi.ingsw.net.server;

import it.polimi.ingsw.controller.SessionController;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.messages.lobby.RegistrationReply;
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
    private List<ServerConnection> pendingConnections;

    private final Object connectionsLock = new Object();

    private SessionController sessionController;

    public static final Logger LOG = Logger.getLogger("Server");

    public Server(int port) {
        connections = new HashMap<>();
        pendingConnections = new ArrayList<>();
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

    /*
        Quando arriva una richiesta di connessione l'apre e l'aggiunge alle connessioni non registrate
     */
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                LOG.info("Waiting for request ...\n"); // TEST
                Socket client = serverSocket.accept();
                LOG.info("Socket created\n"); // TEST
                ServerConnection c = new ServerConnection(this, client);
                sendLobbyUpdate(c);
                pendingConnections.add(c);
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
            synchronized (connectionsLock) {
                if(sessionController.getState() == GameState.LOBBY) { // Game in lobby - felice e non sconnette tutti
                    connections.remove(user);
                    LOG.info(user + " removed from lobby\n");
                }
                sessionController.removePlayer(user);
            }
        }
        // Gestione disconnessione giocatore
        // 1) in lobby esce
        // 2) altrimenti notifica tutti e chiude la partita
    }

    public void registerConnection(ServerConnection connection, String user, Colors color) {
        synchronized(connectionsLock) {
            if(connections.containsKey(user)) {
                connection.sendMessage(new RegistrationReply("SERVER", "This username is already in use",false));
                LOG.info("A player tried to register with the already in use username "+user+"\n");
            }
            else if(!sessionController.getFreeColors().contains(color)) {
                connection.sendMessage(new RegistrationReply("SERVER", "Color "+color+ " is not available",false));
                LOG.info("A player tried to register with the already in use color "+color+"\n");
            }
            else if(sessionController.isStarted()) {
                connection.sendMessage(new RegistrationReply("SERVER", "A game has already started",false));
                LOG.info(user + " tried to register, but the game has already started\n");
                connection.disconnect();
            }
            else if(sessionController.getPlayers().size() == 3) {
                connection.sendMessage(new RegistrationReply("SERVER", "Lobby is full",false));
                LOG.info(user + " tried to register, but the lobby is full\n");
            }
            else {
                confirmConnection(user, color, connection);
                connection.sendMessage(new RegistrationReply("SERVER", user,true));
                System.out.println(sessionController.getPlayers()); // TEST
                broadcastLobbyUpdate();

            }
        }
    }

    private void sendLobbyUpdate(ServerConnection connection) {
        connection.sendMessage(new LobbyUpdate( "SERVER", "Update",
                               sessionController.getFreeColors(), sessionController.getPlayers()));
    }

    private void broadcastLobbyUpdate() {
        broadcastMessage(new LobbyUpdate("SERVER", "Update",
                        sessionController.getFreeColors(), sessionController.getPlayers()));
    }

    public void broadcastMessage(Message message) {
        if(sessionController.getState() == GameState.LOBBY) {
            for (ServerConnection c : pendingConnections) {
                c.sendMessage(message);
            }
        }
        for(String c : connections.keySet()) {
            connections.get(c).sendMessage(message);
        }
    }

    private void confirmConnection(String user, Colors color, ServerConnection connection) {
        connections.put(user, connection);
        pendingConnections.remove(connection);
        connection.register();
        LOG.info(user + " connected\n");
        sessionController.addPlayer(user, color, new RemoteView(user,connection));
    }
}

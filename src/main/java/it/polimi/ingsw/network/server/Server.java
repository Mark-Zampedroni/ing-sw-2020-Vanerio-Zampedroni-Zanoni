package it.polimi.ingsw.network.server;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.utility.persistency.ReconnectionHandler;
//import it.polimi.ingsw.utility.persistency.SaveHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private final List<ServerConnection> allConnections;
    private final List<ServerConnection> freshConnections;

    private final Object connectionsLock = new Object();

    private /*final*/ SessionController sessionController;
    private ServerConnection gameCreator;

    private int token = 0;

    public static final Logger LOG = Logger.getLogger("Server");

    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private final Thread queueHandler;

    private class QueueHandler implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    tasks.take().run();
                } catch (InterruptedException | NullPointerException e) {
                    LOG.warning(e.getMessage());
                }
            }
        }
    }

    public Server(int port) {
        freshConnections = new ArrayList<>();
        allConnections = new ArrayList<>();
        startLogging();
        sessionController = new SessionController(freshConnections, LOG); // Controller
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
        this.start();
        queueHandler = new Thread(new QueueHandler());
        queueHandler.start();
    }
 /*
    public Server(int port, ReconnectionHandler reconnectionHandler) {
        reconnectionHandler.setLOG(LOG);

        //da qui tramite i ping mi serve che veda i player e li associ a

        freshConnections = new ArrayList<>();
        allConnections = new ArrayList<>();
        startLogging();
        sessionController = new SessionController(freshConnections, LOG); // Controller
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
        this.start();
        queueHandler = new Thread(new QueueHandler());
        queueHandler.start();
    }*/

    protected void handleReconnection(Message message) {
        System.out.println(message);
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

    public void run() { // BLOCCA SE NON IN LOBBY
        while (!Thread.currentThread().isInterrupted()) {
            try {
                token += 1;
                String name = String.valueOf(token);
                Socket client = serverSocket.accept();
                tasks.add(() -> queueNewConnection(client,name));
                } catch (IOException e) {
                    Server.LOG.warning(e.getMessage());
                } catch (NullPointerException e) {
                    Server.LOG.severe("The connection port is already occupied");
            }
        }
        queueHandler.interrupt();
    }

    private void queueNewConnection(Socket client, String name) {
        ServerConnection c = new ServerConnection(this, client, name, tasks);
        System.out.println("Creating connection");
        if(sessionController.isGameStarted()) {
            System.out.println("Denying connection");
            c.denyConnection("The game has already started, you can't connect ");
        }
        else {
            synchronized (connectionsLock) {
                freshConnections.add(c);
                allConnections.add(c);
                if (gameCreator == null) {
                    setNewGameCreator();
                } // Se e' il primo player decide quante persone entreranno
            }
            if (isLobbyCreated()) {
                fillPlayersSlots();
            } // Fresh -> Pending se spazio e si sa quanti player giocano
            notifyFreshQueue(); // Aggiorna la coda Fresh sulla situazione
        }
    }

    protected void onDisconnection(ServerConnection connection) {
        synchronized(connectionsLock) {
            if (connection.isInLobby()) {
                disconnectInLobby(connection);
            } else {
                freshConnections.remove(connection);
            }
            allConnections.remove(connection);

            if (gameCreator == connection) { // !sessionController.isGameStarted() ? Se non si potrà gestire in registered
                Server.LOG.info("Removed current game Creator");
                setNewGameCreator();
            }
            fillPlayersSlots();
        }
    }

    private void disconnectInLobby(ServerConnection connection) {
        String user = connection.getUsername();
        LOG.info(user + " disconnected\n");
        if(sessionController.getState() == GameState.LOBBY) { // Game in lobby - felice e non sconnette tutti
            LOG.info(user + " removed from lobby\n");
            sessionController.removePlayer(user);
            sessionController.sendUpdate();
        }
        else {
            System.out.println("CHIUSURA PARTITA! USCITO PLAYER"); // <------------ Da gestire via controller
        }
    }

    protected void startLobby(ServerConnection connection, String choice) {
        if(!isLobbyCreated() && connection == gameCreator) { // Anti-cheat
            if(Arrays.asList(new String[]{"2","3"}).contains(choice)) { // Anti-cheat
                sessionController.setGameCapacity(Integer.parseInt(choice));
                fillPlayersSlots();
            }
            else { // Anti-cheat
                gameCreator.sendMessage(new Message(MessageType.SLOTS_UPDATE, "SERVER", "Create game",gameCreator.getUsername()));
            }
        }
    }

    private void setNewGameCreator() {
        gameCreator = null;
        if(!allConnections.isEmpty()) {
            gameCreator = (freshConnections.size() != allConnections.size()) ?
                          allConnections.stream().filter(ServerConnection::isInLobby).collect(Collectors.toList()).get(0) :
                          allConnections.get(0); // Oldest connection
            if(!isLobbyCreated()) {
                gameCreator.sendMessage(new Message(MessageType.SLOTS_UPDATE, "SERVER", "Create game",gameCreator.getUsername()));
            }
        }
        else { sessionController.setGameCapacity(0); }
    }

    // Supposto che la lista di connessioni sia piccola, altrimenti sarebbe meglio dividere in più liste in base allo stato
    private void fillPlayersSlots() {
        synchronized (connectionsLock) {
            if(isLobbyCreated()) {
                int neededPlayers = sessionController.getGameCapacity() - (allConnections.size() - freshConnections.size());
                List<ServerConnection> copy = new ArrayList<>(freshConnections);
                copy.stream().limit(neededPlayers)
                        .forEach(c -> {
                            c.putInLobby();
                            sessionController.addUnregisteredView(c);
                            freshConnections.remove(c);
                            c.sendMessage(createLobbyUpdate());
                        });
            }
            notifyFreshQueue();
        }
    }

    private boolean isLobbyCreated() {
        return (sessionController.getGameCapacity() != 0);
    }

    // Notifica la coda in pre-lobby sulla posizione in cui sono
    private void notifyFreshQueue() {
        for(ServerConnection connection : freshConnections) {
            if(gameCreator != connection) { connection.sendMessage(createInfoUpdate(freshConnections.indexOf(connection),connection.getUsername())); }
        }
    }

    // Crea un messaggio di info per la coda in pre-lobby in base alla situazione di gioco
    private Message createInfoUpdate(int position, String username) {
        if(isLobbyCreated()) {
            return new Message(MessageType.INFO_UPDATE, "SERVER", "The game is for " + sessionController.getGameCapacity() + " players ... \n"
                    + "At the moment the Lobby is full and " + ((position == 0) ? "you are first in queue\n" : "there are " + position + " players in queue before you\n")
                    + "You will be disconnected if no slots are vacated and the game starts",username);
        }
        else {
            return new Message(MessageType.INFO_UPDATE, "SERVER", "A player is already creating a game!\nThere are "
                    + position + " players before you in queue ",username);
        }
    }

    private Message createLobbyUpdate() {
        return new LobbyUpdate("SERVER", "Update", sessionController.getFreeColors(), sessionController.getPlayers(),"ALL");
    }




}

package it.polimi.ingsw.network.server;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.ConnectionLevel;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private final List<ServerConnection> allConnections;
    private final List<ServerConnection> freshConnections;

    private final Object connectionsLock = new Object();

    private SessionController sessionController;
    private ServerConnection gameCreator;

    private int token = 0;

    public static final Logger LOG = Logger.getLogger("Server");

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
                LOG.info("Waiting for connection request");
                token += 1;
                String name = String.valueOf(token);
                Socket client = serverSocket.accept();
                ServerConnection c = new ServerConnection(this, client, name);
                if(sessionController.isGameStarted()) { // DA SINCRONIZZARE SU LOCK
                    c.denyConnection("The game has already started, you can't connect");
                }
                else {
                    synchronized(connectionsLock) {
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
                    LOG.info("Created new connection");
                }
            } catch (IOException e) {
                Server.LOG.warning(e.getMessage());
            } catch (NullPointerException e) {
                Server.LOG.severe("The connection port is alreaday occupied");
            }

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
            System.out.println("CHIUSURA PARTITA! USCITO PLAYER");
        }
    }

    protected void startLobby(ServerConnection connection, String choice) {
        if(!isLobbyCreated() && connection == gameCreator) { // Anti-cheat
            if(Arrays.asList(new String[]{"2","3"}).contains(choice)) { // Anti-cheat
                sessionController.setGameCapacity(Integer.parseInt(choice));
                fillPlayersSlots();
            }
            else { // Anti-cheat
                gameCreator.sendMessage(new Message(MessageType.SLOTS_CHOICE, "SERVER", "Create game"));
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
                gameCreator.sendMessage(new Message(MessageType.SLOTS_CHOICE, "SERVER", "Create game"));
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
            if(gameCreator != connection) { connection.sendMessage(createInfoUpdate(freshConnections.indexOf(connection))); }
        }
    }

    // Crea un messaggio di info per la coda in pre-lobby in base alla situazione di gioco
    private Message createInfoUpdate(int position) {
        if(isLobbyCreated()) {
            return new Message(MessageType.INFO, "SERVER", "The game is for " + sessionController.getGameCapacity() + " players ...\n"
                    + "At the moment the Lobby is full and " + ((position == 0) ? "you are first in queue\n" : "there are " + position + " players in queue before you\n")
                    + "You will be disconnected if no slots are vacated and the game starts");
        }
        else {
            return new Message(MessageType.INFO, "SERVER", "A player is already creating a game!\nThere are "
                    + position + " players before you in queue");
        }
    }

    private Message createLobbyUpdate() {
        return new LobbyUpdate("SERVER", "Update", sessionController.getFreeColors(), sessionController.getPlayers());
    }




}

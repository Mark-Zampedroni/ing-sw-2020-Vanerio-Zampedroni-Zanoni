package it.polimi.ingsw.network.server;

import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.ConnectionLevel;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.MVC.view.RemoteView;

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
    private Map<String, ServerConnection> registeredConnections;
    private final List<ServerConnection> connections;

    private final Object connectionsLock = new Object();

    private SessionController sessionController;
    private ServerConnection gameCreator;

    public static final Logger LOG = Logger.getLogger("Server");

    public Server(int port) {
        registeredConnections = new HashMap<>();
        connections = new ArrayList<>();
        startLogging();
        sessionController = new SessionController(connections, LOG); // Controller
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            LOG.severe(e.getMessage());
        }
        System.out.println("Starting Server thread");
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
                Socket client = serverSocket.accept();
                ServerConnection c = new ServerConnection(this, client);
                if(sessionController.isGameStarted()) { // DA SINCRONIZZARE SU LOCK
                    denyConnection(c,"The game has already started, you can't connect");
                }
                else {
                    synchronized(connectionsLock) {
                        connections.add(c); //
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
            }
        }
    }

    protected void onDisconnection(ServerConnection connection) {
        synchronized(connectionsLock) {
            if (connection.getConnectionLevel() == ConnectionLevel.REGISTERED) {
                disconnectRegistered(connection);
            } else {
                connections.remove(connection);
            }
            if (gameCreator == connection) { // !sessionController.isGameStarted() ? Se non si potrà gestire in registered
                Server.LOG.info("Removed current game Creator");
                setNewGameCreator();
            }
            fillPlayersSlots();
        }
    }

    private void disconnectRegistered(ServerConnection connection) {
        String user = getConnectionUsername(connection);
        if(user != null) {
            LOG.info(user + " disconnected\n");
            if(sessionController.getState() == GameState.LOBBY) { // Game in lobby - felice e non sconnette tutti
                registeredConnections.remove(user);
                LOG.info(user + " removed from lobby\n");
                sessionController.removePlayer(user);
                sessionController.sendUpdate();
            }
            else {
                System.out.println("CHIUSURA PARTITA! USCITO PLAYER");
            }
        }
    }

    protected void registerConnection(ServerConnection connection, String user, Colors color) {
        synchronized(connectionsLock) {
            if(registeredConnections.containsKey(user)) { // Anti-cheat
                connection.sendMessage(createRegistrationReply("This username is already in use",false));
                LOG.info("A player tried to register with the already in use username "+user+"\n");
            }
            else if(!sessionController.getFreeColors().contains(color)) { // Anti-cheat
                connection.sendMessage(createRegistrationReply("Color "+color+ " is not available",false));
                LOG.info("A player tried to register with the already in use color "+color+"\n");
            }
            else {
                confirmConnection(user, color, connection);
                connection.sendMessage(createRegistrationReply( user,true));
                sessionController.sendUpdate();
                tryStartGame();
            }
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

    private void denyConnection(ServerConnection connection, String message) {
        connection.sendMessage(new Message(MessageType.DISCONNECT, "SERVER", message));
        connection.disconnect();
        LOG.info("Disconnected connection with message "+message);
    }

    private void setNewGameCreator() {
        gameCreator = null;
        if(!connections.isEmpty()) {
            gameCreator = connections.get(0);
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
                int neededPlayers = sessionController.getGameCapacity() - (getFilteredLevel(ConnectionLevel.PENDING).size() + registeredConnections.size());
                connections.stream().filter(c -> c.getConnectionLevel() == ConnectionLevel.FRESH).limit(neededPlayers)
                        .forEach(c -> {
                            c.sendMessage(createLobbyUpdate());
                            c.setConnectionLevel(ConnectionLevel.PENDING);
                        });
            }
            notifyFreshQueue();
        }
    }

    private void confirmConnection(String user, Colors color, ServerConnection connection) {
        synchronized (connectionsLock) {
            registeredConnections.put(user, connection);
            connections.remove(connection);
            connection.setConnectionLevel(ConnectionLevel.REGISTERED);
            LOG.info(user + " connected\n");
            sessionController.addPlayer(user, color, new RemoteView(user, connection));
        }
    }

    private boolean isLobbyCreated() {
        return (sessionController.getGameCapacity() != 0);
    }

    private List<ServerConnection> getFilteredLevel(ConnectionLevel level) {
        return connections.stream().filter(c -> c.getConnectionLevel() == level).collect(Collectors.toList());
    }

    private String getConnectionUsername(ServerConnection connection) {
        String user = null;
        for (Map.Entry<String, ServerConnection> entry : registeredConnections.entrySet()) {
            if (entry.getValue().equals(connection)) {
                user = entry.getKey();
                break;
            }
        }
        return user;
    }

    private void tryStartGame() {
        synchronized (connectionsLock) {
            if (registeredConnections.size() == sessionController.getGameCapacity()) {
                List<ServerConnection> copy = new ArrayList<>(connections);
                copy.forEach(c -> denyConnection(c,"The game has started, you were disconnected"));
                connections.clear();
                sessionController.tryNextState();
            }
        }
    }

    // Notifica la coda in pre-lobby sulla posizione in cui sono
    private void notifyFreshQueue() {
        List<ServerConnection> toNotify = getFilteredLevel(ConnectionLevel.FRESH);
        for(ServerConnection connection : toNotify) {
            if(gameCreator != connection) { connection.sendMessage(createInfoUpdate(toNotify.indexOf(connection))); }
        }
    }

    // Crea un messaggio di info per la coda in pre-lobby in base alla situazione di gioco
    private Message createInfoUpdate(int position) {
        if(isLobbyCreated()) {
            return new Message(MessageType.INFO, "SERVER", "The game is for " + sessionController.getGameCapacity() + " players ...\n"
                    + "At the moment the Lobby is full and there are " + position + " players in queue before you\n"
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

    private Message createRegistrationReply(String info, boolean success) {
        return new FlagMessage(MessageType.REGISTRATION,"SERVER", info ,success);
    }


}

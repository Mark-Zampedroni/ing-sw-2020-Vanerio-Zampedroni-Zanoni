package it.polimi.ingsw.network.server;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.persistency.ReloadGame;

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

/**
 * Server; manages the connection/reconnection/disconnection of any {@link ServerConnection ServerConnection}
 */
public class Server extends Thread {

    /**
     * Logger of the Server
     */
    public static final Logger LOG = Logger.getLogger("Server");
    /**
     * Name of the Server, used as signature in the messages sent
     */
    public static final String SENDER = "SERVER";
    private static Server instance = null;
    private final List<ServerConnection> allConnections;
    private final List<ServerConnection> freshConnections;
    private final Object connectionsLock = new Object();
    private final Map<String, ServerConnection> reconnecting;
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private ServerSocket serverSocket;
    private SessionController sessionController;
    private ServerConnection gameCreator;
    private int token = 0;
    private Thread queueHandler;
    private boolean isLogging;

    /**
     * Constructor of a the Singleton instance.
     * Opens the server on the port specified
     *
     * @param port port on which the server will wait for connections
     * @param log  if {@code true} creates a log where any event will be recorded
     */
    public Server(int port, boolean log) {
        isLogging = log;
        if (instance == null) Server.assignInstance(this);
        reconnecting = new HashMap<>();
        freshConnections = new ArrayList<>();
        allConnections = new ArrayList<>();
        if (isLogging) startLogging();
        sessionController = new SessionController(freshConnections, LOG); // Controller
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
        this.start();
        queueHandler = new Thread(new QueueHandler());
        queueHandler.start();
    }

    /**
     * Assigns a new instance of the Server
     *
     * @param currentInstance new instance of th server
     */
    public static void assignInstance(Server currentInstance) {
        instance = currentInstance;
    }

    /**
     * Disconnects all the connections and clears any variables in the current instance
     */
    public static void restartSession() {
        instance.restart();
    }

    /**
     * Reloads the game from a save file, checks if the connection given was
     * a player of that game. If not refuses the reconnection, otherwise
     * waits for all the players to reconnect and resumes the game
     *
     * @param message    reconnection request
     * @param connection connection trying to reconnect
     */
    protected void handleReconnection(Message message, ServerConnection connection) {
        synchronized (reconnecting) {
            SessionController newController = ReloadGame.reloadConnection(sessionController, reconnecting, connection, LOG, message);
            if (newController != null)
                sessionController = newController;
        }
    }

    /**
     * Starts a new Logger, assigns it to the static variable LOG
     */
    private void startLogging() {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH-mm-ss");
        Date date = new Date();
        try {
            FileHandler fileHandler = new FileHandler(dateFormat.format(date) + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOG.addHandler(fileHandler);
        } catch (IOException e) {
            LOG.severe("[SERVER] " + e.getMessage() + " couldn't be opened\n");
        }
    }

    /**
     * Task of the Server Thread.
     * Stays idle waiting for new connection requests, when one is received
     * opens a socket and queues the connection creation request
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                token += 1;
                String name = String.valueOf(token);
                Socket client = serverSocket.accept();
                tasks.add(() -> queueNewConnection(client, name));
            } catch (IOException e) {
                Server.LOG.warning(e.getMessage());
            } catch (NullPointerException e) {
                Server.LOG.severe("[SERVER] The connection port is already occupied");
            }
        }
        queueHandler.interrupt();
    }

    /**
     * Runs a connection creation request, adds the fresh connection
     * to the Lists freshConnections and allConnections
     *
     * @param client socket of the connection
     * @param name   temporary token of the connection
     */
    private void queueNewConnection(Socket client, String name) {
        ServerConnection c = new ServerConnection(this, client, name, tasks);
        Server.LOG.info(() -> "[SERVER] Opening connection " + name);
        if (sessionController.isGameStarted()) {
            Server.LOG.info(() -> "[SERVER] Denying connection " + name);
            c.denyConnection("The game has already started, you can't connect ");
        } else {
            synchronized (connectionsLock) {
                freshConnections.add(c);
                allConnections.add(c);
                if (gameCreator == null) setNewGameCreator();
            }
            if (isLobbyCreated()) fillPlayersSlots();
            notifyFreshQueue();
        }
    }

    /**
     * Called when a {@link ServerConnection connection} disconnects.
     * Removes such connection from any List of the server
     *
     * @param connection connection that disconnected
     */
    protected void onDisconnection(ServerConnection connection) {
        synchronized (connectionsLock) {
            if (connection.isInLobby()) disconnectInLobby(connection);
            else freshConnections.remove(connection);
            allConnections.remove(connection);

            if (gameCreator == connection) {
                Server.LOG.info("[SERVER] Removed current game Creator");
                setNewGameCreator();
            }
            fillPlayersSlots();
        }
    }

    /**
     * Handles a disconnection in lobby.
     * Asks the Controller to remove the player from the Model
     *
     * @param connection connection that disconnected
     */
    private void disconnectInLobby(ServerConnection connection) {
        String user = connection.getUsername();
        LOG.info(() -> "[SERVER] " + user + " disconnected\n");
        sessionController.removePlayer(user);
        if (sessionController.getState() == GameState.LOBBY) sessionController.sendUpdate();
    }

    /**
     * Opens the lobby
     *
     * @param connection connection of the game creator
     * @param choice     number of players chosen for the game
     */
    protected void startLobby(ServerConnection connection, String choice) {
        if (!isLobbyCreated() && connection == gameCreator) { // Anti-cheat
            if (Arrays.asList("2", "3").contains(choice)) { // Anti-cheat
                sessionController.setGameCapacity(Integer.parseInt(choice));
                fillPlayersSlots();
            } else { // Anti-cheat
                gameCreator.sendMessage(new Message(MessageType.SLOTS_UPDATE, SENDER, "Create game", gameCreator.getUsername()));
            }
        }
    }

    /**
     * Sets a new game creator;
     * may be used when the first connection occurs or when the previous
     * game creator disconnects
     */
    private void setNewGameCreator() {
        gameCreator = null;
        if (!allConnections.isEmpty()) {
            gameCreator = (freshConnections.size() != allConnections.size()) ?
                    allConnections.stream().filter(ServerConnection::isInLobby).collect(Collectors.toList()).get(0) :
                    allConnections.get(0); // Oldest connection
            if (!isLobbyCreated()) {
                gameCreator.sendMessage(new Message(MessageType.SLOTS_UPDATE, SENDER, "Create game", gameCreator.getUsername()));
            }
        } else {
            sessionController.setGameCapacity(0);
        }
    }

    /**
     * Moves in lobby the required number of players, choosing them following a FIFO rule
     * on the connection queue
     */
    private void fillPlayersSlots() {
        synchronized (connectionsLock) {
            if (isLobbyCreated()) {
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

    /**
     * Checks if the lobby is open
     *
     * @return {@code true} if the lobby is open
     */
    private boolean isLobbyCreated() {
        return (sessionController.getGameCapacity() != 0);
    }

    /**
     * Updates all the connections in freshConnection on their position in queue
     */
    private void notifyFreshQueue() {
        for (ServerConnection connection : freshConnections) {
            if (gameCreator != connection) {
                connection.sendMessage(createInfoUpdate(freshConnections.indexOf(connection), connection.getUsername()));
            }
        }
    }

    /**
     * Creates a message of update on why a connection can't join the lobby
     *
     * @param position position of the connection in queue
     * @param username token of the connection
     * @return message of update
     */
    private Message createInfoUpdate(int position, String username) {
        if (isLobbyCreated()) {
            return new Message(MessageType.INFO_UPDATE, SENDER, "The game is for " + sessionController.getGameCapacity() + " players ... \n"
                    + "At the moment the Lobby is full and " + ((position == 0) ? "you are first in queue\n" : "there are " + position + " players in queue before you\n")
                    + "You will be disconnected if no slots are vacated and the game starts", username);
        } else {
            return new Message(MessageType.INFO_UPDATE, SENDER, "A player is already creating a game!\nThere are "
                    + position + " players before you in queue ", username);
        }
    }

    /**
     * Creates a message of update for the player in Lobby
     *
     * @return message of update
     */
    private Message createLobbyUpdate() {
        return new LobbyUpdate(SENDER, "Update", sessionController.getFreeColors(), sessionController.getPlayers(), "ALL");
    }

    /**
     * Restarts the Server, clearing all the variables and connections
     */
    public void restart() {
        allConnections.forEach(ServerConnection::interrupt);
        reconnecting.clear();
        freshConnections.clear();
        allConnections.clear();
        gameCreator = null;
        if (isLogging) startLogging();
        sessionController = new SessionController(freshConnections, LOG); // Controller
    }

    /**
     * Inner class acting as a receiver for the messages sent by the connections
     */
    private class QueueHandler implements Runnable {
        /**
         * Task of the Thread; waits for any new message and adds it to the list in server
         */
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    tasks.take().run();
                } catch (InterruptedException | NullPointerException e) {
                    LOG.warning(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


}
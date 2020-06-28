package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Connection of the RemoteView on Server side. Acts as middle-man for the message sent and received
 * by a Client
 */
public class ServerConnection extends Observable<Message> implements Runnable {

    private final transient Server server;
    private final transient Socket socket;
    private final transient Object readLock = new Object();
    private final transient Object sendLock = new Object();
    private final transient BlockingQueue<Runnable> tasks;
    private String token;
    private boolean open;
    private boolean inLobby;
    private transient ObjectInputStream input;
    private transient ObjectOutputStream output;
    private transient Thread listener;

    /**
     * Constructor of the connection
     *
     * @param server server handling the connection
     * @param socket socket of the connection
     * @param token  connection token
     * @param tasks  list of tasks read by the server
     */
    public ServerConnection(Server server, Socket socket, String token, BlockingQueue<Runnable> tasks) {
        Server.LOG.info(() -> "[CONNECTION] Creating virtual view, token: " + token);
        this.server = server;
        this.socket = socket;
        this.tasks = tasks;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
            listener = new Thread(this);
            listener.start();
            open = true;
            sendToken(token);
            Server.LOG.info(() -> "[CONNECTION] Successful creation of Connection, token: " + token);
        } catch (IOException e) {
            Server.LOG.severe(() -> "[CONNECTION] In Server constructor, token: " + token);
        }
    }

    /**
     * Sends the connection token to the Client side
     *
     * @param token name of the token
     */
    private void sendToken(String token) {
        this.token = token;
        sendMessage(new Message(MessageType.CONNECTION_TOKEN, Server.SENDER, token, token));
    }

    /**
     * Sends a message on the socket
     *
     * @param msg message to send
     */
    public void sendMessage(Message msg) {
        if (open) {
            try {
                synchronized (sendLock) {
                    output.writeObject(msg);
                    output.reset();
                }
            } catch (IOException e) {
                Server.LOG.severe("[CONNECTION] IOE on sending message, token: " + token);
                disconnect();
            }
        }
    }

    /**
     * Disconnects the connection
     */
    protected void disconnect() {
        if (open) {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                    listener.interrupt();
                    server.onDisconnection(this);
                    open = false;
                }
            } catch (IOException e) {
                Server.LOG.severe("[CONNECTION] IOE on disconnection, token: " + token);
            }
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
                synchronized (readLock) {
                    tasks.add(() -> queueMessage(msg));
                }
            } catch (ClassNotFoundException e) {
                Server.LOG.severe("[CONNECTION] classNotFound on run of ServerConnection, token: " + token);
            } catch (IOException e) {
                Server.LOG.severe("[CONNECTION] IOE on run of ServerConnection, token: " + token);
                disconnect();
                break;
            }
        }
    }

    /**
     * Queues a message, if the connection is registered sends it to the
     * RemoteView, otherwise asks the Server to parse it
     *
     * @param msg message to add to the queue
     */
    private void queueMessage(Message msg) {
        if (!inLobby) {
            if (msg.getType() == MessageType.SLOTS_UPDATE)
                server.startLobby(this, msg.getInfo());
            else if (msg.getType() == MessageType.RECONNECTION_UPDATE)
                server.handleReconnection(msg, this);
        } else {
            notify(msg); // this.notify -> RemoteView.notify -> SessionController
        }
    }

    /**
     * Checks if this connection is in lobby
     *
     * @return {@code true} if the connection is in lobby
     */
    public boolean isInLobby() {
        return inLobby;
    }

    /**
     * Flags the connection as in lobby
     */
    public void putInLobby() {
        inLobby = true;
    }

    /**
     * Denies the connection, before closing the socket sends a message stating the reason
     *
     * @param message message of disconnection
     */
    public void denyConnection(String message) {
        sendMessage(new Message(MessageType.DISCONNECTION_UPDATE, Server.SENDER, message, token));
        disconnect();
    }

    /**
     * Denies the reconnection, before closing the socket sends a message stating the reason
     *
     * @param name   name of the player who tried to reconnect (different than the connection name if
     *               it's a reconnection request, the name is the old connection name it had in the
     *               save file)
     * @param reason reason of the refusal
     */
    public void denyReconnection(String name, String reason) {
        sendMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, Server.SENDER, reason, false, name));
    }

    /**
     * Sets the name of the connection
     *
     * @param username name being set
     */
    public void setName(String username) {
        token = username;
    }

    /**
     * Gets the name of the connection
     *
     * @return the connection name / token
     */
    public String getUsername() {
        return token;
    }

    /**
     * Stops the thread
     */
    public void interrupt() {
        listener.interrupt();
    }

}
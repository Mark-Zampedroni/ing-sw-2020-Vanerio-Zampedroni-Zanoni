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

public class ServerConnection extends Observable<Message> implements Runnable {

    private final transient Server server;
    private final transient Socket socket;
    private final transient Object readLock = new Object();
    private final transient Object sendLock = new Object();
    private final transient BlockingQueue<Runnable> tasks;
    private String token;
    private boolean open;
    private boolean inLobby;
    private transient ObjectInputStream input; // Not final because on error it may not initialize
    private transient ObjectOutputStream output;
    private transient Thread listener;

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

    private void sendToken(String token) {
        this.token = token;
        sendMessage(new Message(MessageType.CONNECTION_TOKEN, Server.SENDER, token, token));
    }

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

    private void queueMessage(Message msg) {
        if (!inLobby) {
            if (msg.getType() == MessageType.SLOTS_UPDATE) {
                server.startLobby(this, msg.getInfo());
            } else if (msg.getType() == MessageType.RECONNECTION_UPDATE) {
                server.handleReconnection(msg, this);
            }
        } else {
            notify(msg); // this.notify -> RemoteView.notify -> SessionController
        }
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public void putInLobby() {
        inLobby = true;
    }

    public void denyConnection(String message) {
        sendMessage(new Message(MessageType.DISCONNECTION_UPDATE, Server.SENDER, message, token));
        disconnect();
    }

    public void denyReconnection(String name, String reason) {
        sendMessage(new FlagMessage(MessageType.RECONNECTION_REPLY, Server.SENDER, reason, false, name));
    }

    public void setName(String username) {
        token = username;
    }

    public String getUsername() {
        return token;
    }

    public void interrupt() {
        listener.interrupt();
    }

}
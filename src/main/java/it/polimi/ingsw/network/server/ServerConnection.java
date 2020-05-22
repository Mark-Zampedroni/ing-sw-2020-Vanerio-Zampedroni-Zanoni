package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ServerConnection extends Observable<Message> implements Runnable {

    private String token;

    private boolean open;

    private final Server server;
    private final Socket socket;

    private boolean inLobby;

    private final Object readLock = new Object(); // static?
    private final Object sendLock = new Object(); // static?

    private ObjectInputStream input; // Not final because on error it may not initialize
    private ObjectOutputStream output;

    private final BlockingQueue<Runnable> tasks;

    private Thread listener;

    public ServerConnection(Server server, Socket socket, String token, BlockingQueue<Runnable> tasks) {
        Server.LOG.info("[CONNECTION] Creating virtual view");
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
            Server.LOG.info("[CONNECTION] Successful creation of Connection");
        } catch(IOException e) {
            Server.LOG.severe("[CONNECTION] "+e.toString());
        }
    }

    private void sendToken(String token) {
        this.token = token;
        sendMessage(new Message(MessageType.CONNECTION_TOKEN, "SERVER", token,token));
    }

    public void sendMessage(Message msg) {
        if(open) {
            try {
                synchronized(sendLock) {
                    output.writeObject(msg);
                    output.reset();
                }
            } catch(IOException e) {
                Server.LOG.severe("[CONNECTION] "+e.toString());
                disconnect();
            }
        }
    }

    protected void disconnect() {
        if(open) {
            try {
                if(!socket.isClosed()) {
                    socket.close();
                    listener.interrupt();
                    server.onDisconnection(this);
                    open = false;
                }
            } catch(IOException e) {
                Server.LOG.severe("[CONNECTION] "+e.toString());
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = (Message) input.readObject();
                synchronized (readLock) {
                    if (msg != null) {
                        tasks.add(() -> queueMessage(msg));
                    }
                }
            } catch(ClassNotFoundException e) {
                Server.LOG.severe("[CONNECTION] "+e.toString());
            } catch(IOException e) {
                Server.LOG.severe("[CONNECTION] "+e.toString());
                disconnect();
            }
        }
    }

    private void queueMessage(Message msg) {
        if (!inLobby) { // Message received by Server
            if (msg.getType() == MessageType.SLOTS_UPDATE) {
                server.startLobby(this, msg.getInfo());
            }
            else if(msg.getType() == MessageType.RECONNECTION_UPDATE) {
                server.handleReconnection(msg);
            }
        } else { // Message received by View
            notify(msg); // Notify -> RemoteView -> SessionController
        }
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public void putInLobby() {
        inLobby = true;
    }

    public String getToken() {
        return token;
    }

    public void denyConnection(String message) {
        sendMessage(new Message(MessageType.DISCONNECTION_UPDATE, "SERVER", message, token));
        disconnect();
    }

    public void setName(String username) {
        token = username;
    }

    public String getUsername() {
        return token;
    }

}

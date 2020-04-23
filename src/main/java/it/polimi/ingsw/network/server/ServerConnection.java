package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.enumerations.ConnectionLevel;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection extends Observable<Message> implements Runnable {

    private ConnectionLevel level;
    private boolean open;

    private final Server server;
    private final Socket socket;

    private final Object readLock = new Object(); // static?
    private final Object sendLock = new Object(); // static?

    private ObjectInputStream input; // Not final because on error it may not initialize
    private ObjectOutputStream output;

    private Thread listener;

    public ServerConnection(Server server, Socket socket) {
        Server.LOG.info("[CONNECTION] Creating virtual view");
        level = ConnectionLevel.FRESH;
        this.server = server;
        this.socket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
            listener = new Thread(this);
            listener.start();
            open = true;
            Server.LOG.info("[CONNECTION] Successful creation of Connection");
        } catch(IOException e) {
            Server.LOG.severe("[CONNECTION] "+e.toString());
        }
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
                        if (level != ConnectionLevel.REGISTERED) { // Message received by Server
                            if (msg.getType() == MessageType.REGISTRATION) {
                                server.registerConnection(this, msg.getSender(), ((RegistrationMessage) msg).getColor());
                            }
                            else if(msg.getType() == MessageType.SLOTS_CHOICE) {
                                server.startLobby(this, msg.getInfo());
                            }
                        }
                        else { // Message received by View
                            notify(msg); // Notify -> RemoteView -> SessionController
                        }
                    }
                }
            } catch(ClassNotFoundException e){
                Server.LOG.severe("[CONNECTION] "+e.toString());
            } catch(IOException e){
                Server.LOG.severe("[CONNECTION] "+e.toString());
                disconnect();
            }
        }
    }

    protected void setConnectionLevel(ConnectionLevel level) {
        this.level = level;
    }

    public ConnectionLevel getConnectionLevel() {
        return level;
    }

}

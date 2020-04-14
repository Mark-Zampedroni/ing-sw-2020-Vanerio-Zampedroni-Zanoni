package it.polimi.ingsw.net.server;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView implements Runnable {

    private boolean open;

    private final ServerConnection server;
    private final Socket socket;

    public static final Object readLock = new Object(); // Lock shared on all VirtualViews
    public static final Object sendLock = new Object();

    private ObjectInputStream input; // Not final because on error it may not initialize
    private ObjectOutputStream output;

    private Thread listener;

    public VirtualView(ServerConnection server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
            listener = new Thread(this);
            listener.start();
            open = true;
        } catch(IOException e) {
            ServerConnection.LOG.severe(e.toString());
        }
    }

    public void sendMessage(Message msg) {
        if(open) {
            try {
                synchronized(VirtualView.sendLock) {
                    output.writeObject(msg);
                    output.reset();
                }
            } catch(IOException e) {
                ServerConnection.LOG.severe(e.getMessage());
                disconnect();
            }
        }
    }

    public void disconnect() {
        if(open) {
            try {
                if(!socket.isClosed()) {
                    socket.close();
                    listener.interrupt();
                    server.onDisconnection(this);
                    open = false;
                }
            } catch(IOException e) {
                ServerConnection.LOG.severe(e.getMessage());
            }
        }
    }

    public boolean isOpen()  {
        return open;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (VirtualView.readLock) {
                    Message msg = (Message) input.readObject();
                    if(msg != null) {
                        if(msg.getType() == MessageType.REGISTRATION) {
                            server.registerConnection(this, msg.getSender());
                        }
                        // ServerApplication.onMessage(msg) || Altri casi
                        System.out.println(msg); // TEST
                    }
                }
            } catch(ClassNotFoundException e) {
                ServerConnection.LOG.severe(e.getMessage());
            } catch(IOException e) {
                disconnect();
            }
        }
    }
}
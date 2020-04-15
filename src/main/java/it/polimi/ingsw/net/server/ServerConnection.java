package it.polimi.ingsw.net.server;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.observer.observable.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection extends Observable<ActionMessage> implements Runnable {

    private boolean open;

    private final Server server;
    private final Socket socket;

    private final Object readLock = new Object(); // Lock shared on all VirtualViews
    private final Object sendLock = new Object();

    private ObjectInputStream input; // Not final because on error it may not initialize
    private ObjectOutputStream output;

    private Thread listener;

    public ServerConnection(Server server, Socket socket) {
        Server.LOG.info("Creating virtual view\n");
        this.server = server;
        this.socket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
            listener = new Thread(this);
            listener.start();
            open = true;
            Server.LOG.info("Successful creation of virtual view\n");
        } catch(IOException e) {
            Server.LOG.severe(e.toString());
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
                Server.LOG.severe(e.getMessage());
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
                Server.LOG.severe(e.getMessage());
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
                synchronized (readLock) {
                    Message msg = (Message) input.readObject();
                    if(msg != null) {
                        if(msg.getType() == MessageType.REGISTRATION) {
                            server.registerConnection(this, msg.getSender());
                            System.out.println("Stampato da Connection, gestito su Server: \n"+msg+"\n"); // TEST
                        }
                        else if(msg.getType() == MessageType.ACTION) {
                            notify((ActionMessage) msg); // Notify RemoteView -> SessionController -> print
                        }
                        else { // NO REGISTRATION AND NO ACTION WILL PRINT ON CONSOLE // TEST
                            System.out.println("Stampato da Connection: \n"+msg+"\n"); // TEST
                        }
                    }
                }
            } catch(ClassNotFoundException e) {
                Server.LOG.severe(e.getMessage());
            } catch(IOException e) {
                Server.LOG.severe("Exception throw, connection closed");
                disconnect();
            }
        }
    }
}

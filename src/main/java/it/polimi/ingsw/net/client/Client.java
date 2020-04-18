package it.polimi.ingsw.net.client;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.observer.observable.Observer;
import it.polimi.ingsw.view.CLI.Cli;
import it.polimi.ingsw.view.View;

import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread implements Observer<Message> {

    private final LinkedBlockingQueue<Runnable> viewUpdate;
    private final LinkedBlockingQueue<Runnable> viewInput;
    private GameState state;
    private String username;
    private ClientConnection connection;
    private View view;
    private int playerCounter;

    public Client(String ip, int port, int view) {

        state = GameState.CONNECTION;
        viewUpdate = new LinkedBlockingQueue<>();
        viewInput = new LinkedBlockingQueue<>();

        initGraphic(view);
        createConnection(ip,port); // state -> LOGIN

        System.out.println("Connected");

        viewInput.add(() -> this.view.requestLogin());
        new UpdateListener().start();
        this.start();
    }

    private class UpdateListener extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    viewUpdate.take().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                viewInput.take().run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private void initGraphic(int choice) {
        // this.view = (choice == 0) ? new Cli(this) : new Gui(this);
        this.view = new Cli(this); // Test CLI, da implementare View su GUI
    }


    public void createConnection(String ip, int port) {
        connection = new ClientConnection(ip, port, this);
        state = GameState.LOGIN;
    }

    public void update(Message message) {
        switch(message.getType()) {
            case REGISTRATION:
                parseRegistrationReply((FlagMessage) message);
                break;
            case LOBBY_UPDATE:
                parseLobbyUpdate((LobbyUpdate) message);
                break;
            case ACTION: // TEST
                parseActionReply(message);
                break;
            case STATE_UPDATE: // ancora niente
                break;
        }
    }

    // TEST, NO MESSAGE MA ACTIONMESSAGE
    private void parseActionReply(Message message) {
        viewUpdate.add(() -> view.requestAction());
    }

    private void parseRegistrationReply(FlagMessage message) {
        if(state == GameState.LOGIN) {
            if(message.getFlag()) {
                connection.register();
                username = message.getInfo();
                state = GameState.LOBBY;
                viewInput.add(() -> view.requestAction()); // TEST
            }
            else {
                if(playerCounter<3){viewInput.add(() -> view.requestLogin() );}
                else{viewInput.add(() -> view.denyLogin());}
            }
        }
    }

    private void parseLobbyUpdate(LobbyUpdate message){
        if(state == GameState.LOGIN || state == GameState.LOBBY) {
            if(playerCounter == 3 && message.getPlayers().keySet().size() < playerCounter && state == GameState.LOGIN){
                viewInput.add(() -> view.requestLogin() );}
            playerCounter = message.getPlayers().keySet().size();
            viewUpdate.add(() -> view.updateLobby(message));
            }
        }

    public void requestLogin(String username, Colors color) {
        connection.registerConnection(username,color);
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    public GameState getGameState() {
        return state;
    }

}

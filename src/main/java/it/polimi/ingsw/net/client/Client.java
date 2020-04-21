package it.polimi.ingsw.net.client;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.StateUpdateMessage;
import it.polimi.ingsw.net.messages.lobby.GodChoice;
import it.polimi.ingsw.net.messages.lobby.GodUpdate;
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
        switch (message.getType()) {
            case DISCONNECT:
                parseDisconnectMessage(message);
                break;
            case REGISTRATION:
                parseRegistrationReply((FlagMessage) message);
                break;
            case LOBBY_UPDATE:
                parseLobbyUpdate((LobbyUpdate) message);
                break;
            case ACTION: // TEST
                parseActionReply(message);
                break;
            case STATE_UPDATE:
                parseStateUpdate((StateUpdateMessage) message);
                break;
            case GOD_UPDATE:
                parseGodUpdate((GodUpdate) message);
                break;
            case GOD_CHOICE:
                parseGodChoice((GodChoice) message);
                break;
        }
    }

    private void parseDisconnectMessage(Message message) {
        viewUpdate.add(() -> view.showMessage(message.getInfo()));
    }

    // TEST, NO MESSAGE MA ACTIONMESSAGE
    private void parseActionReply(Message message) {
        viewInput.add(() -> view.requestAction());
    }

    private void parseRegistrationReply(FlagMessage message) {
        if(state == GameState.LOGIN) {
            if(message.getFlag()) {
                connection.register();
                username = message.getInfo();
                state = GameState.LOBBY;
                viewInput.add(() -> view.requestReady()); // TEST
            }
            else {
                if(playerCounter<3){viewInput.add(() -> view.requestLogin() );}
                else{viewInput.add(() -> view.denyLogin());}
            }
        }
    }

    private void parseLobbyUpdate(LobbyUpdate message) {
        if(state == GameState.LOGIN || state == GameState.LOBBY) {
            if(playerCounter == 3 && message.getPlayers().keySet().size() < playerCounter && state == GameState.LOGIN) {
                viewInput.add(() -> view.requestLogin() );
            }
            playerCounter = message.getPlayers().keySet().size();
            viewUpdate.add(() -> view.updateLobby(message));
        }
    }

    private void parseGodUpdate(GodUpdate message) {
            viewUpdate.add(() -> view.displayGods(message));
            if (message.getInfo().equals(username)) {
                viewInput.add(() -> view.godSelection(message.getGods()));
            }

    }

    private void parseGodChoice(GodChoice message){
        if(message.getInfo().equals("starter")){
            viewInput.add(() -> view.Starter(message.getGods()));
        }
        else{
            viewInput.add(() -> view.godAssignment(message.getGods()));
        }
    }


    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        //viewUpdate.add(() -> view.switchState(state));
    }

    public void requestLogin(String username, Colors color) {
        connection.registerConnection(username,color);
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

}

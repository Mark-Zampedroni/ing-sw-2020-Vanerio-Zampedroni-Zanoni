package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.lobby.GodUpdate;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.MVC.view.CLI.Cli;
import it.polimi.ingsw.MVC.view.View;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread implements Observer<Message> {

    private final LinkedBlockingQueue<Runnable> viewOutput;
    private final LinkedBlockingQueue<Runnable> viewInput;

    private GameState state;
    private String username;

    private ClientConnection connection;

    private View view;

    private ArrayList<String> chosenGods;
    private Map<String, Colors> players;

    public Client(String ip, int port, int view) {

        viewOutput = new LinkedBlockingQueue<>();
        viewInput = new LinkedBlockingQueue<>();

        initGraphic(view);
        createConnection(ip,port);

        new UpdateListener().start();
        this.start();
    }

    private class UpdateListener extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    viewOutput.take().run();
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
        state = GameState.CONNECTION;
        connection = new ClientConnection(ip, port, this);
    }

    public void update(Message message) {
        switch (message.getType()) {
            case CONNECTION_TOKEN: // state = GameState.PRE_LOBBY;
                parseConnectionMessage(message);
                break;
            case SLOTS_CHOICE: // PRE-LOBBY
                parseSlotMessage(message);
                break;
            case INFO: // PRE-LOBBY
                parseInfoMessage(message);
                break;
            case DISCONNECT: // PRE-LOBBY / LOBBY (tutte)
                parseDisconnectMessage(message);
                break;
            case REGISTRATION: // LOBBY (pendente)
                parseRegistrationReply((FlagMessage) message);
                break;
            case LOBBY_UPDATE: // LOBBY (tutte)
                parseLobbyUpdate((LobbyUpdate) message);
                break;
            case ACTION: // GAME
                parseActionReply(message);
                break;
            case STATE_UPDATE: // TUTTE
                parseStateUpdate((StateUpdateMessage) message);
                break;
            case GOD_UPDATE: // GOD_SELECTION
                parseGodUpdate((GodUpdate) message);
                break;
            case GOD_CHOICE: // GOD_SELECTION
                parseGodChoice(message);
                break;
        }
    }

    private void parseConnectionMessage(Message message) {
        if(state == GameState.CONNECTION) {
            username = message.getInfo();
            state = GameState.PRE_LOBBY;
        }
    }

    private void parseSlotMessage(Message message) { // TEST CLI
        System.out.println("\nDa quanti player il game? Scrivere 2 o 3\n");
        sendMessage(new Message(MessageType.SLOTS_CHOICE, username, (new Scanner(System.in)).nextLine()));
    }

    private void parseInfoMessage(Message message) {
        System.out.println("\n\n\n"+message.getInfo()+"\n\n");
    }

    private void parseDisconnectMessage(Message message) {
        viewOutput.add(() -> view.showMessage(message.getInfo()));
    }

    // TEST, NO MESSAGE MA ACTIONMESSAGE
    private void parseActionReply(Message message) {
        viewInput.add(view::requestAction);
    }

    private void parseRegistrationReply(FlagMessage message) {
        if(state == GameState.LOGIN) {
            if(message.getFlag()) {
                username = message.getInfo(); // View registrata su Server
                state = GameState.LOBBY;
                viewInput.add(view::showLogged); // TEST
            }
            else {
                viewInput.add(view::requestLogin);
            }
        }
    }


    private void parseLobbyUpdate(LobbyUpdate message) {
        players = message.getPlayers();
        if(state == GameState.PRE_LOBBY) {
            state = GameState.LOGIN;
            viewInput.add(view::requestLogin);
        }
        if(state == GameState.LOGIN || state == GameState.LOBBY) {
            viewOutput.add(() -> view.updateLobby(message)); // Qui ha info
        }
    }

    private void parseGodUpdate(GodUpdate message) {
        if(state == GameState.GOD_SELECTION) {
            chosenGods = new ArrayList<>(message.getGods().get("chosen"));
            if(!message.getInfo().equals("update")){
                viewOutput.add(() -> view.displayGods(message));
                if (message.getInfo().equals(username)) {
                    viewInput.add(() -> view.godSelection(message.getGods()));
                }
            }

        }
    }
            //Da aggiungere mappa per i nomi dei player, Adesso va con 3 stronzi a,b,c
    private void parseGodChoice(Message message){
        switch (message.getInfo()) {
            case "starter":
                viewOutput.add(() -> view.displayString(new ArrayList<>(players.keySet()), "\nAvailable Players to choose: "));
                viewInput.add(() -> view.starter(new ArrayList<>(players.keySet())));
                break;
            case "choice":
                viewOutput.add(() -> view.displayString(chosenGods, "\nAvailable Gods: "));
                viewInput.add(() -> view.godAssignment(chosenGods));
                break;
            default:
                chosenGods.remove(message.getInfo());
                break;
        }
    }


    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        //viewUpdate.add(() -> view.switchState(state));
    }

    public void requestLogin(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(username, requestedUsername, color));
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

}

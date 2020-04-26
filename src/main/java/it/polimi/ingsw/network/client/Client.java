package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.lobby.GodChosen;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.MVC.view.CLI.Cli;
import it.polimi.ingsw.MVC.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread implements Observer<Message> {

    private final LinkedBlockingQueue<Runnable> viewOutput;
    private final LinkedBlockingQueue<Runnable> viewInput;

    private GameState state;
    private String username;

    private ClientConnection connection;

    private View view;

    private boolean challenger;
    private ArrayList<Gods> gods = new ArrayList<>(Arrays.asList(Gods.values()));
    private ArrayList<String> godToString;
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
            case CONNECTION_TOKEN: // CONNECTION
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
            case GOD_UPDATE:
                parseGodUpdate(message);
                break;
            case GOD_CHOSEN: // GOD_SELECTION
                parseGodChosen((GodChosen) message);
                break;
            case GOD_CHOICE: // GOD_SELECTION
                parseGodChoice(message);
                break;
        }
    }

    /* Connection */
    private void parseConnectionMessage(Message message) {
        if(state == GameState.CONNECTION) {
            username = message.getInfo();
            state = GameState.PRE_LOBBY;
        }
    }

    /* CREAZIONE PARTITA */////////////////////////////////////////////////////////////////
    private void parseSlotMessage(Message message) { // TEST CLI
        viewOutput.add(view::requestNumberOfPlayers);
    }

    public boolean validateNumberOfPlayers(String number) {
        if(number.equals("2") || number.equals("3")) { // Controllo su client - implementato anti-cheat su server
            sendMessage(new Message(MessageType.SLOTS_CHOICE, username, number));
            return true;
        }
        view.showInputText("The number you typed is not valid, please choose 2 or 3:");
        return false;
    }
    //^^^ CREAZIONE PARTITA ^^^////uwu/////////////////////////////////////////////////////////////////////////


    /* LOBBY *////////////////////////////////////////////////////////////////////////////
    public boolean validateUsername(String requestedUsername) {
        if(!players.containsKey(requestedUsername)) {
            return true;
        }
        view.showInputText("This username is already taken, choose a different one:");
        return false;
    }

    public boolean validateColor(String requestedColor) {
        if(!Colors.isValid(requestedColor)) {
            view.showInputText("The color selected does not exist, choose one of the available colors:");
            return false;
        }
        else if(players.containsValue(Colors.valueOf(requestedColor))) {
            view.showInputText("This color is already taken, choose a different one:");
            return false;
        }
        return true;
    }

    public void requestLogin(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(username, requestedUsername, color));
    }

    private void parseLobbyUpdate(LobbyUpdate message) {
        players = message.getPlayers();
        if(state == GameState.PRE_LOBBY) {
            state = GameState.LOGIN;
            viewInput.add(view::requestLogin);
            viewOutput.add(() -> view.updateLobby(message.getPlayers(), message.getColors()));
        }
        else if(state == GameState.LOGIN || state == GameState.LOBBY) {
            viewOutput.add(() -> view.updateLobby(message.getPlayers(), message.getColors()));
        }
    }

    private void parseDisconnectMessage(Message message) {
        viewOutput.add(() -> view.showInputText(message.getInfo()));
    }
    //^^^ LOBBY ^^^////////////////////////////////////////////////////////////////////////////

    private void parseInfoMessage(Message message) {
        System.out.println("\n\n\n"+message.getInfo()+"\n\n");
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
                viewInput.add(() -> view.showInputText("Waiting for other players to log")); // TEST
            }
            else {
                viewInput.add(view::requestLogin);
            }
        }
    }
    //^^^ GOD SELECTION ^^^///////////////////uwu/////////////////////////////////owo////////////////////////
    private void parseGodUpdate(Message message){
        if(message.getInfo().equals(username)){
            challenger = true;
            viewOutput.add(() -> view.addText("\nYou are the Challenger"));
            viewOutput.add(() -> view.updateGodSelection(gods));
            godToString = createGodListofString(gods); //Trovami un altro modo che sono sicuro esista @junkyboi
            viewInput.add(view::requestGods);
        }
        else {
            viewOutput.add(() -> view.addText("\nChallenger is: " + message.getInfo()));
            viewOutput.add(() -> view.updateGodSelection(gods));
        }
    }

    public boolean validateGods(String string){
        if(!godToString.contains(string)){
            view.showInputText("God not available, choose a different one:");
            return false;
        }
            godToString.remove(string);
            chosenGods.add(string);
        if(chosenGods.size() == players.keySet().size()){
            sendMessage(new GodChosen(username, "", chosenGods)); //Messaggio con gli dei scelti
            return true;
        }
        return false;
    }

    private ArrayList<String> createGodListofString(ArrayList<Gods> god){
        ArrayList<String> godToString = new ArrayList<>();
        for(Gods list: god){
            godToString.add(list.toString());
        }
        return godToString;
    }

    private ArrayList<Gods> createGodListofGods(ArrayList<String> god){
        ArrayList<Gods> gods = new ArrayList<>();
        for(String text: god){
            gods.add(Gods.valueOf(text));
        }
        return gods;
    }

    private void parseGodChosen(GodChosen message){
        chosenGods.clear();
        chosenGods.addAll(message.getGods());
        if(chosenGods.size() == 0 ){
            askStarter();
        }
        else{
            viewOutput.add(() -> view.updateGodSelection(createGodListofGods(message.getGods())));
        }
    }

    private void parseGodChoice(Message message){
        viewInput.add(view::requestGods);
    }

    public boolean validateGod(String string){
        if(!chosenGods.contains(string)){
            view.showInputText("This god isn't available, please choose a different one: ");
            return false;
        }
        sendMessage(new Message(MessageType.GOD_CHOICE,username, ""));
        return true;
    }

    private void askStarter(){
        viewInput.add(view::requestGods);
    }

    public boolean validatePlayer(String string){
         if(!players.containsKey(string)){
             view.showInputText("This player doesn't exist, choose again: ");
             return false;
         }
         sendMessage(new Message(MessageType.STARTER_PLAYER,username, string));
         return true;
    }








    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        //viewUpdate.add(() -> view.switchState(state));
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

}

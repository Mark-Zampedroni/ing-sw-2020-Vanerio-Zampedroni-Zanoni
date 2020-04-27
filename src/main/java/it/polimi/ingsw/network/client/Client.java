package it.polimi.ingsw.network.client;

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

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public abstract class Client extends Thread implements Observer<Message>, View {

    private GameState state;
    protected String username;

    private ClientConnection connection;

    protected boolean challenger; // Sicuro serva? Se lo usi solo nel metodo dichiaralo in locale li
    protected List<Gods> gods = new ArrayList<>(Arrays.asList(Gods.values()));
    // Per vedere se un dio è valido true/false usa -> Gods.isValid(string);
    protected List<String> godToString;
    // godToString in ogni momento vale -> Arrays.asList(Gods.values()).filter(g -> !chosenGods.contains(g)).map(g -> g.toString()).collect(Collectors.toList());
    protected List<String> chosenGods;
    protected Map<String, Colors> players;

    public Client(String ip, int port, int view) {
        chosenGods = new ArrayList<>();
        createConnection(ip,port);
        this.start();
    }

    private void viewRequest(Runnable request) {
        new Thread(request).start();
        // view.updateFrame();
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
            case GOD_START:
                parseGodStart(message);
                break;
            case GOD_ADD: // GOD_SELECTION
                parseAddGod(message);
                break;
            case GOD_REMOVE:
                parseRemoveGod(message);
                break;
            case GOD_PLAYERCHOICE: // GOD_SELECTION
                parseGodPlayerChoice(message);
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
        viewRequest(this::requestNumberOfPlayers);
    }

    public boolean validateNumberOfPlayers(String number) {
        if(number.equals("2") || number.equals("3")) { // Controllo su client - implementato anti-cheat su server
            sendMessage(new Message(MessageType.SLOTS_CHOICE, username, number));
            return true;
        }
        showInputText("The number you typed is not valid, please choose 2 or 3:");
        return false;
    }
    //^^^ CREAZIONE PARTITA ^^^////uwu/////////////////////////////////////////////////////////////////////////


    /* LOBBY *////////////////////////////////////////////////////////////////////////////
    public boolean validateUsername(String requestedUsername) {
        if(!players.containsKey(requestedUsername)) {
            return true;
        }
        showInputText("This username is already taken, choose a different one:");
        return false;
    }

    public boolean validateColor(String requestedColor) {
        if(!Colors.isValid(requestedColor)) {
            showInputText("The color selected does not exist, choose one of the available colors:");
            return false;
        }
        else if(players.containsValue(Colors.valueOf(requestedColor))) {
            showInputText("This color is already taken, choose a different one:");
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
            viewRequest(this::requestLogin);
            viewRequest(() -> updateLobby(message.getPlayers(), message.getColors()));
        }
        else if(state == GameState.LOGIN || state == GameState.LOBBY) {
            viewRequest(() -> updateLobby(message.getPlayers(), message.getColors()));
        }
    }

    private void parseDisconnectMessage(Message message) {
        viewRequest(() -> showInputText(message.getInfo()));
    }
    //^^^ LOBBY ^^^////////////////////////////////////////////////////////////////////////////

    private void parseInfoMessage(Message message) {
        System.out.println("\n\n\n"+message.getInfo()+"\n\n");
    }

    // TEST, NO MESSAGE MA ACTIONMESSAGE
    private void parseActionReply(Message message) {
        viewRequest(this::requestAction);
    }

    private void parseRegistrationReply(FlagMessage message) {
        if(state == GameState.LOGIN) {
            if(message.getFlag()) {
                username = message.getInfo(); // View registrata su Server
                state = GameState.LOBBY;
                viewRequest(() -> showInputText("Waiting for other players to log")); // TEST
            }
            else {
                viewRequest(this::requestLogin);
            }
        }
    }
    //* GOD SELECTION *///////////////////uwu/////////OwO/////////UwU/////////////owo////////////////////////
    private void parseGodStart(Message message){
        if(message.getInfo().equals(username)){
            challenger = true;
            viewRequest(() -> updateGameGods(gods));
            viewRequest(() -> showChallenger(message.getInfo(),challenger));
            godToString = gods.stream().map(Enum::toString).collect(Collectors.toList());
            viewRequest(this::requestGameGods);
        }
        else {
            viewRequest(() -> updateGameGods(gods));
            viewRequest(() -> showChallenger(message.getInfo(),challenger));
        }
    }

    public boolean validateGods(String requestedGod){
        if(!godToString.contains(requestedGod)){
            showInputText("God not available, choose a different one:");
            return false;
        }
            godToString.remove(requestedGod);
            chosenGods.add(requestedGod);
        sendMessage(new Message(MessageType.GOD_ADD, username, requestedGod));
        return chosenGods.size() == players.keySet().size();
    }

    private void parseAddGod(Message message){
        if(!challenger){
            chosenGods.add(message.getInfo());
            viewRequest(() -> showChosenGods(chosenGods));
        }
    }


    private void parseGodPlayerChoice(Message message){
        if(username.equals(message.getInfo())){
            viewRequest(this::requestPlayerGod);
        }
        else{
            viewRequest(() -> showChosenGods(chosenGods));
            viewRequest(() -> showPicking(message.getInfo()));
        }
    }

    public boolean validatePlayerGodChoice(String requestedGod){
        if(!chosenGods.contains(requestedGod)){
            showInputText("This god isn't available, please choose a different one: ");
            return false;
        }
        sendMessage(new Message(MessageType.GOD_PLAYERCHOICE,username, requestedGod));
        return true;
    }

    private void parseRemoveGod(Message message){
        chosenGods.remove(message.getInfo());
        if(chosenGods.size() == 0 && challenger){
            askStarter();
        }
        else{
            viewRequest(() -> showChosenGods(chosenGods));
        }
    }

    private void askStarter(){
        viewRequest(this::requestStarterPlayer);
    }

    public boolean validatePlayer(String string){
         if(!players.containsKey(string)){
             showInputText("This player doesn't exist, choose again: ");
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

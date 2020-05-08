package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.game.ActionRequestMessage;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.*;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.observer.Observer;
import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Client implements Observer<Message>, View {

    protected GameState state;
    protected String username;

    private final List<Runnable> requests;

    protected ClientConnection connection;

    protected String challenger;
    protected final List<String> chosenGods; // Si può usare solo la mappa gods qui sotto, usando delle key segnaposto che poi si tolgono
    protected Map<String, Colors> players;
    protected Map<String, String> gods;

    public Client() {
        chosenGods = new ArrayList<>();
        requests = new ArrayList<>();
    }

    private void viewRequest(Runnable request) {
        requests.add(request);
    }

    public void flushRequests() {
        requests.forEach(r -> new Thread(r).start());
        requests.clear();
    }

    public boolean createConnection(String ip, int port) {
        state = GameState.CONNECTION;
        try {
            connection = new ClientConnection(ip, port, this);
        } catch(IOException e) { return false; }
        return true;
    }

    public void update(Message message) {
        switch (message.getType()) {
            case CONNECTION_TOKEN: // CONNECTION
                parseConnectionMessage(message);
                break;
            case SLOTS_CHOICE: // PRE-LOBBY
                parseSlotMessage();
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
            case STATE_UPDATE: // TUTTE
                parseStateUpdate((StateUpdateMessage) message);
                break;
            case CHALLENGER_SELECTION:
                parseChallengerSelection(message);
                break;
            case SELECTED_GODS_CHANGE: // GOD_SELECTION
                parseManagementMessage((FlagMessage) message);
                break;
            case ASK_PLAYER_GOD: // GOD_SELECTION
                parseGodPlayerChoice(message);
                break;
            case TURN_UPDATE:
                parseTurnUpdate((ActionRequestMessage) message);
                break;
            default: //
        }
        flushRequests();
    }

    /* Connection */
    private void parseConnectionMessage(Message message) {
        if(state == GameState.CONNECTION) {
            username = message.getInfo();
            state = GameState.PRE_LOBBY;
        }
    }

    /* CREAZIONE PARTITA */////////////////////////////////////////////////////////////////
    private void parseSlotMessage() { // TEST CLI
        viewRequest(this::requestNumberOfPlayers);
    }

    public boolean validateNumberOfPlayers(String number) {
        if(number.equals("2") || number.equals("3")) { // Controllo su client - implementato anti-cheat su server
            sendMessage(new Message(MessageType.SLOTS_CHOICE, username, number));
            return true;
        }
        return false;
    }
    //^^^ CREAZIONE PARTITA ^^^////uwu/////////////////////////////////////////////////////////////////////////


    /* LOBBY *////////////////////////////////////////////////////////////////////////////
    public boolean validateUsername(String requestedUsername) {
        return !players.containsKey(requestedUsername);
    }

    public boolean validateColor(String requestedColor) {
        return !(!Colors.isValid(requestedColor) || players.containsValue(Colors.valueOf(requestedColor)));
    }

    public void requestLogin(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(username, requestedUsername, color));
    }

    private void parseLobbyUpdate(LobbyUpdate message) {
        players = message.getPlayers();
        if(state == GameState.PRE_LOBBY) {
            state = GameState.LOGIN;
            viewRequest(this::requestLogin);
        }
        if(state == GameState.LOGIN || state == GameState.LOBBY) {
            viewRequest(() -> updateLobby(message.getColors()));
        }
    }

    private void parseDisconnectMessage(Message message) {
        viewRequest(() -> showInputText(message.getInfo()));
    }
    //^^^ LOBBY ^^^////////////////////////////////////////////////////////////////////////////

    private void parseInfoMessage(Message message) {
        viewRequest(() -> showInfo(message.getInfo()));
    }

    private void parseRegistrationReply(FlagMessage message) {
        if(state == GameState.LOGIN) {
            if(message.getFlag()) {
                username = message.getInfo(); // View registrata su Server
                state = GameState.LOBBY;
            }
            else {
                viewRequest(this::requestLogin);
            }
        }
    }
    //* GOD SELECTION *///////////////////uwu/////////OwO/////////UwU/////////////owo////////////////////////
    private void parseChallengerSelection(Message message) {
        gods = new HashMap<>();
        challenger = message.getInfo();
        if(message.getInfo().equals(username) && chosenGods.size() != players.size()) {
            viewRequest(this::requestChallengerGod);
        }
        viewRequest(this::updateChallengerGodSelection);
    }

    public boolean validateGods(String requestedGod){
        if(!getStringAvailableGods().contains(requestedGod)){
            showInputText("God not available, choose a different one:");
            return false;
        }
        sendMessage(new FlagMessage(MessageType.SELECTED_GODS_CHANGE, username, requestedGod, true));
        return true;
    }

    private void parseManagementMessage(FlagMessage message){
        if (message.getFlag()) {
            addGod(message);
        } else {
            removeGod(message);
        }
    }

    private void addGod(Message message){
        chosenGods.add(message.getInfo());
        if(chosenGods.size() != players.size()) {
            viewRequest(this::updateChallengerGodSelection);
            if (challenger.equals(username)) {
                viewRequest(this::requestChallengerGod);
            }
        }
    }

    private void parseGodPlayerChoice(Message message){
        gods.put(message.getInfo(),""); // Adding player in map
        viewRequest(this::updatePlayerGodSelection);
        if(username.equals(message.getInfo())){
            viewRequest(this::requestPlayerGod);
        }
        else {
            viewRequest(() -> showInputText(message.getInfo()+" is choosing ..."));
        }
    }

    public boolean validatePlayerGodChoice(String requestedGod){
        if(!chosenGods.contains(requestedGod)){
            showInputText("This god isn't available, please choose a different one: ");
            return false;
        }
        sendMessage(new Message(MessageType.ASK_PLAYER_GOD, username, requestedGod));
        return true;
    }

    private void removeGod(Message message) {
        chosenGods.remove(message.getInfo());
        for(String player: gods.keySet()) {
            if(gods.get(player).equals("")) {
                gods.replace(player, message.getInfo());
            }
        }
        viewRequest(this::updatePlayerGodSelection);
        if(chosenGods.size() == 0) {
            if(challenger.equals(username)) {
                viewRequest(this::requestStarterPlayer);
            }
            else {
                viewRequest(() -> showInputText("The Challenger is choosing the starter player ..."));
            }
            viewRequest(this::showAvailablePlayers);
        }
    }

    public boolean validatePlayer(String string) {
         if(!players.containsKey(string)) {
             showInputText("This player doesn't exist, choose again: ");
             return false;
         }
         sendMessage(new Message(MessageType.STARTER_PLAYER, username, string));
         return true;
    }


    /*GAME*//////////////////////////////////////////////////////////////////////////////////////////

    private void parseTurnUpdate(ActionRequestMessage message) {
        System.out.println(message.getGameUpdate());
        if(username.equals(message.getInfo())) {
            viewRequest(() -> requestTurnAction(message.getPossibleActions()));
            //Aggiungere stampa board
        }
    }

    public boolean validateAction(int range, int value) {
        return value >= 0 && value <= range;
    }

    public boolean validatePosition(List<DtoPosition> possiblePositions, int x, int y){
        return possiblePositions.stream().anyMatch(p -> p.getX() == x && p.getY() == y);
    }


    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        //viewUpdate.add(() -> view.switchState(state));
    }

    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    protected List<String> getStringAvailableGods() {
        return getAvailableGods().stream().map(Enum::toString).collect(Collectors.toList());
    }

    protected List<Gods> getAvailableGods() {
        return Arrays.stream(Gods.values()).filter(god -> !chosenGods.contains(god.toString())).collect(Collectors.toList());
    }




}

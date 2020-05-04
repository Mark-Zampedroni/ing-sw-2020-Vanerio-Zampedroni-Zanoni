package it.polimi.ingsw.MVC;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.*;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;

import java.util.List;
import java.util.Map;

public class TestClient extends Client {


    private boolean printMessages;

    String challenger;

    public TestClient(String ip, int port, int view, boolean showLog) {
        super(ip, port, view);
        this.printMessages = showLog;
    }


    public void interrupt() {
        connection.disconnect();
    }

    public void requestRegister(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(this.username, requestedUsername, color));
    }

    public void requestRegister(String sender, String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(sender, requestedUsername, color));
    }

    public void requestChallengerGod(String god) {
        sendMessage(new FlagMessage(MessageType.SELECTED_GODS_CHANGE, username, god, true));
    }

    public void requestPlayerGod(String god) {
        sendMessage(new Message(MessageType.ASK_PLAYER_GOD, username, god));
    }

    public void requestAction(Action action, DTOposition position) {
        sendMessage(new ActionMessage(username, "Test action request", action, position));
    }

    public void requestPlayersNumber(String number) {
        sendMessage(new Message(MessageType.SLOTS_CHOICE, username, number));
    }

    public void requestStarterPlayer(String starterPlayer) {
        sendMessage(new Message (MessageType.STARTER_PLAYER, username, starterPlayer));
    }

    public boolean isChallenger() {
            return (username != null) && username.equals(challenger);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void update(Message message) {
        if(printMessages) { System.out.println(username+" RECEIVED FROM SERVER:\n"+message+"\n"); }
        switch(message.getType()) {
            case CONNECTION_TOKEN:
                super.update(message); // REGISTRA TESTCLIENT
                break;
            case REGISTRATION:
                if(((FlagMessage) message).getFlag()) { username = message.getInfo(); }
                break;
            case CHALLENGER_SELECTION:
                challenger = message.getInfo();
                break;
        }
    }

    @Override
    public void sendMessage(Message message) {
        if(printMessages) { System.out.println(username+" SENDING TO SERVER:\n"+message+"\n"); }
        super.sendMessage(message);
    }

    @Override
    public void showInputText(String text) { }
    @Override
    public void showInfo(String text) { }
    @Override
    public void requestNumberOfPlayers() { }
    @Override
    public void updateLobby(List<Colors> availableColors) { }
    @Override
    public void requestLogin() { }
    @Override
    public void updateChallengerGodSelection() { }
    @Override
    public void requestChallengerGod() { }
    @Override
    public void updatePlayerGodSelection() { }
    @Override
    public void requestPlayerGod() { }
    @Override
    public void showAvailablePlayers() { }
    @Override
    public void requestStarterPlayer() { }
    @Override
    public void showPlayerGod() { }
    @Override
    public void requestTurnAction(Map<Action, List<DTOposition>> possibleActions) { }
    @Override
    public void showBoard(DTOsession session) { }
}

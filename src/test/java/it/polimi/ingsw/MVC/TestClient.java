package it.polimi.ingsw.MVC;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientConnection;
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

    public TestClient(String ip, int port, int view) {
        super(ip, port, view);
    }

    public void requestRegister(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(this.username, requestedUsername, color));
    }

    public void requestChallengerGod(String requestedUsername, String god) {
        sendMessage(new FlagMessage(MessageType.SELECTED_GODS_CHANGE, requestedUsername, god, true));
    }

    public void requestPlayerGod(String username, String god) {
        sendMessage(new Message(MessageType.ASK_PLAYER_GOD, username, god));
    }

    public void requestAction(String username, Action action, DTOposition position) {
        sendMessage(new ActionMessage(username, null, action, position));
    }

    public void requestTwoPlayers() {
        sendMessage(new Message(MessageType.SLOTS_CHOICE, username, "2"));
    }

    public void requestStarterPlayer(String username, String starterPlayer) {
        sendMessage(new Message (MessageType.STARTER_PLAYER, username, starterPlayer));
    }

    @Override
    public void update(Message message) {
        System.out.println("RECEIVED FROM SERVER:\n"+message+"\n");
        if(message.getType() == MessageType.CONNECTION_TOKEN) {
            super.update(message); // REGISTRA TESTCLIENT
        }
    }

    @Override
    public void sendMessage(Message message) {
        System.out.println("SENDING TO SERVER:\n"+message+"\n");
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

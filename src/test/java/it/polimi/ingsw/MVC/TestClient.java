package it.polimi.ingsw.MVC;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;
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

    public void requestTwoPlayers() {
        sendMessage(new Message(MessageType.SLOTS_CHOICE, username, "2"));
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

package it.polimi.ingsw.view;

import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;

public interface View {


    void showMessage(String string);
    void updateLobby(LobbyUpdate message);
    void requestLogin();
    void requestAction();
    void denyLogin();
    void requestReady();
    void switchState(GameState state); // Enum con varie finestre ? No bello se passa state
    //void requestUnready();

}

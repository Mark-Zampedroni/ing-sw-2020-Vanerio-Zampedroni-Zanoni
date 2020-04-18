package it.polimi.ingsw.view;

import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;

public interface View {


    void showMessage(String string);
    void updateLobby(LobbyUpdate message);
    void requestLogin();
    void requestAction();
    void denyLogin();
    void requestReady();
    void requestUnready();

}

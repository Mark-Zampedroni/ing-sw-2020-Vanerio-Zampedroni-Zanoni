package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.network.messages.lobby.GodUpdate;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;

import java.util.ArrayList;
import java.util.Map;

public interface View {


    void showMessage(String string);
    void updateLobby(LobbyUpdate message);
    void requestLogin();
    void requestAction();
    void denyLogin();
    void showLogged();
    void displayGods(GodUpdate message);
    void godSelection(Map<String, ArrayList<String>> gods);
    void Starter(ArrayList<String> string );
    void displayString(ArrayList<String> string, String text);
    void godAssignment(ArrayList<String> gods);
    void switchState(GameState state); // Enum con varie finestre ? No bello se passa state
    //void requestUnready();

}
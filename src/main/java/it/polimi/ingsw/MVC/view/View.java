package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.network.messages.lobby.GodUpdate;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;

import java.util.ArrayList;
import java.util.Map;

public interface View {

    // NO PASSARE MESSAGGI ! VANNO MESSI COME ARGOMENTI LIST O STRING

    // Prefissi :
    // show -> mostra roba come messaggio (vicino all'output / finestra popup)
    // update -> uno per tipo di finestra, si passano le variabili per costruirlo (pezzi fissi + variabili)
    // request -> attesa di input che verrà poi parsato nel client

    void showMessage(String string);
    void updateLobby(LobbyUpdate message);
    void requestLogin();
    void requestAction();
    void showLogged();
    void displayGods(GodUpdate message);
    void godSelection(Map<String, ArrayList<String>> gods);
    void starter(ArrayList<String> string );
    void displayString(ArrayList<String> string, String text);
    void godAssignment(ArrayList<String> gods);
    //void requestUnready();

}

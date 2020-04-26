package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface View {

    // NO PASSARE MESSAGGI ! VANNO MESSI COME ARGOMENTI LIST O STRING

    // Prefissi :
    // show -> mostra roba come messaggio (vicino all'output / finestra popup)
    // update -> uno per tipo di finestra, si passano le variabili per costruirlo (pezzi fissi + variabili)
    // request -> attesa di input che verrà poi parsato nel client
    void addText(String text);
    void showInputText(String text);
    //Lobby
    void updateLobby(Map<String, Colors> players, List<Colors> availableColors); // Aggiorna la LOBBY
    void requestNumberOfPlayers(); // Chiede al primo giocatore da quante persone fare la partita
    void requestLogin(); // Chiede al giocatore di scegliere un username e un colore
    //Scelta dio / starte
    void updateGodSelection(ArrayList<Gods> gods);//Aggiorna con la lista degli dei disponibili e la loro descrizione
    void requestGods(); //Chiede al challenger gli dei per il gioco
    void requestSingleGod();
    void requestStarter();

    //Board
    void requestAction(); // TEST

}

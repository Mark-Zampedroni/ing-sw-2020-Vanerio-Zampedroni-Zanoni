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



    // ^^^ Sta roba non vuole dire nulla,
    // è come invocare dei metodi dello SceneBuilder direttamente, la GUI non saprebbe interpretarla
    // con solo una stringa come argomento. Se devi mostrare testo chiama metodi di SceneBuilder direttamente nella CLI.



    void showInputText(String text);
    //Lobby
    void updateLobby(Map<String, Colors> players, List<Colors> availableColors); // Aggiorna la LOBBY
    void requestNumberOfPlayers(); // Chiede al primo giocatore da quante persone fare la partita
    void requestLogin(); // Chiede al giocatore di scegliere un username e un colore
    //Scelta dio / starter
    void updateGameGods(List<Gods> gods);//Aggiorna con la lista degli dei disponibili e la loro descrizione
    void requestGameGods(); //Chiede al challenger gli dei per il gioco
    void showChallenger(String name, boolean flag);
    void showChosenGods(List<String> name);
    void requestStarterPlayer();
    void requestPlayerGod();
    void showPicking(String name);
    //Board
    void requestAction(); // TEST

}

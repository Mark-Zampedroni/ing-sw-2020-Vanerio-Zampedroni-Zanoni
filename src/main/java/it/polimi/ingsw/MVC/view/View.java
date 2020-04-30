package it.polimi.ingsw.MVC.view;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;

import java.util.List;
import java.util.Map;


public interface View {

    // Prefissi :
    // show -> mostra roba come messaggio (vicino all'output / finestra popup)
    // update -> uno per tipo di finestra, si passano le variabili per costruirlo (pezzi fissi + variabili)
    // request -> attesa di input che verrà poi parsato nel client


    // Da togliere -> i testi vanno spostati in CLI
    void showInputText(String text);
    void showInfo(String text);
    //Pre-Lobby
    void requestNumberOfPlayers(); // Chiede al primo giocatore da quante persone fare la partita
    //Lobby
    void updateLobby(List<Colors> availableColors); // Aggiorna la LOBBY
    void requestLogin(); // Chiede al giocatore di scegliere un username e un colore
    //Scelta dei da parte del Challenger
    void updateChallengerGodSelection(); // Aggiorna i player durante la scelta del Challenger
    void requestChallengerGod(); //Chiede di scegliere un dio da aggiungere al gioco
    //Scelta dio di ogni giocatore
    void updatePlayerGodSelection(); // Aggiorna i player durante le scelte del dio di ogni giocatore
    void requestPlayerGod(); // Chiede di scegliere un dio da usare
    //Scelta starter player
    void showAvailablePlayers(); // Mostra i giocatori disponibili con i loro dei
    void requestStarterPlayer(); // Chiede di scegliere lo starter player
    void showPlayer_God();
    //Richiesta azione durante il gioco
    void requestTurnAction(Map<Action, List<DTOposition>> possibleActions);

}

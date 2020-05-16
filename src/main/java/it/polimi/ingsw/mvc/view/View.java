package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;
import it.polimi.ingsw.utility.serialization.dto.DtoSession;

import java.util.List;
import java.util.Map;


public interface View {


    // Da togliere -> i testi vanno spostati in CLI
    void showInputText(String text);
    void showInfo(String text); // Generico per mostrare testo nel box input XXXXXXXXXXXXXXXXX
    //Pre-Lobby
    void requestNumberOfPlayers(); // Chiede al primo giocatore da quante persone fare la partita XXXXXXXXXXXXX
    //Lobby
    void showLobby(List<Colors> availableColors); // Aggiorna la LOBBY XXXXXXXXXXXXXXXXX
    void requestLogin(); // Chiede al giocatore di scegliere un username e un colore XXXXXXXXXXXXXXXXX
    //Scelta dei da parte del Challenger
    void updateChallengerGodSelection(); // Aggiorna i player durante la scelta del Challenger
    void requestChallengerGod(List<String> chosenGods); //Visualizza schermata al challenger, chiede in input un dio XXXXXXXXX
    //Scelta dio di ogni giocatore
    void updatePlayerGodSelection(); // Aggiorna i player durante le scelte del dio di ogni giocatore
    void requestPlayerGod(); // Chiede di scegliere un dio da usare
    //Scelta starter player
    void showAvailablePlayers(); // Mostra i giocatori disponibili con i loro dei
    void requestStarterPlayer(); // Chiede di scegliere lo starter player
    void showPlayerGod();
    //Richiesta azione durante il gioco
    void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions);
    void showBoard(DtoSession session);

}

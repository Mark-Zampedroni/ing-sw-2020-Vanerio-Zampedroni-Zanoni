package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.util.List;
import java.util.Map;


public interface View {


    void showInfo(String text); // Generico per mostrare testo nel box input XXXXXXXXXXXXXXXXX
    //Pre-Lobby
    void requestNumberOfPlayers(); // Chiede al primo giocatore da quante persone fare la partita XXXXXXXXXXXXX
    //Lobby
    void showLobby(List<Colors> availableColors); // Aggiorna la LOBBY XXXXXXXXXXXXXXXXX
    void requestLogin(); // Chiede al giocatore di scegliere un username e un colore XXXXXXXXXXXXXXXXX
    //Scelta dei da parte del Challenger
    void updateChallengerGodSelection(List<String> chosenGods); // Aggiorna i player durante la scelta del Challenger XXXXXX
    void requestChallengerGod(List<String> chosenGods); //Visualizza schermata al challenger, chiede in input un dio XXXXXXXXX
    //Scelta dio di ogni giocatore
    void updatePlayerGodSelection(String turnOwner, Map<String,String> choices, List<String> chosenGods); // Aggiorna i player durante le scelte del dio di ogni giocatore XXXXXX
    void requestPlayerGod(List<String> chosenGods, Map<String,String> choices); // Chiede di scegliere un dio da usare XXXXXX
    //Scelta starter player
    void updateStarterPlayerSelection(Map<String,String> choices); // Mostra i giocatori disponibili con i loro dei XXXXXXXXX
    void requestStarterPlayer(Map<String,String> choices); // Chiede di scegliere lo starter player
    //Richiesta azione durante il gioco
    void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String,Colors> colors, Map<String,String> gods, boolean specialPower);
    void showBoard(DtoSession session, Map<String,Colors> colors, Map<String,String> gods);
    //In caso di crash
    void showReconnection(boolean isReconnecting);
    void showDisconnected(String info);

}

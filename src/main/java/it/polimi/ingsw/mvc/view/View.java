package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.util.List;
import java.util.Map;


public interface View {


    void showQueueInfo(String text);
    //Pre-Lobby
    void requestNumberOfPlayers();
    //Lobby
    void showLobby(List<Colors> availableColors);
    void requestLogin();
    //Scelta dei da parte del Challenger
    void updateChallengerGodSelection(List<String> chosenGods);
    void requestChallengerGod(List<String> chosenGods);
    //Scelta dio di ogni giocatore
    void updatePlayerGodSelection(String turnOwner, Map<String,String> choices, List<String> chosenGods);
    void requestPlayerGod(List<String> chosenGods, Map<String,String> choices);
    //Scelta starter player
    void updateStarterPlayerSelection(Map<String,String> choices);
    void requestStarterPlayer(Map<String,String> choices);
    //Richiesta azione durante il gioco
    void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String,Colors> colors, Map<String,String> gods, boolean specialPower);
    void showBoard(DtoSession session, Map<String,Colors> colors, Map<String,String> gods);
    //In caso vincita/perdita player
    void showWin(String playerName);
    void showLose(String playerName);
    //In caso di crash
    void showReconnection(boolean isReconnecting);
    void showDisconnected(String info);

}

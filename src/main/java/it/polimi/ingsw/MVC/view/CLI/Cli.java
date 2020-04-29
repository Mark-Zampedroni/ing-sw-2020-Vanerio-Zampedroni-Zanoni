package it.polimi.ingsw.MVC.view.CLI;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.MVC.view.View;


/*
    request -> cambiano solo la riga di output e quella sopra, chiedendo di inserire qualcosa
    show -> cambiano la riga sopra quella di output per mostrare un messaggio
    update -> refresha lo scenario aggiornando le variabili

    Variabili:
    LOBBY -> Giocatore, colore
    GOD_SELECTION -> Dei disponibili / dei scelti
    BOARD -> Contenuto delle celle, giocatori, ? ... altro

 */
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class Cli extends Client {


    private Scanner input;

    private String inputSave = "";

    public Cli(String ip, int port, int view) {

        super(ip, port, view);
        input = new Scanner(System.in);
        waitConnectionRequest(ip, port);

    }

    private String requestInput() {
        while(true) {
            try {
                return input.nextLine();
            } catch (IndexOutOfBoundsException e) { /* nothing */ }
        }
    }

    private void waitConnectionRequest(String ip, int port) {
        SceneBuilder.printStartScreen("Press [ENTER] when you are ready");
        requestInput(); // waiting for [ENTER]
        while(!createConnection(ip, port)) {
            SceneBuilder.printStartScreen("Connection failed! The server is unreachable, press [ENTER] to try again");
            requestInput(); // waiting for [ENTER]
        }
    }

    /* CREAZIONE PARTITA *///////////////////////////////////////////////////////////////////////////////////////
    public void requestNumberOfPlayers() {
        SceneBuilder.printStartScreen("You are the first player to connect!\nchoose if you want to play as 2 or 3 people (Type 2 or 3)");
        while(!validateNumberOfPlayers(requestInput().toUpperCase())) {
            SceneBuilder.printStartScreen("The number you typed is not valid, please choose 2 or 3:");
        }
    }

    public void showInputText(String text) {
        SceneBuilder.putOutputRequest("\n"+text);
        SceneBuilder.printScene();
    }

    public void showInfo(String text) {
        switch(state) {
            case PRE_LOBBY:
                SceneBuilder.printStartScreen(text);
                break;
        }

    }

    //^^^ CREAZIONE PARTITA ^^^///////////////////////////////////

    /* LOBBY *///////////////////////////////////////////////////////////////////////////////////////
    public void requestLogin() {
        String requestedUsername = requestLobbyInput("Input username:",
                                                       "This username is already taken, choose a different one:",
                                                            (username) -> !validateUsername(username));
        String requestedColor = requestLobbyInput("Choose one of the available colors:",
                                                    "This color does not exist or was already chosen, select a different one:",
                                                          (color) -> !validateColor(color.toUpperCase())).toUpperCase();
        requestLogin(requestedUsername, Colors.valueOf(requestedColor));
    }

    private String requestLobbyInput(String request, String error, Function<String,Boolean> check) {
        inputSave = request;
        SceneBuilder.printLobbyScreen(inputSave, players);
        String someInput = requestInput();
        while(check.apply(someInput)) {
            someInput = showWrongInput(error);
        }
        return someInput;
    }

    private String showWrongInput(String text) {
        inputSave = text;
        SceneBuilder.printLobbyScreen(inputSave, players);
        return requestInput();
    }

    public void updateLobby(List<Colors> availableColors) {
        if(players.containsKey(username)) {
            inputSave = "Waiting for other players to join ...";
        }
        SceneBuilder.printLobbyScreen(inputSave, players);
        // Vanno mostrati i colori -> da fare metodo showRequest in SceneBuilder per cambiare solo una Stringa
        // "request" che si utilizza in tutti gli scenari, diventa più facile modificare solo un pezzo quando arriva
        // info, vale anche per la pre-lobby e le cose da fare dopo !
    }

    //^^^ LOBBY ^^^//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void requestAction() { // Test
        showInputText("You are registered!\nType your message:  ");
        String content = requestInput();  // Read user input
        // TEST
        sendMessage(new ActionMessage(username, content, Action.MOVE, new Position(0, 0), new Worker(new Position(0, 0))));
    }
    //*GOD SELECTION*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateChallengerGodSelection() {
        SceneBuilder.clearScenario();
        showAvailableGods();
        if(!chosenGods.isEmpty()) { showChosenGods(); }
        showInputText((challenger.equals(username))
                ? "You are the challenger! Choose "+players.size()+" gods\nType the name of 1 god:"
                : "You are not the challenger, wait while " +challenger+" chooses "+players.size()+" gods");
    }

    public void updatePlayerGodSelection() {
        SceneBuilder.clearScenario();
        showChosenGods();
        showPlayer_God();
        SceneBuilder.printScene();
    }

    public void showAvailablePlayers() {
        SceneBuilder.clearScenario();
        SceneBuilder.addToScenario("Players:\n");
        players.keySet().forEach(p -> SceneBuilder.addToScenario(p+" with "+players.get(p).toString()+"\n"));
        SceneBuilder.printScene();
    }

    public void showPlayer_God() {
        SceneBuilder.addToScenario("Players with their God:\n");
        gods.keySet().forEach(p -> SceneBuilder.addToScenario(p+" with "+gods.get(p)+"\n"));
    }

    public void requestChallengerGod(){
        while(!validateGods(requestInput().toUpperCase()));
    }

    private void showAvailableGods() {
        SceneBuilder.addToScenario("Available gods:\n");
        getAvailableGods().forEach(g -> SceneBuilder.addToScenario("Name: " + g.toString() + ", Description: " + g.getDescription() + "\n"));
    }

    public void showChosenGods() {
        SceneBuilder.addToScenario("\nChosen gods: \n");
        chosenGods.stream().map(Gods::valueOf).forEach(g ->  SceneBuilder.addToScenario("Name: "+g.toString()+", Description: "+g.getDescription()+"\n"));
    }

    public void requestPlayerGod(){
        showInputText("Choose one of the available gods:");
        while(!validatePlayerGodChoice(requestInput().toUpperCase()));
    }

    public void requestStarterPlayer(){
        showInputText("Choose the starting player: ");
        while(!validatePlayer(requestInput()));
    }


}

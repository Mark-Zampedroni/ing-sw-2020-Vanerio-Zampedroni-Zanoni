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

public class Cli extends Client {


    private Scanner input;

    public Cli(String ip, int port, int view) {

        super(ip, port, view);
        input = new Scanner(System.in);
    }

    private String requestInput() {
        while(true) {
            try {
                return input.nextLine();
            } catch (IndexOutOfBoundsException e) { /* nothing */ }
        }
    }

    /* CREAZIONE PARTITA *///////////////////////////////////////////////////////////////////////////////////////
    public void requestNumberOfPlayers() {
        showInputText("You are the first player who connected: choose if you want to play as 2 or 3 people :\n(Type 2 or 3) ");
        while(!validateNumberOfPlayers(requestInput().toUpperCase()));
    }

    public void showInputText(String text) {
        SceneBuilder.putOutputRequest("\n"+text);
        SceneBuilder.printScene();
    }

    //^^^ CREAZIONE PARTITA ^^^///////////////////////////////////

    /* LOBBY *///////////////////////////////////////////////////////////////////////////////////////
    public void requestLogin() {
        String requestedUsername = requestUsername();
        String requestedColor = requestColor();
        requestLogin(requestedUsername, Colors.valueOf(requestedColor));
    }

    private String requestUsername() {
        showInputText("Input username:");
        String requestedUsername;
        do {
            requestedUsername = requestInput();
        }
        while(!validateUsername(requestedUsername));
        return requestedUsername;
    }

    private String requestColor() {
        showInputText("Choose one of the available colors:");
        String requestedColor;
        do {
            requestedColor = requestInput().toUpperCase();
        }
        while(!validateColor(requestedColor));
        return requestedColor;
    }

    public void updateLobby(List<Colors> availableColors) {
        SceneBuilder.clearScenario();
        SceneBuilder.addToScenario("Current players:\n");
        if(players.keySet().isEmpty()) { SceneBuilder.addToScenario("No one registered yet\n"); }
        players.keySet().stream().forEach(s -> SceneBuilder.addToScenario("Name: "+s+", Color: "+players.get(s)+"\n"));
        SceneBuilder.addToScenario("\nAvailable colors: "+availableColors+"\n");
        SceneBuilder.printScene();
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
        showInputText((challenger)
                ? "You are the challenger! Choose "+players.size()+" gods\nType the name of 1 god:"
                : "You are not the challenger, wait while he chooses "+players.size()+" gods");
    }

    public void updatePlayerGodSelection() {
        SceneBuilder.clearScenario();
        showChosenGods();
        SceneBuilder.printScene();
    }

    public void showAvailablePlayers() {
        SceneBuilder.clearScenario();
        SceneBuilder.addToScenario("Players:\n");
        players.keySet().forEach(p -> SceneBuilder.addToScenario(p+" , "+players.get(p).toString()+"\n"));
        SceneBuilder.printScene();
    }

    public void requestChallengerGod(){
        while(!validateGods(requestInput().toUpperCase()));
    }

    private void showAvailableGods() {
        SceneBuilder.addToScenario("Available gods:\n");
        getAvailableGods().forEach(g -> SceneBuilder.addToScenario("Name: " + g.toString() + ", Description: " + g.getDescription() + "\n"));
    }
    private void showChosenGods() {
        SceneBuilder.addToScenario("\nChosen gods: \n");
        chosenGods.stream().map(god -> Gods.valueOf(god)).forEach(g ->  SceneBuilder.addToScenario("Name: "+g.toString()+", Description: "+g.getDescription()+"\n"));
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

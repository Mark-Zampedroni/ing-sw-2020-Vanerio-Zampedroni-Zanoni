package it.polimi.ingsw.MVC.view.CLI;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
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

public class Cli implements View {

    private String username;
    private Colors color;

    private List<Colors> colors;

    private Client client;
    private Scanner input;

    public Cli(Client client) {
        colors = new ArrayList<>(Arrays.asList(Colors.values()));

        /*
        Toolkit tk = Toolkit.getDefaultToolkit();
        double width = tk.getScreenSize().getWidth();
        double height = tk.getScreenSize().getHeight();

        for(int x = 0; x < height/40; x++) { System.out.println(""); } // Dovrebbe stampare righe vuote fino a metà schermo
        screenTest(width,9.15);
         */

        this.client = client;
        input = new Scanner(System.in);
    }

    /* TEST FOR SCREEN WIDTH
    private void screenTest(double width, double divisor) {
        System.out.println("\n\n"+divisor);
        for(int x = 0; x < Toolkit.getDefaultToolkit().getScreenSize().getWidth()/9.15; x++) {
            System.out.print("A");
        }
    }*/


    /* CREAZIONE PARTITA *///////////////////////////////////////////////////////////////////////////////////////
    public void requestNumberOfPlayers() {
        showInputText("You are the first player who connected: choose if you want to play as 2 or 3 people :\n(Type 2 or 3) ");
        while(!client.validateNumberOfPlayers(input.nextLine().toUpperCase()));
    }

    public void showInputText(String text) {
        SceneBuilder.putOutputRequest("\n"+text);
        SceneBuilder.printScene();
    }

    public void addText(String text) {
        SceneBuilder.addToScenario("\n"+text);
        SceneBuilder.printScene();
    }

    //^^^ CREAZIONE PARTITA ^^^///////////////////////////////////

    /* LOBBY *///////////////////////////////////////////////////////////////////////////////////////
    public void requestLogin() {
        String requestedUsername = requestUsername();
        String requestedColor = requestColor();
        client.requestLogin(requestedUsername, Colors.valueOf(requestedColor));
    }

    private String requestUsername() {
        showInputText("Input username:");
        String requestedUsername;
        do {
            requestedUsername = input.nextLine();
        }
        while(!client.validateUsername(requestedUsername));
        return requestedUsername;
    }

    private String requestColor() {
        showInputText("Choose one of the available colors:");
        String requestedColor;
        do {
            requestedColor = input.nextLine().toUpperCase();
        }
        while(!client.validateColor(requestedColor));
        return requestedColor;
    }

    public void updateLobby(Map<String, Colors> players, List<Colors> availableColors) {
        SceneBuilder.clearScenario();
        SceneBuilder.addToScenario("Current players:\n");
        if(players.keySet().isEmpty()) { SceneBuilder.addToScenario("No one registered yet\n"); }
        for(String s : players.keySet()) {
           SceneBuilder.addToScenario("Name: "+s+", Color: "+players.get(s)+"\n");
        }
        SceneBuilder.addToScenario("\nAvailable colors: "+availableColors+"\n");
        SceneBuilder.printScene();
    }

    //^^^ LOBBY ^^^//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void requestAction() { // Test
        showInputText("You are registered!\nType your message:  ");
        String content = input.nextLine();  // Read user input
        // TEST
        client.sendMessage(new ActionMessage(username, content, Action.MOVE, new Position(0, 0), new Worker(new Position(0, 0))));
    }
    //^^^ GOD SELECTION ^^^//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateGodSelection(ArrayList<Gods> gods) {
        SceneBuilder.clearScenario();
        SceneBuilder.addToScenario("Available gods:\n");
        for(Gods god : gods) {
            SceneBuilder.addToScenario("Name: "+god.toString()+", Description: "+god.getDescription()+"\n");
        }
        SceneBuilder.printScene();
    }

    public void requestGods(){
        showInputText("Choose three of the available gods:");
        String requestedGod;
        do {
            requestedGod = input.nextLine().toUpperCase();
        }
        while(!client.validateGods(requestedGod));
    }

    public void requestSingleGod(){
        showInputText("Choose one of the available gods:");
        String requestedGod;
        do {
            requestedGod = input.nextLine().toUpperCase();
        }
        while(!client.validateGod(requestedGod));
    }

    public void requestStarter(){
        showInputText("Choose the starting player: ");
        String requestedPlayer;
        do {
            requestedPlayer = input.nextLine().toUpperCase();
        }
        while(!client.validatePlayer(requestedPlayer));
    }


}

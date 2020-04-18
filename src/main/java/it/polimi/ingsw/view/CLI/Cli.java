package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.net.client.Client;
import it.polimi.ingsw.net.messages.FlagMessage;
import it.polimi.ingsw.net.messages.game.ActionMessage;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.view.View;


/*
    Commento di Mark:

    Bisogna trovare un modo per non dover chiamare il sendMessage del client direttamente dalla view.
    Magari facendo una classe per la costruzione dei messaggi, perchè sono usati uguali sia da CLI che da GUI ...

 */
import java.util.*;

public class Cli implements View {

    private String username;
    private Colors color;

    private List<Colors> colors;

    private Screen inputScreen;
    private Screen outputScreen;

    private Client client;
    private Scanner input;

    public Cli(Client client) {
        colors = new ArrayList<>(Arrays.asList(Colors.values()));

        inputScreen = new Screen();
        outputScreen = new Screen();

        this.client = client;
        input = new Scanner(System.in);
    }

    private void updateScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+outputScreen.getLayout() + inputScreen.getLayout());
    }

    public void requestLogin() {
        inputScreen.clear();
        requestUsername();
        requestColor();
        client.requestLogin(username, color);
    }

    private void requestUsername() {
        inputScreen.addLine("Enter username: ");
        updateScreen();
        username = input.nextLine();  // Read user input
        inputScreen.removeLastLine();
        inputScreen.addLine("Username selected: ");
        inputScreen.addLine(username);
        updateScreen();
    }

    private void requestColor() {
        inputScreen.addLine("Choose one of the available colors: ");
        String c;
        do {
            updateScreen();
            c = input.nextLine().toUpperCase();
            inputScreen.removeLastLine();
            inputScreen.addLine("The color selected doesn't exist, choose a different one: ");
        } while(!Colors.isValid(c));
        inputScreen.removeLastLine();
        color = Colors.valueOf(c);
        inputScreen.addLine("Color selected :");
        inputScreen.addLine(color.name());
        updateScreen();
    }

    public void requestAction() { // Test
        inputScreen.clear();
        inputScreen.addLine("You are registered!\nType your message:  ");
        updateScreen();
        String content = input.nextLine();  // Read user input
        inputScreen.addLine(content);
        updateScreen();
        // TEST
        client.sendMessage(new ActionMessage(username, content, Action.MOVE, new Position(0, 0), new Worker(new Position(0, 0))));
    }

    public void updateLobby(LobbyUpdate message) {
        updateColors(message.getColors());
        updateLobbyChoices(message.getPlayers());
    }

    public void showMessage(String text) {
        outputScreen.clear();
        inputScreen.clear();
        outputScreen.addLine(text+"\n");
        updateScreen();
    }

    private void updateLobbyChoices(Map<String, Colors> choices) {
        outputScreen.clear();
        outputScreen.addLine("Current players:");
        if(choices.keySet().isEmpty()) { outputScreen.addLine("No one registered yet"); }
        for(String s : choices.keySet()) {
           outputScreen.addLine("Name: "+s+", Color: "+choices.get(s));
        }
        outputScreen.addLine("");
        outputScreen.addLine("Available colors: "+colors+"\n");
        updateScreen();
    }

    private void updateColors(List<Colors> colors) {
        this.colors = colors;
    }

    public void denyLogin(){
        inputScreen.clear();
        inputScreen.addLine("Lobby is full");
        outputScreen.removeLastLine();
        updateScreen();
    }

    public void requestReady() {
        inputScreen.clear();
        inputScreen.addLine("\nType 'Ok' to ready up: ");
        updateScreen();
        String content = input.nextLine();  // Read user input
        inputScreen.addLine(content);
        updateScreen();
        client.sendMessage(new FlagMessage(MessageType.READY,username,content,true));
    }

    public void switchState(GameState state) {
        inputScreen.clear();
        outputScreen.clear();
        outputScreen.addLine("\nGame state is now: "+state+"\n");
        updateScreen();
    }

    /*
    public void requestUnready(){
        inputScreen.clear();
        inputScreen.addLine("\nType 'Ko' to unready: ");
        updateScreen();
        String content = input.nextLine();  // Read user input
        inputScreen.addLine(content);
        updateScreen();
        client.sendMessage(new FlagMessage(MessageType.READY,username,content,false));
    }*/


}

package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.net.client.Client;
import it.polimi.ingsw.net.messages.game.ActionMessage;
import it.polimi.ingsw.net.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.view.View;

import java.util.*;

public class Cli implements View {

    private String username;
    private Colors color;

    private List<Colors> colors;

    private InputScreen inputScreen;
    private OutputScreen outputScreen;

    private Client client;
    private Scanner input;

    public Cli(Client client) {
        colors = new ArrayList<>(Arrays.asList(Colors.values()));

        inputScreen = new InputScreen();
        outputScreen = new OutputScreen();

        this.client = client;
        input = new Scanner(System.in);
    }

    private void updateScreen() {
        System.out.println(outputScreen.getLayout() + inputScreen.getLayout());
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
        inputScreen.addLine("\nType your message: ");
        updateScreen();
        String content = input.nextLine();  // Read user input
        inputScreen.addLine(content);
        updateScreen();
        client.sendMessage(new ActionMessage(username, content, Action.MOVE, new Position(0, 0), new Worker(new Position(0, 0))));
    }

    public void updateLobby(LobbyUpdate message) {
        updateColors(message.getColors());
        updateLobbyChoices(message.getPlayers());
    }

    public void showMessage(String text) {
        System.out.println(text);
    }

    private void updateLobbyChoices(Map<String, Colors> choices) {
        outputScreen.clear();
        for(String s : choices.keySet()) {
           outputScreen.addLine("\nCurrent players:\nName: "+s+", Color: "+choices.get(s));
        }
        outputScreen.addLine("\nAvailable colors: "+colors+"\n");
        updateScreen();
    }

    private void updateColors(List<Colors> colors) {
        this.colors = colors;
    }

    public void denyLogin(){
        inputScreen.clear();
        inputScreen.addLine("\nLobby is full");
        outputScreen.removeLastLine();
        updateScreen();
    }
}

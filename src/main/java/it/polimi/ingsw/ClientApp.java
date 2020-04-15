package it.polimi.ingsw;


import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.exceptions.net.FailedConnectionException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.net.messages.ActionMessage;
import it.polimi.ingsw.net.messages.Message;
import it.polimi.ingsw.net.client.ClientConnection;

import java.util.List;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {
        ClientConnection client = null;
        String username;
        boolean connected = false;

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        do {
            List<Message> queue = null;
            System.out.println("Enter username: ");
            username = scanner.nextLine();  // Read user input
            try {
                client = new ClientConnection(username, "127.0.0.1", 7654);
                System.out.println("Created socket with username " + username);
                boolean replied = false;
                while (!replied) {
                    queue = client.getQueue();
                    replied = !(queue.isEmpty());
                }
                Message msg = queue.get(0);
                connected = (msg.getType() == MessageType.OK);
            } catch (FailedConnectionException e) {
                System.out.println("Client couldn't reach the server, retry when ready");
            }
        } while (!connected);

        System.out.println("Connection successful!");

        while (true) {
            System.out.println("\nType 1 for ACTION, 2 for OK, 3 for KO");
            String content = scanner.nextLine();  // Read user input
            if (content.equals("1")) {
                System.out.println("\nType your ACTION message: ");
                content = scanner.nextLine();  // Read user input
                client.sendMessage(new ActionMessage(username, content, Action.MOVE, new Position(0, 0), new Worker(new Player("test"))));
            } else if (content.equals("2")) {
                System.out.println("\nType your OK message: ");
                content = scanner.nextLine();  // Read user input
                client.sendMessage(new Message(MessageType.OK, username,content));
            } else if (content.equals("3")) {
                System.out.println("\nType your KO message: ");
                content = scanner.nextLine();  // Read user input
                client.sendMessage(new Message(MessageType.KO, username,content));
            } else {
                System.out.println("That's not 1, 2 or 3!");
            }
        }
    }
}

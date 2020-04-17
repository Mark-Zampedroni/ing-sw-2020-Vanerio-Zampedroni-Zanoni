package it.polimi.ingsw;


import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.net.client.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientApp {

    public static List<Colors> colors = new ArrayList<>(Arrays.asList(Colors.values()));

    public static void main(String[] args) {
        new Client("127.0.0.1", 7654,0);
    }
}

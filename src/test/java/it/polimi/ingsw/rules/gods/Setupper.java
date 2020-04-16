package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;

public class Setupper {

    public static Player addPlayer(String username, Colors color, int number) {
        Player player = new Player(username,color);
        player.addWorker(new Position(-number,0));
        player.addWorker(new Position(0,-number));
        Session.getInstance().addPlayer(player);
        return player;
    }

    public static void removePlayer(Player player) {
        Session.getInstance().removePlayer(player);
    }

}

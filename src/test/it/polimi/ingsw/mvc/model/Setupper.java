package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;

public class Setupper {

    //Adds a player to the session
    public static Player addPlayer(String username, Colors color, int number) {
        Player player = new Player(username,color);
        player.addWorker(new Position(-number,0));
        player.addWorker(new Position(0,-number));
        Session.getInstance().addPlayer(player);
        return player;
    }

    //Removes a player from the session
    public static void removePlayer(Player player) {
        Session.getInstance().removePlayer(player);
    }

}

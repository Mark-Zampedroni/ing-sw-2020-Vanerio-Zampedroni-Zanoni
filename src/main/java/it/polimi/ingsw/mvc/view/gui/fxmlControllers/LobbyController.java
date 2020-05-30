package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Colors;

import java.util.List;

public class LobbyController extends GenericController {

    public void initialize() {
        super.initialize(this);
        System.out.println("yey");
    }

    public void showLobby(List<Colors> availableColors) {
        System.out.println("LOBBY !");
    }

}

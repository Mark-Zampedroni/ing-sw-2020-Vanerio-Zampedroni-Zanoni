package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class GodSelectionController extends GenericController {


    /*

    -Border
        Grid 1x1
            Grid 10x16
                Label(9-1)
                Label(9-4)
                Label(9-14)
            Grid 1x6
                Border(0-4)
            Border
                Grid 6x22
                    Border(0-15)



     */

    private class GodCard extends BorderPane {

    }

    @FXML
    Button info;

    public void initialize() {
        super.initialize(this);

        info.setOnMouseEntered(event -> info.setId("infoPressed"));

        info.setOnMouseExited(event -> info.setId("infoButton"));

    }

    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        // update da view quando è il turno di un altro client
    }

    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        // update da view quando tocca a questo client
    }

    public void updateStarterPlayerSelection(Map<String, String> choices) {
        // update da view a tutti i giocatori notificando che il challenger sta scegliendo lo starter player
    }

    public void requestStarterPlayer(Map<String, String> choices) {
        // update da view al challenger avvertendolo che deve scegliere lo starter player
    }

}

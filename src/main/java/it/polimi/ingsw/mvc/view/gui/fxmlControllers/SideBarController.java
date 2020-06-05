package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class SideBarController extends GenericController {

    @FXML
    Label powerLabel;

    private void initialize () {
        powerLabel.setOnMouseEntered(event -> {
            try {
                handlePowerLabelEnter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        powerLabel.setOnMouseExited(event -> handlePowerLabelExited());
    }

    private void handlePowerLabelExited() {
    }

    private void handlePowerLabelEnter() throws IOException {
        System.out.println("PopupPower");
        Popup popup = new Popup();
    }


}

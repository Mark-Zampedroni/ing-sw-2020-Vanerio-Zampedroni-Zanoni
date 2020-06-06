package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DisconnectionController extends GenericController {

    @FXML
    Label reason;
    @FXML
    Button okButton;

    public void initialize() {
        super.initialize(this);
        initButton();
    }

    public void initButton() {
        okButton.setOnMouseClicked(event -> okButton.setId("buttonPressed"));
        okButton.setOnMouseReleased(event -> {
            okButton.setId("buttonReleased");
            gui.resetClient();
            Platform.runLater(() -> GuiManager.setLayout(this.getScene(), GuiManager.getFxmlPath(TitleController.class)));
        });
    }

    public void showDisconnected(String info) {
        reason.setText(info);
    }



}

package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

import it.polimi.ingsw.mvc.view.gui.GuiController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Disconnection window (shown only if interrupted forcibly) FXML controller
 */
public class DisconnectionController extends GenericController {

    @FXML
    Label reason;
    @FXML
    Label fixedLabel;
    @FXML
    Button okButton;

    /**
     * Initializes the elements of the scene and sets the rescaling of the fonts
     */
    public void initialize() {
        super.initialize(this);
        Platform.runLater(this::initButton);
        setFontRatio(fixedLabel);
        setFontRatio(reason);
        setFontRatio(okButton);
    }

    /**
     * Sets the events of the only button
     */
    private void initButton() {
        okButton.setOnMousePressed(event -> okButton.setId("buttonPressed"));
        okButton.setOnMouseReleased(event -> {
            okButton.setId("buttonReleased");
            Platform.runLater(() -> GuiController.setLayout(this.getScene(), GuiController.getFxmlPath(TitleController.class)));
        });
    }

    /**
     * Displays a message concerning the reason of the disconnection
     *
     * @param info shown message
     */
    public void showDisconnected(String info) {
        reason.setText(info);
    }

}

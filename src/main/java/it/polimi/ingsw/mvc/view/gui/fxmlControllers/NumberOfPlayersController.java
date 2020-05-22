package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class NumberOfPlayersController {
    private GuiManager gui;
    ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    public Pane mainPane;
    @FXML
    public Button confirmButton;
    @FXML
    public RadioButton twoButton;
    @FXML
    public RadioButton threeButton;


    public void initialize() {
        gui = GuiManager.getInstance();
        twoButton.setToggleGroup(toggleGroup);
        threeButton.setToggleGroup(toggleGroup);

    }

    public void handleClick() throws IOException {
        if (twoButton.isSelected() || threeButton.isSelected()) {
            GuiManager.setLayout(mainPane.getScene(), "/fxmlFiles/Lobby.fxml");
        } else {
            GuiManager.showDialog((Stage) mainPane.getScene().getWindow(),"Error", "Field can't be empty");
        }
    }
}


package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
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

    @FXML
    public void handleClick() throws IOException {
        if (twoButton.isSelected() || threeButton.isSelected()) {
            GuiManager.setLayout(mainPane.getScene(), "/fxmlFiles/LobbySelectionName.fxml");
            if (twoButton.isSelected()) {GuiManager.getInstance().validateNumberOfPlayers("2");}
            else {GuiManager.getInstance().validateNumberOfPlayers("3");}
        } else {
            GuiManager.showDialog((Stage) mainPane.getScene().getWindow(),"Error", "Field can't be empty");
        }
    }

    @FXML
    public void glowButton(){
        confirmButton.setEffect(new Glow(0.4));
        //confirmButton.setImage(new Image("/Texture2D_sorted/Pulsanti/button-play-down.png"));
    }

    @FXML
    public void switchOffGlow(){
        confirmButton.setEffect(new Glow(0));
        //playButtonImageView.setImage(new Image("/Texture2D_sorted/Pulsanti/button-play-normal.png"));
    }
}


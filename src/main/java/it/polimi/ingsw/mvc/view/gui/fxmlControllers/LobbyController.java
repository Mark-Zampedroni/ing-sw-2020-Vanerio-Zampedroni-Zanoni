package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LobbyController extends GenericController {

    @FXML
    public Button confirmButton; //id confirmButtonBg

    @FXML
    public TextField nameTextField; //id textBack

    @FXML
    public ToggleButton blueButton; //id colorBackground

    @FXML
    public ToggleButton whiteButton; //id colorBackground

    @FXML
    public ToggleButton brownButton; //id colorBackground

    @FXML
    public BorderPane colorPlayerOne; //id head

    @FXML
    public BorderPane colorPlayerTwo; //id head

    @FXML
    public Label playerNameOne;

    @FXML
    public Label playerNameTwo;

    ToggleGroup toggleGroup;
    Colors color;



    public void initialize() {
        super.initialize(this);
        nameTextField.setStyle("-fx-text-inner-color: white; -fx-background-color: transparent;");
        if(gui.getNumber()==2) {
            hideNode(playerNameTwo);
            hideNode(colorPlayerTwo);
        }
        blueButton.setToggleGroup(toggleGroup);
        brownButton.setToggleGroup(toggleGroup);
        whiteButton.setToggleGroup(toggleGroup);
        initColorButton(blueButton);
        initColorButton(whiteButton);
        initColorButton(brownButton);
        initConfirmButton(confirmButton);

        //playerNameOne.setText("    Sandro");
        //colorPlayerOne.setId("colorBrown");

        if (gui.getPlayers().size()==1) {
            playerNameOne.setText("    "+(String)GuiManager.getInstance().getPlayers().values().toArray()[0]);
            if (((Colors)gui.getPlayers().values().toArray()[0]).equals(Colors.BROWN)) {
            colorPlayerOne.setId("colorBrown");}
            if (((Colors)gui.getPlayers().values().toArray()[0]).equals(Colors.WHITE)) {
                colorPlayerOne.setId("colorWhite");}
            if (((Colors)gui.getPlayers().values().toArray()[0]).equals(Colors.BLUE)) {
                colorPlayerOne.setId("colorBlue");}
        }
        if (gui.getPlayers().size()==2) {
            playerNameTwo.setText("    "+(String)GuiManager.getInstance().getPlayers().values().toArray()[1]);
            if (((Colors)gui.getPlayers().values().toArray()[1]).equals(Colors.BROWN)) {
                colorPlayerTwo.setId("colorBrown");}
            if (((Colors)gui.getPlayers().values().toArray()[1]).equals(Colors.WHITE)) {
                colorPlayerTwo.setId("colorWhite");}
            if (((Colors)gui.getPlayers().values().toArray()[1]).equals(Colors.BLUE)) {
                colorPlayerTwo.setId("colorBlue");}
        }
    }

    private void initConfirmButton(Button button) {
        button.setOnMousePressed(event -> handleButtonPressed(button));
        button.setOnMouseReleased(event -> {
            try {
                handleButtonReleased(button);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }

    private void handleButtonReleased(Button button) throws IOException {
        button.setId("buttonReleased");
        if(!gui.validateUsername(nameTextField.getText()) || ((color!=Colors.BROWN) && (color!=Colors.BLUE) && (color!=Colors.WHITE)) ) {
            GuiManager.showDialog((Stage) main.getScene().getWindow(), "Input Error!", "You must Insert a valid username and color! ");
        } else { gui.requestLogin(nameTextField.getText(), color);
                 gui.requestLogin();}
    }

    private void initColorButton(ToggleButton button) {
        button.setOnMousePressed(event -> handleButtonPressed(button));
        button.setOnMouseReleased(event -> handleButtonReleased(button));
    }

    private void handleButtonPressed(ToggleButton button) {
        button.setId("buttonPressedColor");
        blueButton.setEffect(new Glow(0));
        brownButton.setEffect(new Glow(0));
        whiteButton.setEffect(new Glow(0));
    }

    private void handleButtonReleased(ToggleButton button) {
        button.setId("buttonReleasedColor");
        if (button.equals(blueButton)) { color=Colors.BLUE;
            blueButton.setEffect(new Glow(0.3));}
        if (button.equals(brownButton)) { color=Colors.BROWN;
            brownButton.setEffect(new Glow(0.3));}
        if (button.equals(whiteButton)) { color=Colors.WHITE;
            whiteButton.setEffect(new Glow(0.3));}
    }

    public void showLobby(List<Colors> availableColors) {
        System.out.println("LOBBY !");
        if (!availableColors.contains(Colors.BLUE)) {
            blueButton.setDisable(true);
        }
        if (!availableColors.contains(Colors.BROWN)) {
            brownButton.setDisable(true);
        }
        if (!availableColors.contains(Colors.WHITE)) {
            whiteButton.setDisable(true);
        }
    }

}

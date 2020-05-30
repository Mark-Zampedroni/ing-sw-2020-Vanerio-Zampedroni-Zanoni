package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @FXML
    public Label infoLabel;

    ToggleGroup toggleGroup;

    Colors color;

    Map<Colors,ToggleButton> buttons;

    public void initialize() {
        super.initialize(this);
        buttons = new HashMap<>();
        initButtons(buttons);
        hideNode(playerNameTwo);
        hideNode(colorPlayerTwo);
        initConfirmButton(confirmButton);
    }

    private void initButtons(Map<Colors,ToggleButton> buttons) {
        buttons.put(Colors.BLUE,blueButton);
        buttons.put(Colors.WHITE,whiteButton);
        buttons.put(Colors.BROWN,brownButton);
        buttons.values().forEach(this::initColorButton);
        buttons.values().forEach(b -> b.setToggleGroup(toggleGroup));
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
        button.setDisable(true);
    }

    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }

    private void handleButtonReleased(Button button) throws IOException {
        button.setId("buttonReleased");
        if(!gui.validateUsername(nameTextField.getText()) || !gui.validateColor(color.toString()) ) {
            GuiManager.showDialog((Stage) main.getScene().getWindow(), "Input Error!", "You must Insert a valid username and color! ");
        } else {
            gui.requestLogin(nameTextField.getText(), color);
        }
    }

    private void initColorButton(ToggleButton button) {
        button.setOnMousePressed(event -> {
            color = buttons.keySet().stream().filter(key -> buttons.get(key) == button).findFirst().get();
            confirmButton.setDisable(false);
            button.setId("buttonPressedColor");
            button.setEffect(new Glow(0.3));
            buttons.values().stream().filter(b -> b != button).forEach(b -> {
                b.setId("buttonReleasedColor");
                b.setEffect(new Glow(0));
            });
        });
    }

    public void showLobby(List<Colors> availableColors) {
        buttons.keySet().forEach(c -> buttons.get(c).setDisable(!availableColors.contains(c)));
        updatePlayers();
    }

    private void updatePlayers() {
        if(Integer.parseInt(gui.getNumberOfPlayers()) > 0) { addPlayer(playerNameOne,colorPlayerOne,0); }
        if(Integer.parseInt(gui.getNumberOfPlayers()) > 1) { addPlayer(playerNameTwo,colorPlayerTwo,1); }
        if(gui.getPlayers().containsKey(gui.getUsername())) {
            hideNode(confirmButton);
            nameTextField.setDisable(true);
            infoLabel.setText("Wait for other players to log ...");
        }
    }

    private void addPlayer(Label name, BorderPane color, int number) {
        String playerName = (String) gui.getPlayers().keySet().toArray()[number];
        String playerColor = gui.getPlayers().get(playerName).toString().toLowerCase();
        name.setText(playerName);
        color.setId("color" + playerColor.substring(0, 1).toUpperCase() + playerColor.substring(1));
        showNode(name);
        showNode(color);
    }

}

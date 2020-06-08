package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
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
        initFonts();
    }

    private void initFonts() {
        setFontRatio(nameTextField);
        setFontRatio(confirmButton);
        setFontRatio(playerNameOne);
        setFontRatio(playerNameTwo);
        setFontRatio(infoLabel);
        buttons.values().forEach(this::setFontRatio);
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
        button.setOnMouseReleased(event -> handleButtonReleased(button));
        button.setDisable(true);
    }

    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }

    private void handleButtonReleased(Button button) {
        button.setId("buttonReleased");
        if(gui.validateUsername(nameTextField.getText()) || gui.validateColor(color.toString())) {
            infoLabel.setText("Insert a valid username and color!");
        } else {
            gui.requestLogin(nameTextField.getText(), color);
        }
    }

    private void initColorButton(ToggleButton button) {
        button.setOnMousePressed(event -> {
            color = buttons.keySet().stream().filter(key -> buttons.get(key) == button).findFirst().orElse(Colors.WHITE);
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
        handlePlayerSlot(playerNameOne,colorPlayerOne,0, Integer.parseInt(gui.getNumberOfPlayers()) > 0);
        handlePlayerSlot(playerNameTwo,colorPlayerTwo,1, Integer.parseInt(gui.getNumberOfPlayers()) > 1);

        if(gui.getPlayers().containsKey(gui.getUsername())) {
            hideNode(confirmButton);
            nameTextField.setDisable(true);
            infoLabel.setText("Wait for other players to log ...");
        }
    }

    private void handlePlayerSlot(Label name, BorderPane color, int number, boolean hasPlayer) {
        if(hasPlayer) {
            addPlayer(name,color,number);
        }
        else {
            hideSlot(name,color,number);
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

    private void hideSlot(Label name, BorderPane color, int number) {
        name.setText((number == 0) ? "Waiting..." : "");
        color.setId((number == 0) ? "head" : "");
        if(number != 0) {
            hideNode(name);
            hideNode(color);
        }
    }

}

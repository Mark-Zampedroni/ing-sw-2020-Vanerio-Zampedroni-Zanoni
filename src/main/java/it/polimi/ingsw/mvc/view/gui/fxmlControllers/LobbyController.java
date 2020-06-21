package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
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
    public BorderPane blueButton; //id colorBackground
    @FXML
    public BorderPane brownButton; //id colorBackground
    @FXML
    public BorderPane whiteButton; //id colorBackground
    @FXML
    public BorderPane colorPlayerOne; //id head
    @FXML
    public BorderPane colorPlayerTwo; //id head
    @FXML
    public Label playerNameOne;
    @FXML
    public Label playerNameTwo;
    @FXML
    public AnchorPane borderPlayerTwo;
    @FXML
    public AnchorPane borderPlayerOne;
    @FXML
    public Label infoLabel;


    Colors color;

    Map<Colors, BorderPane> buttons;

    public void initialize() {
        super.initialize(this);
        buttons = new HashMap<>();
        initButtons(buttons);
        hideNode(playerNameTwo);
        hideNode(colorPlayerTwo);
        hideNode(borderPlayerTwo);
        initConfirmButton(confirmButton);
        initFonts();
    }

    private void initFonts() {
        setFontRatio(nameTextField);
        setFontRatio(confirmButton);
        setFontRatio(playerNameOne);
        setFontRatio(playerNameTwo);
        setFontRatio(infoLabel);
        buttons.values().forEach(b -> setFontRatio((Control) b.getChildren().get(0)));
    }

    private void initButtons(Map<Colors, BorderPane> buttons) {
        buttons.put(Colors.BLUE, blueButton);
        buttons.put(Colors.WHITE, whiteButton);
        buttons.put(Colors.BROWN, brownButton);
        buttons.values().forEach(this::initColorButton);
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
        button.setId("confirmButtonBg");
        if (gui.validateUsername(nameTextField.getText()) || gui.validateColor(color.toString())) {
            infoLabel.setText("Insert a valid username and color!");
        } else {
            gui.requestLogin(nameTextField.getText(), color);
        }
    }

    private void initColorButton(BorderPane button) {
        button.setOnMousePressed(event -> {
            color = buttons.keySet().stream().filter(key -> buttons.get(key) == button).findFirst().orElse(Colors.WHITE);
            confirmButton.setDisable(false);
            button.setId("colorpressed" + color);
            button.setEffect(new Glow(0.6));
            buttons.values().stream().filter(b -> b != button).forEach(b -> {
                b.setId("color" + buttons.keySet().stream().filter(c -> buttons.get(c) == b).findFirst().orElse(Colors.WHITE));
                b.setEffect(new Glow(0));
            });
        });
    }

    public void showLobby(List<Colors> availableColors) {
        buttons.keySet().forEach(c -> setDisableButton(buttons.get(c), !availableColors.contains(c)));
        updatePlayers();
    }

    private void updatePlayers() {
        handlePlayerSlot(playerNameOne, colorPlayerOne, 0, Integer.parseInt(gui.getNumberOfPlayers()) > 0, borderPlayerOne);
        handlePlayerSlot(playerNameTwo, colorPlayerTwo, 1, Integer.parseInt(gui.getNumberOfPlayers()) > 1, borderPlayerTwo);

        if (gui.getPlayers().containsKey(gui.getUsername())) {
            hideNode(confirmButton);
            nameTextField.setDisable(true);
            infoLabel.setText("Wait for other players to log ...");
            buttons.keySet().forEach(c -> setDisableButton(buttons.get(c), true));
        }
    }

    private void setDisableButton(Node node, boolean v) {
        node.setDisable(v);
        node.setOpacity((v) ? 0.5 : 1);
    }

    private void handlePlayerSlot(Label name, BorderPane color, int number, boolean hasPlayer, AnchorPane labelBg) {
        if (hasPlayer) {
            addPlayer(name, color, number, labelBg);
        } else {
            hideSlot(name, color, number, labelBg);
        }
    }

    private void addPlayer(Label name, BorderPane color, int number, AnchorPane labelbg) {
        String playerName = (String) gui.getPlayers().keySet().toArray()[number];
        String playerColor = gui.getPlayers().get(playerName).toString().toLowerCase();
        name.setText(playerName);
        color.setId("color" + playerColor.substring(0, 1).toUpperCase() + playerColor.substring(1));
        showNode(name);
        showNode(color);
        showNode(labelbg);
    }

    private void hideSlot(Label name, BorderPane color, int number, AnchorPane labelbg) {
        name.setText((number == 0) ? "Waiting..." : "");
        color.setId((number == 0) ? "head" : "");
        if (number != 0) {
            hideNode(name);
            hideNode(color);
            hideNode(labelbg);
        }
    }

}

package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;


public class TitleController extends GenericController {
    private static String connectionIp;
    private static int connectionPort;

    @FXML
    public Button playButton;
    @FXML
    public Button p2;
    @FXML
    public Button p3;
    @FXML
    public BorderPane textPane;
    @FXML
    public Label textLabel;
    @FXML
    public Button fullScreenButton;
    @FXML
    public Button okButton;
    @FXML
    public BorderPane textPaneCon;
    @FXML
    public Label textLabelCon;

    public static void setConnectionConfig(String ip, int port) {
        connectionPort = port;
        connectionIp = ip;
    }

    public void initialize() {
        super.initialize(this);
        initFonts();
        initButton(p2);
        initButton(p3);
        initButton(okButton);
        hideNode(textPane);
        hideNode(textPaneCon);
        //fullScreenButton.setOnMouseClicked(event -> GuiManager.getInstance().getStage().setFullScreen(true));
    }

    private void initFonts() {
        setFontRatio(textLabelCon);
        setFontRatio(textLabel); //<--- TEST
        setFontRatio(p2); //<--- TEST
        setFontRatio(p3); //<--- TEST
        setFontRatio(okButton);
    }

    @FXML
    private void handleClickPlay() {
        if (!gui.createConnection(connectionIp, connectionPort)) {
            showDisconnected("Connection Error! \nConnection failed! The server is unreachable, try again");
        }
    }

    @FXML
    private void handleStartEntered() {
        playButton.setEffect(new Glow(0.4));
        playButton.setId("pressedStart");
    }

    @FXML
    private void handleStartExited() {
        playButton.setEffect(new Glow(0.0));
        playButton.setId("releasedStart");
    }

    private void initButton(Button button) {
        button.setOnMousePressed(event -> handleButtonPressed(button));
        button.setOnMouseReleased(event -> handleButtonReleased(button));
        hideNode(button);
    }

    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }

    private void handleButtonReleased(Button button) {
        button.setId("buttonReleased");
        if (button == okButton) {
            hideNode(textPaneCon);
            hideNode(okButton);
            showNode(playButton);
        } else {
            gui.validateNumberOfPlayers(Arrays.asList(button.getText().split(" ")).get(0));
        }
    }

    public void requestNumberOfPlayers() {
        switchMode();
        showNode(p2);
        showNode(p3);
    }

    public void showInfo(String text) {
        switchModeConn(text);
    }

    public void showDisconnected(String text) {
        switchModeConn(text);
        showNode(okButton);
    }

    private void switchModeConn(String text) {
        hideNode(playButton);
        hideNode(p2);
        hideNode(p3);
        hideNode(textPane);
        showNode(textPaneCon);
        textLabelCon.setText(text);
    }

    private void switchMode() {
        hideNode(playButton);
        showNode(textPane);
        textLabel.setText("Choose the number of players!");
    }

}


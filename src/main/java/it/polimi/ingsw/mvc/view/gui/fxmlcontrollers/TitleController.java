package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;

/**
 * Starting (title) screen FXML controller
 */
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
    public Button okButton;
    @FXML
    public BorderPane textPaneCon;
    @FXML
    public Label textLabelCon;

    public static void setConnectionConfig(String ip, int port) {
        connectionPort = port;
        connectionIp = ip;
    }

    /**
     * Initializes the scene
     */
    public void initialize() {
        super.initialize(this);
        initFonts();
        initButton(p2);
        initButton(p3);
        initButton(okButton);
        hideNode(textPane);
        hideNode(textPaneCon);
    }

    /**
     * Initializes the fonts scaling
     */
    private void initFonts() {
        setFontRatio(textLabelCon);
        setFontRatio(textLabel);
        setFontRatio(p2);
        setFontRatio(p3);
        setFontRatio(okButton);
    }

    /**
     * Starts a connection as a {@link it.polimi.ingsw.network.client.Client client} or displays an error message
     */
    @FXML
    private void handleClickPlay() {
        if (!gui.createConnection(connectionIp, connectionPort)) {
            showDisconnected("Connection Error! \nConnection failed! The server is unreachable, try again");
        }
    }

    /**
     * Applies a glowing effect on the button that allows a player to enter the game
     */
    @FXML
    private void handleStartEntered() {
        playButton.setEffect(new Glow(0.4));
        playButton.setId("pressedStart");
    }

    /**
     * Removes the glowing effect applied on the button that allows a player to enter the game
     */
    @FXML
    private void handleStartExited() {
        playButton.setEffect(new Glow(0.0));
        playButton.setId("releasedStart");
    }

    /**
     * Defines the methods called by the button after being clicked. Hides the button until release
     *
     * @param button targeted button
     */
    private void initButton(Button button) {
        button.setOnMousePressed(event -> handleButtonPressed(button));
        button.setOnMouseReleased(event -> handleButtonReleased(button));
        hideNode(button);
    }


    /**
     * Handles the event of pressing a button
     *
     * @param button targeted button
     */
    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }


    /**
     * Handles the event of releasing a button
     *
     * @param button targeted button
     */
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

    /**
     * Allows a player to choose the number of players that can join that game
     */
    public void requestNumberOfPlayers() {
        switchMode();
        showNode(p2);
        showNode(p3);
    }


    /**
     * Displays a message
     *
     * @param text text that's is going to be shown
     */
    public void showInfo(String text) {
        switchModeConn(text);
    }


    /**
     * Displays a message regarding a disconnection
     *
     * @param text text that's is going to be shown
     */
    public void showDisconnected(String text) {
        switchModeConn(text);
        showNode(okButton);
    }


    /**
     * Hides all the buttons and displays a message
     *
     * @param text text that's is going to be shown
     */
    private void switchModeConn(String text) {
        hideNode(playButton);
        hideNode(p2);
        hideNode(p3);
        hideNode(textPane);
        showNode(textPaneCon);
        textLabelCon.setText(text);
    }


    /**
     * Displays a message regarding the number of player in the game
     *
     */
    private void switchMode() {
        hideNode(playButton);
        showNode(textPane);
        textLabel.setText("Choose the number of players!");
    }

}


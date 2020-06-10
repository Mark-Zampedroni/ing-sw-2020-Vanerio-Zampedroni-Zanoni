package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.mvc.view.gui.music.Music;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;


public class TitleController extends GenericController {
    private static String connectionIp;
    private static int connectionPort;
    private static boolean music;

    @FXML
    public Button playButton;
    @FXML
    public Button musicButton;
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


    public void initialize() {
        super.initialize(this);
        initFonts();
        initChoiceButton(p2);
        initChoiceButton(p3);
        hideNode(textPane);

        //fullScreenButton.setOnMouseClicked(event -> GuiManager.getInstance().getStage().setFullScreen(true));
    }

    private void initFonts() {
        setFontRatio(textLabel); //<--- TEST
        setFontRatio(p2); //<--- TEST
        setFontRatio(p3); //<--- TEST
    }

    @FXML
    private void handleClickPlay() throws IOException {
        if(!gui.createConnection(connectionIp, connectionPort)){
            GuiManager.showDialog((Stage) main.getScene().getWindow(), "Connection Error", "Connection failed! The server is unreachable, try again");
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

    private void initChoiceButton(Button button) {
        button.setOnMousePressed(event -> handleButtonPressed(button));
        button.setOnMouseReleased(event -> handleButtonReleased(button));
        hideNode(button);
    }

    private void handleButtonPressed(Button button) {
        button.setId("buttonPressed");
    }

    private void handleButtonReleased(Button button) {
        button.setId("buttonReleased");
        gui.validateNumberOfPlayers(Arrays.asList(button.getText().split(" ")).get(0));
    }

    @FXML
    public void handleMusic() {
        if (!music) {
            Music.playMusic();
            musicButton.setId("musicOn");
            music = true;
        }
        else {
            Music.turnOffMusic();
            music = false;
            musicButton.setId("musicOff");
        }
    }

    public void requestNumberOfPlayers() {
        hideNode(playButton);
        showNode(textPane);
        showNode(p2);
        showNode(p3);
        textLabel.setText("Choose the number of players!");
    }

    public void showInfo(String text) {
        hideNode(playButton);
        showNode(textPane);
        textLabel.setText(text);
    }

    public static void setConnectionConfig(String ip, int port){
        connectionPort = port;
        connectionIp = ip;
    }

}


package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.mvc.view.gui.music.Music;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class TitleController {
    private GuiManager gui;
    private static String connectionIp;
    private static int connectionPort;


        @FXML
        public Pane mainPane;
        @FXML
        public Button playButton;
        @FXML
        public ImageView playButtonImageView;
        @FXML
        public Button menuButton;
        @FXML
        public ImageView menuButtonImageView;

        double level=0;

    public void initialize() {
        gui = GuiManager.getInstance();

    }

        @FXML
        public void handleClickPlay() throws IOException {
        if(!gui.createConnection(connectionIp, connectionPort)){
            GuiManager.showDialog((Stage) mainPane.getScene().getWindow(), "Connection Error", "Connection failed! The server is unreachable, try again");
        }
        else{
            GuiManager.setLayout(mainPane.getScene(), "/fxmlFiles/NumberOfPlayers.fxml"); }
        }

        @FXML
        public void glowButton(){
           playButtonImageView.setEffect(new Glow(0.4));
           playButtonImageView.setImage(new Image("/Texture2D_sorted/Pulsanti/button-play-down.png"));
        }

        @FXML
        public void switchOffGlow(){
            playButtonImageView.setEffect(new Glow(0));
            playButtonImageView.setImage(new Image("/Texture2D_sorted/Pulsanti/button-play-normal.png"));
        }

        @FXML
        public void changeButton(){

        }

        @FXML
        public void changeButtonMenu(){
            menuButtonImageView.setImage(new Image("/Texture2D_sorted/Pulsanti/menu_button_pressed.png"));
        }

        @FXML
        public void restoreMenuButton(){
            menuButtonImageView.setImage(new Image("/Texture2D_sorted/Pulsanti/menu_button.png"));
        }

        @FXML
        public void music(){
            Music.playMusic();
        }

    public static void setConnectionConfig(String ip, int port){
        connectionPort = port;
        connectionIp = ip;
    }
}


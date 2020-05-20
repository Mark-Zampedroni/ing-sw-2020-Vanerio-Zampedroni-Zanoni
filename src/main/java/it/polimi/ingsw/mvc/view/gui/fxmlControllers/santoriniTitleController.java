package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

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


public class santoriniTitleController extends Application{

        @FXML
         public Button playButton;
        @FXML
         public ImageView playButtonImageView;
        @FXML
        public Button menuButton;
        @FXML
        public ImageView menuButtonImageView;

        double level=0;


        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlFiles/01_Santorini_title.fxml"));
            AnchorPane anchorPane= (AnchorPane) root.getChildrenUnmodifiable().get(0);
            Pane pane = (Pane) anchorPane.getChildren().get(0);

            primaryStage.setTitle("Santorini Game");
            Scene scene = new Scene(root, 1040, 700);

            primaryStage.setScene(scene);
            primaryStage.show();


        }

        @FXML
        public void handleClickPlay(){
            PopUp.show();
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


}


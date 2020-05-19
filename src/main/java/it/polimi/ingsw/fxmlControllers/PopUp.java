package it.polimi.ingsw.fxmlControllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class PopUp {


    public static void show(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("We are not ready!");
        window.setMinWidth(300);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText("Server is offline! Try later!");
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label);
        vbox.setAlignment(Pos.CENTER);
        Scene scene =new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}

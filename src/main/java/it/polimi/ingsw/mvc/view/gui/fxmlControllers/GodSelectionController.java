package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GodSelectionController {
    private static int counter;
    private GuiManager gui;

    @FXML
    public Pane mainPane;
    @FXML
    public BorderPane selectButton;
    @FXML
    public Label godNameTitle;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label conditionLabel;
    @FXML
    public static BorderPane firstGod;
    @FXML
    public static BorderPane secondGod;
    @FXML
    public static BorderPane thirdGod;


    private String god;



    public void initialize() {
        gui = GuiManager.getInstance();
        displayGods();
    }


    @FXML
    public void handleSelectButton() throws IOException {
        if(!gui.validatePlayerGodChoice(god)){
            GuiManager.showDialog((Stage) mainPane.getScene().getWindow(),"Error", "God can't be selected");
        }
    }

    public static void displayGods(){

    }

    public static void challengerChoice(List<String> chosenGods){
       switch (counter) {
           case 0:
               firstGod.getStyleClass().add(chosenGods.get(counter));
               break;
           case 1:
               secondGod.getStyleClass().add(chosenGods.get(counter));
               break;
           case 2:
               thirdGod.getStyleClass().add(chosenGods.get(counter));
               break;
       }
       counter++;
       //Con una lista di border pane meglio. me disable!
    }

    @FXML
    public void handleGodSelection(BorderPane godPane){
        god = godPane.getId();
        displayDescription(god);

    }

    private void displayDescription(String godName){
        godNameTitle.setText(godName);
        String[] temp = Gods.valueOf(godName).getDescription().split(":");
        conditionLabel.setText(temp[0]);
        descriptionLabel.setText(temp[1]);
    }

    private void changeBorder(BorderPane godPane){

    }

    /*
    #ATLAS{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/ATLAS.png");
    -fx-background-size: 100% 100%;

}

#HEPHAESTUS{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/HEPHAESTUS.png");
    -fx-background-size: 100% 100%;

}

#MINOTAUR{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/MINOTAUR.png");
    -fx-background-size: 100% 100%;

}

#ARTEMIS{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/ARTEMIS.png");
    -fx-background-size: 100% 100%;

}

#PAN{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/PAN.png");
    -fx-background-size: 100% 100%;

}

#PROMETHEUS{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/PROMETHEUS.png");
    -fx-background-size: 100% 100%;

}

#POSEIDON{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/POSEIDON.png");
    -fx-background-size: 100% 100%;

}

#HERA{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/HERA.png");
    -fx-background-size: 100% 100%;

}

#TRITON{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/TRITON.png");
    -fx-background-size: 100% 100%;

}

#HESTIA{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/HESTIA.png");
    -fx-background-size: 100% 100%;

}

#ZEUS{
    -fx-background-image: url("../Texture2D_sorted/Icone_dei/FullBody_con_sfondo/ZEUS.png");
    -fx-background-size: 100% 100%;

}
     */
}

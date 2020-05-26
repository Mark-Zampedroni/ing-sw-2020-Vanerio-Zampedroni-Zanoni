package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GodSelectionController {
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
    public BorderPane god1;
    @FXML
    public BorderPane god2;
    @FXML
    public BorderPane god13;

    private String god;



    public void initialize() {
        gui = GuiManager.getInstance();
       // displayGods();

    }


    @FXML
    public void handleSelectButton() throws IOException {
        if(!gui.validatePlayerGodChoice(god)){
            GuiManager.showDialog((Stage) mainPane.getScene().getWindow(),"Error", "God can't be selected");
        }
    }

   // public static void displayGods(gods){

   // }

    @FXML
    public void handleGodSelection(BorderPane godPane){
        god = godPane.getId();
        displayDescription(god);
    }

    private void displayDescription(String godName){
        godNameTitle.setText(godName);
        Gods.valueOf(godName).getDescription();

    }


}

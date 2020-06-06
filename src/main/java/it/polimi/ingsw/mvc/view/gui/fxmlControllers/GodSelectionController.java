package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class GodSelectionController extends GenericController {



    private class GodCard extends BorderPane {
        private BorderPane god;
        private Button actionButton;
        private GridPane firstGrid;
        private GridPane initGrid;
        private GridPane infoPowerGrid;

        public GodCard(String godName) {
            initInit();
            initDescription(godName);
            initGodPane(godName);
            initInfoGodPower(godName.toLowerCase()+"power");
            initSelectButton();
            this.setCenter(firstGrid);
        }

        private void initInit(){
            BorderPane borderTemp = new BorderPane();
            firstGrid = createGrid(1, 6);
            borderTemp.setPrefSize(100,100);
            firstGrid.add(borderTemp, 0,0, 4, 0);
            initGrid = createGrid(1,1);
            initGrid.getStyleClass().add("fullbackground");
            initGrid.setId("cardBackground");
            borderTemp.setCenter(initGrid);
        }

        private void initDescription(String god){
            Label godNameLabel = new Label();
            Label descriptionLabel = new Label();
            Label conditionLabel = new Label();

            GridPane gridPreDescription = createGrid(1,1);
            gridPreDescription.setPadding(new Insets(0,20,20,15));
            initGrid.add(gridPreDescription,0,0);
            GridPane gridLabel = createGrid(10,16);
            gridPreDescription.add(gridLabel,0,0);
            godNameLabel.setText(god);
            godNameLabel.getStyleClass().add("fullbackground");
            godNameLabel.setId("godNameLabel");
            godNameLabel.setAlignment(Pos.CENTER);
            String[] temp = Gods.valueOf(god).getDescription().split(":");
            conditionLabel.setText("Effect \n("+temp[0]+")");
            conditionLabel.setAlignment(Pos.CENTER);
            descriptionLabel.setText(temp[1]);
            descriptionLabel.setAlignment(Pos.TOP_LEFT);
            gridLabel.add(godNameLabel,0,0,1,9);
            gridLabel.add(descriptionLabel,2,0,4,9);
            gridLabel.add(conditionLabel,5,0,13,9);

        }


        private void initGodPane(String godName){
            god = new BorderPane();
            god.getStyleClass().add("fullbackground");
            god.setId(godName);
            initGrid.add(god,0,0);
            GridPane borderGod = createGrid(1,1);
            borderGod.getStyleClass().add("fullbackground");
            borderGod.setId("godBackground");
            god.setCenter(borderGod);
            infoPowerGrid = createGrid(2,4);
            borderGod.add(infoPowerGrid,0,0);
        }

        private void initInfoGodPower(String godPower){
            GridPane infoGrid = createGrid(6,5);
            GridPane godPowerGrid = createGrid(6,7);
            Button info = new Button();
            BorderPane godPowerPane = new BorderPane();
            info.getStyleClass().add("fullbackground");
            info.setId("infoButton");
            godPowerPane.getStyleClass().add("fullbackground");
            godPowerPane.setId(godPower);
            infoGrid.add(info, 1,3, 2,4);
            godPowerGrid.add(godPowerPane,0,1, 5, 4);

        }

        private void initSelectButton(){
            GridPane actionButtonGrid = createGrid(7,5);
            actionButton = new Button();
            actionButton.getStyleClass().add("fullbackground");
            actionButton.getStyleClass().add("invisiblebackground");
            actionButton.setId("selectbutton");
            actionButton.setText("SELECT");
            actionButtonGrid.add(actionButton,1,1,4,5);
        }

        public void toggleVisibility(){
                god.setVisible(!god.isVisible());
        }

        public String getGod() {return god.getId();}
    }

    @FXML
    GridPane mainGrid;
    @FXML
    Button info;
    @FXML
    Label titleLabel;
    @FXML
    GridPane gridTwoPlayer;
    @FXML
    GridPane gridThreePlayer;



    public void initialize() {
        super.initialize(this);
        info.setOnMouseEntered(event -> info.setId("infoPressed"));

        info.setOnMouseExited(event -> info.setId("infoButton"));

    }

    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        // update da view quando è il turno di un altro client
    }

    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        // update da view quando tocca a questo client
    }

    public void updateStarterPlayerSelection(Map<String, String> choices) {
        // update da view a tutti i giocatori notificando che il challenger sta scegliendo lo starter player
    }

    public void requestStarterPlayer(Map<String, String> choices) {
        // update da view al challenger avvertendolo che deve scegliere lo starter player
    }

    private void initGodsSelectionWindow() {

    }

    private void initTitleLabel() {
        titleLabel.setText("Choose your god");
    }
}

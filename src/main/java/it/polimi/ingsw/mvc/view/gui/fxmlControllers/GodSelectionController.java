package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.*;

public class GodSelectionController extends GenericController {



    private class GodCard extends BorderPane {
        private BorderPane god;
        private BorderPane actionPane;
        private Label actionText;
        private GridPane firstGrid;
        private GridPane initGrid;
        private GridPane infoPowerGrid;
        private GridPane preGod;

        private String playerName;

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
            firstGrid = createGrid(6, 1);
            borderTemp.setPrefSize(100,100);
            firstGrid.add(borderTemp, 0,0, 1, 5);
            initGrid = createGrid(1,1);
            //decorateNode(initGrid,"cardBackground", Arrays.asList("fullbackground"), null);
            initGrid.getStyleClass().add("fullbackground");
            initGrid.setId("cardBackground");
            borderTemp.setCenter(initGrid);


        }

        private void initDescription(String godName){
            Label godNameLabel = new Label();
            Label descriptionLabel = new Label();
            Label conditionLabel = new Label();

            GridPane gridPreDescription = createGrid(1,1);
            gridPreDescription.setPadding(new Insets(15,20,0,20));
            initGrid.add(gridPreDescription,0,0);
            GridPane gridLabel = createGrid(16,10);
            gridPreDescription.add(gridLabel,0,0);
            godNameLabel.setText(godName);
            godNameLabel.getStyleClass().add("fullbackground");
            godNameLabel.setId("godNameLabel");
            godNameLabel.setPrefSize(200,50);
            String[] temp = Gods.valueOf(godName).getDescription().split(":");
            conditionLabel.setText("Effect\n("+temp[0]+")");
            conditionLabel.setPrefSize(200,100);
            conditionLabel.setId("conditionLabel");
            descriptionLabel.setText(temp[1]);
            descriptionLabel.setAlignment(Pos.TOP_LEFT);
            descriptionLabel.setPrefSize(200,200);
            descriptionLabel.setId("descriptionLabel");
            gridLabel.add(godNameLabel,0,0,10,2);
            gridLabel.add(conditionLabel,0,2,10,3);
            gridLabel.add(descriptionLabel,0,5,10,9);
            gridPreDescription.setOnMouseExited(event -> showNode(preGod));

        }


        private void initGodPane(String godName){
            preGod = createGrid(5,1);
            god = new BorderPane();
            god.getStyleClass().add("fullbackground");
            god.setId(godName);
            preGod.add(god,0,0,1,4);
            initGrid.add(preGod,0,0);
            GridPane borderGod = createGrid(1,1);
            borderGod.getStyleClass().add("fullbackground");
            borderGod.setId("godBackground");
            preGod.add(borderGod,0,0,1,5);
            infoPowerGrid = createGrid(4,2);
            borderGod.add(infoPowerGrid,0,0);
        }

        private void initInfoGodPower(String godPower){
            GridPane infoGrid = createGrid(5,6);
            GridPane godPowerGrid = createGrid(7,6);
            BorderPane infoBorder = new BorderPane();
            BorderPane godPowerPane = new BorderPane();
            infoPowerGrid.add(infoGrid,1,0);
            infoPowerGrid.add(godPowerGrid,0,3, 2,1);
            infoBorder.getStyleClass().add("fullbackground");
            infoBorder.getStyleClass().add("invisiblebackground");
            infoBorder.setId("infoButton");
            godPowerPane.getStyleClass().add("fullbackground");
            godPowerPane.setId(godPower);
            infoGrid.add(infoBorder,3,1,2,2);
            godPowerGrid.add(godPowerPane,1,0, 4, 6);
            infoBorder.setOnMouseEntered(event -> hideNode(preGod));

        }

        private void initSelectButton(){
            GridPane actionButtonGrid = createGrid(5,7);
             actionPane = new BorderPane();
             actionText = new Label();
            actionPane.setCenter(actionText);
            firstGrid.add(actionButtonGrid,0,5);
            actionPane.getStyleClass().add("fullbackground");
            actionPane.getStyleClass().add("invisiblebackground");
            actionPane.setId("selectbutton");
            actionText.setText("SELECT");
            actionText.setPadding(new Insets(0,0,7,0));
            actionButtonGrid.add(actionPane,1,1,5,4);
        }

        private void decorateNode(Node node, String id, List<String> classes, String text) {
            if(id != null) { node.setId(id); }
            if(classes != null) { node.getStyleClass().addAll(classes); }
            if(text != null && node instanceof Label) { ((Label) node).setText(text); }
        }

        public String getGod() {
            return god.getId();
        }

        public void disableButton() {
            actionPane.setDisable(true);
        }

        public void enableButton() {
            if (!actionPane.getId().equals("playerNameLabel")) {
                actionPane.setDisable((false));
                actionPane.setId("selectbutton");
                actionPane.setOnMouseClicked(event -> actionPane.setId("selectbuttonpressed"));
                actionPane.setOnMouseReleased(event -> gui.validatePlayerGodChoice(getGod()));
            }
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
            actionText.setText(playerName);
            actionPane.setId("playerNameLabel");
            actionText.setOpacity(1);
            actionText.setPadding(new Insets(0,0,2,0));

        }

        public void setStarterPlayerButton() {
            actionPane.setDisable(false);
            actionPane.setId("playerbutton");
            actionText.setPadding(new Insets(0,0,7,0));
            actionPane.setOnMouseClicked(event -> actionPane.setId("playerbuttonpressed"));
            actionPane.setOnMouseReleased(event -> gui.validatePlayer(getPlayerName()));

        }

        public void setActionButtonDisabled() {
            actionPane.setId("selectbutton");
            actionText.setPadding(new Insets(0,0,7,0));
            disableButton();
        }
    }



    @FXML
    GridPane mainGrid;
    @FXML
    Label titleLabel;
    @FXML
    GridPane twoPlayerFirstGrid;
    @FXML
    GridPane twoPlayerSecondGrid;
    @FXML
    GridPane gridThreePlayer;


    List<GodCard> cards = new ArrayList<>();
    List<GridPane> gridsTwoPlayer;

    public void initialize() {
        super.initialize(this);
        gridsTwoPlayer = new ArrayList<>(Arrays.asList(twoPlayerFirstGrid, twoPlayerSecondGrid));
    }

    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        // update da view quando è il turno di un altro client
        loadSelectedGods(chosenGods, choices);
        titleLabel.setText(turnOwner + " is choosing a god");
        cards.forEach(GodCard::disableButton);

    }


    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        // update da view quando tocca a questo client
        loadSelectedGods(chosenGods, choices);
        titleLabel.setText("Choose your god");
        cards.forEach(GodCard::enableButton);
    }


    public void updateStarterPlayerSelection(Map<String, String> choices) {
        // update da view a tutti i giocatori notificando che il challenger sta scegliendo lo starter player
        titleLabel.setText("The Challenger is choosing the starter player");
        nameUpdate(choices);
        cards.forEach(GodCard::setActionButtonDisabled);
    }

    public void requestStarterPlayer(Map<String, String> choices) {
        // update da view al challenger avvertendolo che deve scegliere lo starter player
        titleLabel.setText("Choose the starter player");
        nameUpdate(choices);
        cards.forEach(GodCard::setStarterPlayerButton);
    }


    private void loadSelectedGods(List<String> chosenGods, Map<String, String> choices) {
        if (chosenGods.size() == Integer.parseInt(gui.getNumberOfPlayers())) {
            createGodCards(chosenGods);
        }
        else {
            nameUpdate(choices);
        }
    }

    private void createGodCards(List<String> chosenGods) {
        if (Integer.parseInt(gui.getNumberOfPlayers()) == 3) {
            chosenGods.forEach(g -> gridThreePlayer.add(createCard(g), chosenGods.indexOf(g),0));
            disableTwo();
        }
        else {
            gridsTwoPlayer.forEach(g -> g.add(createCard(chosenGods.get(gridsTwoPlayer.indexOf(g))),0,0));
            disableThree();
        }
    }

    private GodCard createCard(String god) {
        GodCard temp = new GodCard(god);
        cards.add(temp);
        return temp;
    }
    private void disableTwo() {
        gridsTwoPlayer.forEach(g -> g.setDisable(true));
    }

    private void disableThree() {
        gridThreePlayer.setDisable(true);
    }

    private void nameUpdate(Map<String, String> choices){
        choices.keySet().stream()
                .filter(nome -> !nome.equals(""))
                .forEach(key -> {
                    for(GodCard god: cards) {
                        if(god.getGod().equals(choices.get(key))) {
                            god.setPlayerName(key);
                        }
                    }
                });
    }
}

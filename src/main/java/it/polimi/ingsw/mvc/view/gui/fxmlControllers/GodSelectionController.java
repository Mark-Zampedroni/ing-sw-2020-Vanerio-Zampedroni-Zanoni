package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.*;

public class GodSelectionController extends GenericController {

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
        setFontRatio(titleLabel);
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
        } else {
            nameUpdate(choices);
        }
    }

    private void createGodCards(List<String> chosenGods) {
        if (Integer.parseInt(gui.getNumberOfPlayers()) == 3) {
            chosenGods.forEach(g -> gridThreePlayer.add(createCard(g), chosenGods.indexOf(g), 0));
            disableTwo();
        } else {
            gridsTwoPlayer.forEach(g -> g.add(createCard(chosenGods.get(gridsTwoPlayer.indexOf(g))), 0, 0));
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

    private void nameUpdate(Map<String, String> choices) {
        choices.keySet().stream()
                .filter(nome -> !nome.equals(""))
                .forEach(key -> {
                    for (GodCard god : cards) {
                        if (god.getGod().equals(choices.get(key))) {
                            god.setPlayerName(key);
                        }
                    }
                });
    }


    private class GodCard extends BorderPane {

        private BorderPane god;
        private BorderPane actionPane;
        private Label actionText;
        private GridPane initGrid;
        private GridPane preGod;

        private String playerName;

        public GodCard(String godName) {
            GridPane firstGrid = initInit();
            initDescription(godName);
            GridPane infoPowerGrid = initGodPane(godName);
            initInfoGodPower(godName.toLowerCase() + "power", infoPowerGrid);
            initSelectButton(firstGrid);
            this.setCenter(firstGrid);
            Platform.runLater(() -> setFontRatio(actionText));
        }

        private GridPane initInit() {
            BorderPane borderTemp = new BorderPane();
            GridPane firstGrid = createGrid(6, 1);
            borderTemp.setPrefSize(100, 100);
            firstGrid.add(borderTemp, 0, 0, 1, 5);
            initGrid = createGrid(1, 1);
            decorateNode(initGrid, "cardBackground", Collections.singletonList("fullbackground"), null);
            borderTemp.setCenter(initGrid);
            return firstGrid;
        }

        private void initDescription(String godName) {
            String[] temp = Gods.valueOf(godName).getDescription().split(":");
            GridPane gridPreDescription = createGrid(1, 1);
            gridPreDescription.setPadding(new Insets(15, 20, 0, 20));
            initGrid.add(gridPreDescription, 0, 0);
            GridPane gridLabel = createGrid(16, 10);
            gridPreDescription.add(gridLabel, 0, 0);
            gridLabel.add(createLabel("conditionLabel", null, "Effect\n(" + temp[0] + ")", null), 0, 2, 10, 3);
            gridLabel.add(createLabel("descriptionLabel", null, temp[1], Pos.TOP_LEFT), 0, 5, 10, 9);
            gridLabel.add(createLabel("godNameLabel", Collections.singletonList("fullbackground"), godName, null), 0, 0, 10, 2);
            gridPreDescription.setOnMouseExited(event -> showNode(preGod));
        }

        private Label createLabel(String id, List<String> classes, String text, Pos position) {
            Label temp = new Label();
            decorateNode(temp, id, classes, text);
            temp.setPrefSize(500, 500);
            if (position != null) {
                temp.setAlignment(position);
            }
            Platform.runLater(() -> setFontRatio(temp));
            return temp;
        }

        private GridPane initGodPane(String godName) {
            preGod = createGrid(5, 1);
            god = new BorderPane();
            decorateNode(god, godName, Collections.singletonList("fullbackground"), null);
            preGod.add(god, 0, 0, 1, 4);
            initGrid.add(preGod, 0, 0);
            GridPane borderGod = createGrid(1, 1);
            decorateNode(borderGod, "godBackground", Collections.singletonList("fullbackground"), null);
            preGod.add(borderGod, 0, 0, 1, 5);
            GridPane infoPowerGrid = createGrid(4, 2);
            borderGod.add(infoPowerGrid, 0, 0);
            return infoPowerGrid;
        }

        private void initInfoGodPower(String godPower, GridPane infoPowerGrid) {
            GridPane infoGrid = createGrid(5, 6);
            GridPane godPowerGrid = createGrid(7, 6);
            BorderPane infoBorder = new BorderPane();
            BorderPane godPowerPane = new BorderPane();
            infoPowerGrid.add(infoGrid, 1, 0);
            infoPowerGrid.add(godPowerGrid, 0, 3, 2, 1);
            decorateNode(infoBorder, "infoButton", Arrays.asList("fullbackground", "invisiblebackground"), null);
            decorateNode(godPowerPane, godPower, Collections.singletonList("fullbackground"), null);
            infoGrid.add(infoBorder, 3, 1, 2, 2);
            godPowerGrid.add(godPowerPane, 1, 0, 4, 6);
            infoBorder.setOnMouseEntered(event -> hideNode(preGod));
        }

        private void initSelectButton(GridPane firstGrid) {
            GridPane actionButtonGrid = createGrid(5, 7);
            actionPane = new BorderPane();
            actionText = new Label();
            actionPane.setCenter(actionText);
            firstGrid.add(actionButtonGrid, 0, 5);
            decorateNode(actionPane, "selectbutton", Arrays.asList("fullbackground", "invisiblebackground"), null);
            actionText.setText("SELECT");
            actionText.setId("actionlabel");
            actionText.setPadding(new Insets(0, 0, 7, 0));
            actionButtonGrid.add(actionPane, 1, 1, 5, 4);
        }

        private void decorateNode(Node node, String id, List<String> classes, String text) {
            if (id != null) {
                node.setId(id);
            }
            if (classes != null) {
                node.getStyleClass().addAll(classes);
            }
            if (text != null && node instanceof Label) {
                ((Label) node).setText(text);
            }
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
                setButtonState(() -> actionPane.setId("selectbuttonpressed"), () -> {
                    actionPane.setId("selectbutton");
                    gui.validatePlayerGodChoice(getGod());
                });
            }
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
            actionPane.setId("playerNameLabel");
            actionText.setText(playerName);
            actionText.setOpacity(1);
            actionText.setPadding(new Insets(0, 0, 2, 0));

        }

        public void setStarterPlayerButton() {
            actionPane.setDisable(false);
            actionPane.setId("playerbutton");
            actionText.setPadding(new Insets(0, 0, 7, 0));
            setButtonState(() -> actionPane.setId("playerbuttonpressed"), () -> gui.validatePlayer(getPlayerName()));
        }

        public void setActionButtonDisabled() {
            actionPane.setId("selectbutton");
            actionText.setPadding(new Insets(0, 0, 7, 0));
            disableButton();
        }

        public void setButtonState(Runnable onPress, Runnable onRelease) {
            actionPane.setOnMousePressed(event -> onPress.run());
            actionPane.setOnMouseReleased(event -> onRelease.run());
        }
    }

}

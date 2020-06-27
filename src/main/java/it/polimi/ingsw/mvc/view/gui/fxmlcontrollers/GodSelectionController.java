package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

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

/**
 * Gods selection screen FXML controller
 */
public class GodSelectionController extends GenericController {

    @FXML
    Label titleLabel;
    @FXML
    GridPane twoPlayerFirstGrid;
    @FXML
    GridPane twoPlayerSecondGrid;
    @FXML
    GridPane gridThreePlayer;

    private static final String FULL_BACKGROUND = "fullbackground";
    private static final String SELECT_BUTTON = "selectbutton";

    List<GodCard> cards = new ArrayList<>();
    List<GridPane> gridsTwoPlayer;

    /**
     * Initializes the main features of the scene
     */
    public void initialize() {
        super.initialize(this);
        gridsTwoPlayer = new ArrayList<>(Arrays.asList(twoPlayerFirstGrid, twoPlayerSecondGrid));
        setFontRatio(titleLabel);
    }

    /**
     * Updates all the players, except the turn owner, on the selected gods
     *
     * @param chosenGods list of chosen gods
     * @param choices    map which contains players'name and the god they had picked
     * @param turnOwner  owner of the turn
     */
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        loadSelectedGods(chosenGods, choices);
        titleLabel.setText(turnOwner + " is choosing a god");
        cards.forEach(GodCard::disableButton);
    }

    /**
     * Updates the turn owner on the other players' choices and allows it to choose its god
     *
     * @param chosenGods list of chosen gods
     * @param choices map which contains players'name and the god they had picked
     */
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        loadSelectedGods(chosenGods, choices);
        titleLabel.setText("Choose your god");
        cards.forEach(GodCard::enableButton);
    }

    /**
     * Updates all the players on the challenger's starter player choice
     *
     * @param choices map which contains players'name and the god they had picked
     */
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        titleLabel.setText("The Challenger is choosing the starter player");
        nameUpdate(choices);
        cards.forEach(GodCard::setActionButtonDisabled);
    }

    /**
     * Allows the challenger to choose the starter player
     *
     * @param choices map which contains players'name and the god they had picked
     */
    public void requestStarterPlayer(Map<String, String> choices) {
        titleLabel.setText("Choose the starter player");
        nameUpdate(choices);
        cards.forEach(GodCard::setStarterPlayerButton);
    }

    /**
     * Updates the scene through the creation of new {@link GodCard cards} or displaying the players' choice
     *
     * @param chosenGods list of chosen gods
     * @param choices map which contains players'name and the god they had picked
     */

    private void loadSelectedGods(List<String> chosenGods, Map<String, String> choices) {
        if (chosenGods.size() == Integer.parseInt(gui.getNumberOfPlayers()))
            createGodCards(chosenGods);
        else
            nameUpdate(choices);
    }

    /**
     * Creates the {@link GodCard cards} based on the argument chosen gods
     *
     * @param chosenGods list of chosen gods
     */
    private void createGodCards(List<String> chosenGods) {
        if (Integer.parseInt(gui.getNumberOfPlayers()) == 3) {
            chosenGods.forEach(g -> gridThreePlayer.add(createCard(g), chosenGods.indexOf(g), 0));
            disableTwo();
        } else {
            gridsTwoPlayer.forEach(g -> g.add(createCard(chosenGods.get(gridsTwoPlayer.indexOf(g))), 0, 0));
            disableThree();
        }
    }

    /**
     * Creates a {@link GodCard card} node
     *
     * @param god name of the god
     */
    private GodCard createCard(String god) {
        GodCard temp = new GodCard(god);
        cards.add(temp);
        return temp;
    }

    /**
     * Disables the nodes for the 2-players mode
     */
    private void disableTwo() {
        gridsTwoPlayer.forEach(g -> g.setDisable(true));
    }

    /**
     * Disables the nodes for the 3-players mode
     */
    private void disableThree() {
        gridThreePlayer.setDisable(true);
    }

    /**
     * Displays below a {@link GodCard card} the player who picked that god
     *
     * @param choices map which contains players'name and the god they had picked
     */
    private void nameUpdate(Map<String, String> choices) {
        choices.keySet().stream()
                .filter(nome -> !nome.equals(""))
                .forEach(key -> {
                    for (GodCard god : cards) {
                        if (god.getGod().equals(choices.get(key)))
                            god.setPlayerName(key);
                    }
                });
    }

    /**
     * A card which contains all the information concerning a god and its power, it can be selected by a player
     */
    private class GodCard extends BorderPane {

        private BorderPane god;
        private BorderPane actionPane;
        private Label actionText;
        private GridPane initGrid;

        private String playerName;


        /**
         * Initializes the god card
         *
         * @param godName god's name
         */
        public GodCard(String godName) {
            GridPane preGod = createGrid(5, 1);
            GridPane firstGrid = initInit();
            initDescription(godName, preGod);
            GridPane infoPowerGrid = initGodPane(godName, preGod);
            initInfoGodPower(godName.toLowerCase() + "power", infoPowerGrid, preGod);
            initSelectButton(firstGrid);
            this.setCenter(firstGrid);
            Platform.runLater(() -> setFontRatio(actionText));
        }

        /**
         * Adds the background image to the {@link GodCard card}
         */
        private GridPane initInit() {
            BorderPane borderTemp = new BorderPane();
            GridPane firstGrid = createGrid(6, 1);
            borderTemp.setPrefSize(100, 100);
            firstGrid.add(borderTemp, 0, 0, 1, 5);
            initGrid = createGrid(1, 1);
            decorateNode(initGrid, "cardBackground", Collections.singletonList(FULL_BACKGROUND), null);
            borderTemp.setCenter(initGrid);
            return firstGrid;
        }

        /**
         * Adds information labels about the chosen god
         *
         * @param godName god's name
         * @param preGod  card grid node
         */
        private void initDescription(String godName, GridPane preGod) {
            String[] temp = Gods.valueOf(godName).getDescription().split(":");
            GridPane gridPreDescription = createGrid(1, 1);
            gridPreDescription.setPadding(new Insets(15, 20, 0, 20));
            initGrid.add(gridPreDescription, 0, 0);
            GridPane gridLabel = createGrid(16, 10);
            gridPreDescription.add(gridLabel, 0, 0);
            gridLabel.add(createLabel("conditionLabel", null, "Effect\n(" + temp[0] + ")", null), 0, 2, 10, 3);
            gridLabel.add(createLabel("descriptionLabel", null, temp[1], Pos.TOP_LEFT), 0, 5, 10, 9);
            gridLabel.add(createLabel("godNameLabel", Collections.singletonList(FULL_BACKGROUND), godName, null), 0, 0, 10, 2);
            gridPreDescription.setOnMouseExited(event -> showNode(preGod));
        }

        /**
         * Creates a label with default properties
         *
         * @param id label's name
         * @param text label's text
         * @param position defines the type of alignment for the label
         * @param classes defines the background image for the label
         */
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

        /**
         * Adds the image of the selected god
         *
         * @param godName chosen god
         * @param preGod  card grid node
         */
        private GridPane initGodPane(String godName, GridPane preGod) {
            god = new BorderPane();
            decorateNode(god, godName, Collections.singletonList(FULL_BACKGROUND), null);
            preGod.add(god, 0, 0, 1, 4);
            initGrid.add(preGod, 0, 0);
            GridPane borderGod = createGrid(1, 1);
            decorateNode(borderGod, "godBackground", Collections.singletonList(FULL_BACKGROUND), null);
            preGod.add(borderGod, 0, 0, 1, 5);
            GridPane infoPowerGrid = createGrid(4, 2);
            borderGod.add(infoPowerGrid, 0, 0);
            return infoPowerGrid;
        }

        /**
         * Adds the info button which allows the player to read this god's abilities
         *
         * @param godPower      chosen god
         * @param infoPowerGrid defines the place where the info button is going to be settled in
         * @param preGod        card grid node
         */
        private void initInfoGodPower(String godPower, GridPane infoPowerGrid, GridPane preGod) {
            GridPane infoGrid = createGrid(5, 6);
            GridPane godPowerGrid = createGrid(7, 6);
            BorderPane infoBorder = new BorderPane();
            BorderPane godPowerPane = new BorderPane();
            infoPowerGrid.add(infoGrid, 1, 0);
            infoPowerGrid.add(godPowerGrid, 0, 3, 2, 1);
            decorateNode(infoBorder, "infoButton", Arrays.asList(FULL_BACKGROUND, "invisiblebackground"), null);
            decorateNode(godPowerPane, godPower, Collections.singletonList(FULL_BACKGROUND), null);
            infoGrid.add(infoBorder, 3, 1, 2, 2);
            godPowerGrid.add(godPowerPane, 1, 0, 4, 6);
            infoBorder.setOnMouseEntered(event -> hideNode(preGod));
        }

        /**
         * Adds the button through which a player can choose his god
         *
         * @param firstGrid defines the place where the select button is going to be settled in
         */
        private void initSelectButton(GridPane firstGrid) {
            GridPane actionButtonGrid = createGrid(5, 7);
            actionPane = new BorderPane();
            actionText = new Label();
            actionPane.setCenter(actionText);
            firstGrid.add(actionButtonGrid, 0, 5);
            decorateNode(actionPane, SELECT_BUTTON, Arrays.asList(FULL_BACKGROUND, "invisiblebackground"), null);
            actionText.setText("SELECT");
            actionText.setId("actionlabel");
            actionText.setPadding(new Insets(0, 0, 7, 0));
            actionButtonGrid.add(actionPane, 1, 1, 5, 4);
        }

        /**
         * Adds a list of style classes and a text to a generic node
         *
         * @param node chosen element
         * @param classes style classes
         * @param id node's name
         * @param text node's text
         */
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

        /**
         * Getter for the card's god
         *
         * @return the name of the god associated with this card
         */
        public String getGod() {
            return god.getId();
        }

        /**
         * Disables the select button
         */
        public void disableButton() {
            actionPane.setDisable(true);
        }


        /**
         * Enables the select button which allows a player to choose his god
         */
        public void enableButton() {
            if (!actionPane.getId().equals("playerNameLabel")) {
                actionPane.setDisable((false));
                actionPane.setId(SELECT_BUTTON);
                setButtonState(() -> actionPane.setId("selectbuttonpressed"), () -> {
                    actionPane.setId(SELECT_BUTTON);
                    gui.validatePlayerGodChoice(getGod());
                });
            }
        }


        /**
         * Returns the name of the player who chose the card
         *
         * @return the name of a player
         */
        public String getPlayerName() {
            return playerName;
        }


        /**
         * Sets and displays the player who chose the card
         *
         * @param playerName name of the player
         */
        public void setPlayerName(String playerName) {
            this.playerName = playerName;
            actionPane.setId("playerNameLabel");
            actionText.setText(playerName);
            actionText.setOpacity(1);
            actionText.setPadding(new Insets(0, 0, 2, 0));
        }

        /**
         * Displays a button for the choice of the starting player
         */
        public void setStarterPlayerButton() {
            actionPane.setDisable(false);
            actionPane.setId("playerbutton");
            actionText.setPadding(new Insets(0, 0, 7, 0));
            setButtonState(() -> actionPane.setId("playerbuttonpressed"), () -> gui.validatePlayer(getPlayerName()));
        }

        /**
         * Resets the select button to default and disables it
         */
        public void setActionButtonDisabled() {
            actionPane.setId(SELECT_BUTTON);
            actionText.setPadding(new Insets(0, 0, 7, 0));
            disableButton();
        }

        /**
         * Defines the select button actions after being clicked
         *
         * @param onPress action made after being pressed
         * @param onRelease action made after the release
         */
        public void setButtonState(Runnable onPress, Runnable onRelease) {
            actionPane.setOnMousePressed(event -> onPress.run());
            actionPane.setOnMouseReleased(event -> onRelease.run());
        }
    }

}

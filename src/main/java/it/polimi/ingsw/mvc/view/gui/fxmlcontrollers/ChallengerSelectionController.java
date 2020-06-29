package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardScene;
import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Challenger screen FXML controller
 */
public class ChallengerSelectionController extends GenericController {

    @FXML
    public Button selectButton;
    @FXML
    public Label godNameBanner;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label conditionLabel;
    @FXML
    public Label challengerLabel;
    @FXML
    public Label infoLabel;
    @FXML
    public Label emptySelectedLabel;
    @FXML
    public BorderPane godsPane;
    @FXML
    public ScrollPane godsScroll;
    @FXML
    public GridPane godsGrid;
    @FXML
    public GridPane selectedGrid;
    @FXML
    public Label selectedNameLabel;
    private int godRow;
    private int godColumn;
    private String selectedGod;
    private List<String> chosenGods = new ArrayList<>();

    /**
     * Initializes the main features of the scene
     */
    public void initialize() throws IOException {
        super.initialize(this);
        Platform.runLater(this::initGodsSelectionWindow);
        initSelectionButton();
        initSelectLabel();
        initFonts();
        System.out.println("yey");
        BoardScene.startBoardLoad(gui.getPlayers(), gui.log);
        System.out.println("yey2");
    }

    /**
     * Initializes the fonts scaling
     */
    private void initFonts() {
        setFontRatio(selectButton);
        setFontRatio(godNameBanner);
        setFontRatio(descriptionLabel);
        setFontRatio(conditionLabel);
        setFontRatio(challengerLabel);
        setFontRatio(infoLabel);
        setFontRatio(emptySelectedLabel);
        setFontRatio(selectedNameLabel);
    }

    /**
     * Initializes a label
     */
    private void initSelectLabel() {
        emptySelectedLabel.setText("No gods selected yet");
    }

    /**
     * Initializes the select button and sets its events
     */
    private void initSelectionButton() {
        selectButton.setOnMousePressed(event -> selectButton.setId("selectbuttonpressed"));
        selectButton.setOnMouseReleased(event -> {
            selectButton.setId("selectbutton");
            gui.validateGods(selectedGod);
        });
    }

    /**
     * Initializes the variables with a delay in order to give the JavaFx thread the needed time to correctly load the values
     */
    private void initGodsSelectionWindow() {
        Platform.runLater(this::loadGods);
        Platform.runLater(this::resizeGods);
    }

    /**
     * Creates a space and dynamically adds {@link GodWindow frames} for all the playable gods in the game
     */
    private void loadGods() {
        for (String godName : Gods.getGodsStringList()) {
            GodWindow godWindow = new GodWindow(godName);
            godsGrid.add(godWindow, godColumn, godRow);
            godRow = (godColumn == 2) ? godRow + 1 : godRow;
            godColumn = (godColumn == 2) ? 0 : godColumn + 1;
            addGodClickEvent(godWindow);
        }
        godsGrid.getChildren().removeIf(c -> !(c instanceof GodWindow));
        godWindowConsumer((GodWindow) godsGrid.getChildren().get(0)); // Selects first god
    }

    /**
     * Sets for all the gods the related nodes events
     *
     * @param godWindow the place where to click
     */
    private void addGodClickEvent(GodWindow godWindow) {
        godWindow.setOnMouseClicked(event -> godWindowConsumer(godWindow));
    }

    /**
     * Initializes a {@link GodWindow frame}
     *
     * @param godWindow targeted frame
     */
    private void godWindowConsumer(GodWindow godWindow) {
        godWindow.setCornice("blueborder");
        selectedGod = godWindow.getGod();
        godsGrid.getChildren().stream()
                .filter(god -> god != godWindow)
                .map(GodWindow.class::cast)
                .forEach(god -> god.setCornice("whiteborder"));
        displayDescription(selectedGod);
        selectButtonChange(selectedGod);
    }

    /**
     * Removes the option to choose a god
     *
     * @param selectedGod god's name
     */
    private void selectButtonChange(String selectedGod) {
        selectButton.setDisable(chosenGods.contains(selectedGod));
    }

    /**
     * Displays the description for a given god
     *
     * @param godName targeted god
     */
    private void displayDescription(String godName) {
        godNameBanner.setText(godName);
        String[] temp = Gods.valueOf(godName).getDescription().split(":");
        conditionLabel.setText("Effect (" + temp[0] + ")");
        descriptionLabel.setText(temp[1]);
    }

    /**
     * Binds the size of the gods' nodes
     */
    private void resizeGods() {
        godsPane.minHeightProperty().bind(godsScroll.heightProperty().divide(0.5297560975609756));
        godsPane.maxHeightProperty().bind(godsScroll.heightProperty().divide(0.5297560975609756));
    }

    /**
     * Updates the challenger and allows it to choose a god
     *
     * @param chosenGods list of chosen gods
     */
    public void requestChallengerGod(List<String> chosenGods) {
        loadSelectedGods(chosenGods);
        setChallengerInfo();
    }

    /**
     * Updates the players, except the challenger, on the challenger's choices
     *
     * @param chosenGods list of gods chosen by the challenger
     */
    public void updateChallengerGodSelection(List<String> chosenGods) {
        loadSelectedGods(chosenGods);
        setOthersInfo();
    }

    /**
     * Displays information to the challenger
     */
    private void setChallengerInfo() {
        selectButtonChange(selectedGod);
        challengerLabel.setText("You are the challenger.");
        infoLabel.setText("Choose " + gui.getNumberOfPlayers() + " gods, everyone will select their from the ones you choose. You will be the last one to select yours.");
    }

    /**
     * Displays information to all the players except the challenger
     */
    private void setOthersInfo() {
        hideNode(selectButton);
        challengerLabel.setText("You are not the challenger.");
        infoLabel.setText("Wait while the challenger chooses " + gui.getNumberOfPlayers() + " gods! Everyone will select their own from the ones chosen by the challenger.");
    }

    /**
     * Highlights the gods that have already been chosen
     *
     * @param chosenGods list of chosen gods
     */
    private void loadSelectedGods(List<String> chosenGods) {
        chosenGods.stream()
                .filter(god -> !this.chosenGods.contains(god))
                .forEach(newGod -> {
                    GodWindow newWindow = new GodWindow(newGod);
                    newWindow.setCornice("yellowborder");
                    selectedGrid.add(newWindow, chosenGods.size() - 1, 0);
                });
        if (!chosenGods.isEmpty()) {
            hideNode(emptySelectedLabel);
        }
        this.chosenGods = chosenGods;
    }


    /**
     * A frame containing the image of a god
     */
    private static class GodWindow extends GridPane {

        private Pane god;
        private Pane border;

        /**
         * Initializes the main features of the frame
         *
         * @param godName name of the god
         */
        public GodWindow(String godName) {
            initBorder();
            initGodPane(godName);
            GenericController.addColumns(this, 1);
            GenericController.addRows(this, 1);
            this.add(god, 0, 0);
            this.add(border, 0, 0);
        }

        /**
         * Adds the border of the frame
         */
        private void initBorder() {
            border = new Pane();
            border.getStyleClass().add("fullbackground");
            border.setId("whiteborder");
        }

        /**
         * Adds the image of the god
         *
         * @param godName gods'name
         */
        private void initGodPane(String godName) {
            god = new Pane();
            god.getStyleClass().add("fullbackground");
            GridPane.setMargin(god, new Insets(5, 5, 5, 5));
            god.setId(godName);
        }

        /**
         * Decorates the frame border
         *
         * @param id type of decoration
         */
        public void setCornice(String id) {
            border.setId(id);
        }

        /**
         * Returns the name of the god represented within the frame
         *
         * @return the name of the god contained in the frame
         */
        public String getGod() {
            return god.getId();
        }

    }

}

package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class ChallengerSelectionController extends GenericController {

    private class GodWindow extends GridPane {

        private Pane god;
        private Pane border;

        public GodWindow(String godName) {
            initBorder();
            initGodPane(godName);
            GenericController.addColumns(this,1);
            GenericController.addRows(this,1);
            this.add(god,0,0);
            this.add(border,0,0);
        }

        private void initBorder() {
            border = new Pane();
            border.getStyleClass().add("fullbackground");
            border.setId("whiteborder");
        }

        private void initGodPane(String godName) {
            god = new Pane();
            god.getStyleClass().add("fullbackground");
            GridPane.setMargin(god, new Insets(5,5,5,5));
            god.setId(godName);
        }

        public void setCornice(String id) {
            border.setId(id);
        }

        public String getGod() {
            return god.getId();
        }

    }

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

    private int godRow, godColumn;

    private String selectedGod;
    private List<String> chosenGods = new ArrayList<>(); // Una volta collegato al resto sarà null

    public void initialize() throws Exception {
        super.initialize(this);
        Platform.runLater(this::initGodsSelectionWindow);
        initSelectionButton();
        initSelectLabel();
        initFonts();
        BoardScene.startBoardLoad(gui.getPlayers(), gui.LOG);
    }

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

    private void initSelectLabel() {
        emptySelectedLabel.setText("No gods selected yet");
    }

    private void initSelectionButton() {
        selectButton.setOnMousePressed(event -> selectButton.setId("selectbuttonpressed"));

        selectButton.setOnMouseReleased(event -> {
            selectButton.setId("selectbutton");
            gui.validateGods(selectedGod);
        });
    }

    private void initGodsSelectionWindow() {
        Platform.runLater(this::loadGods);
        Platform.runLater(this::resizeGods);
    }

    private void loadGods() {
        for(String godName : Gods.getGodsStringList()) {
            GodWindow godWindow = new GodWindow(godName);
            godsGrid.add(godWindow,godColumn,godRow);
            godRow = (godColumn == 2) ? godRow+1 : godRow;
            godColumn = (godColumn == 2) ? 0 : godColumn+1;
            addGodClickEvent(godWindow);
        }
        godsGrid.getChildren().removeIf(c -> !(c instanceof GodWindow));
        godWindowConsumer((GodWindow) godsGrid.getChildren().get(0)); // Selects first god
    }

    private void addGodClickEvent(GodWindow godWindow) {
        godWindow.setOnMouseClicked(event -> godWindowConsumer(godWindow));
    }

    private void godWindowConsumer(GodWindow godWindow) {
        godWindow.setCornice("blueborder");
        selectedGod = godWindow.getGod();
        godsGrid.getChildren().stream().filter(god -> god != godWindow).forEach(god -> ((GodWindow) god).setCornice("whiteborder"));
        displayDescription(selectedGod);
        selectButtonChange(selectedGod);
    }

    private void selectButtonChange(String selectedGod) {
        selectButton.setDisable(chosenGods.contains(selectedGod));
    }

    private void displayDescription(String godName){
        godNameBanner.setText(godName);
        String[] temp = Gods.valueOf(godName).getDescription().split(":");
        conditionLabel.setText("Effect ("+temp[0]+")");
        descriptionLabel.setText(temp[1]);
    }

    private void resizeGods() {
        godsPane.minHeightProperty().bind(godsScroll.heightProperty().divide(0.5297560975609756));
        godsPane.maxHeightProperty().bind(godsScroll.heightProperty().divide(0.5297560975609756));
    }

    public void requestChallengerGod(List<String> chosenGods) {
        loadSelectedGods(chosenGods);
        setChallengerInfo();
    }

    public void updateChallengerGodSelection(List<String> chosenGods) {
        loadSelectedGods(chosenGods);
        setOthersInfo();
    }

    private void setChallengerInfo() {
        selectButtonChange(selectedGod);
        challengerLabel.setText("You are the challenger.");
        infoLabel.setText("Choose "+gui.getNumberOfPlayers()+" gods, everyone will select their from the ones you choose. You will be the last one to select yours.");
    }

    private void setOthersInfo() {
        hideNode(selectButton);
        challengerLabel.setText("You are not the challenger.");
        infoLabel.setText("Wait while the challenger chooses "+gui.getNumberOfPlayers()+" gods! Everyone will select their own from the ones chosen by the challenger.");
    }

    private void loadSelectedGods(List<String> chosenGods) {
        chosenGods.stream()
                .filter(god -> !this.chosenGods.contains(god))
                .forEach(newGod -> {
                    GodWindow newWindow = new GodWindow(newGod);
                    newWindow.setCornice("yellowborder");
                    selectedGrid.add(newWindow,chosenGods.size()-1,0);
                });
        if(chosenGods.size() > 0) {
            hideNode(emptySelectedLabel);
        }
        this.chosenGods = chosenGods;
    }

}

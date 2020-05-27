package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.Gods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GodSelectionController {

    private class GodWindow extends GridPane {

        private Pane god;
        private Pane border;

        public GodWindow(String godName) {
            initBorder();
            initGodPane(godName);
            addColumnConstraint();
            addRowConstraint();
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

        private void addColumnConstraint() {
            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(10);
            c.setPrefWidth(100);
            c.setHgrow(Priority.SOMETIMES);
            this.getColumnConstraints().add(c);
        }

        private void addRowConstraint() {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(10);
            r.setPrefHeight(30);
            r.setVgrow(Priority.SOMETIMES);
            this.getRowConstraints().add(r);
        }
    }

    @FXML
    public Pane mainPane;
    @FXML
    public Button selectButton;
    @FXML
    public Label godNameLabel;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label conditionLabel;
    @FXML
    public BorderPane godsPane;
    @FXML
    public ScrollPane godsScroll;
    @FXML
    public GridPane godsGrid;

    private int godRow, godColumn;
    private GuiManager gui;
    private double godsRatio;
    private String selectedGod;
    private List<String> chosenGods = new ArrayList<>(Arrays.asList("ATHENA")); // Una volta collegato al resto sarà null


    public void initialize() {
        gui = GuiManager.getInstance();
        initGodsSelectionWindow();
        initSelectionButton();
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
        Platform.runLater(this::getGodsRatio);
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
        godWindowConsumer((GodWindow) godsGrid.getChildren().get(0));
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
        godNameLabel.setText(godName);
        String[] temp = Gods.valueOf(godName).getDescription().split(":");
        conditionLabel.setText("Effect ("+temp[0]+")");
        descriptionLabel.setText(temp[1]);
    }

    private void getGodsRatio() {
        godsRatio = godsScroll.getHeight()/godsPane.getHeight();
    }

    private void resizeGods() {
        godsPane.minHeightProperty().bind(godsScroll.heightProperty().divide(godsRatio));
        godsPane.maxHeightProperty().bind(godsScroll.heightProperty().divide(godsRatio));
    }

    public void requestChallengerGod(List<String> chosenGods) {
        chosenGods.stream()
                .filter(god -> !this.chosenGods.contains(god))
                .forEach(newGod -> System.out.println("Qui verrà aggiunto agli dei scelti "+newGod+" sapendo che e' in posizione "+chosenGods.size()));
        this.chosenGods = chosenGods;
        // Aggiunge nella posizione corretta il nuovo dio dal .foreach ^
    }

}

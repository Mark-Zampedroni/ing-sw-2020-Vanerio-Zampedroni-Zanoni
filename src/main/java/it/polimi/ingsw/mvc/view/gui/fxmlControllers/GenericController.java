package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;

public abstract class GenericController {

    private BorderPane reconnectionLayer;
    protected GenericController windowName;
    protected GuiManager gui;
    @FXML
    public Pane main;
    @FXML
    public GridPane mainGrid;

    public Class<?> getWindowName() {
        return windowName.getClass();
    }

    public void initialize(GenericController controller) {
        gui = GuiManager.getInstance();
        gui.setCurrentController(controller);
        windowName = controller;
    }

    public Scene getScene() {
        return main.getScene();
    }

    protected void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    protected void showNode(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }

    public void showReconnection(boolean isReconnecting) {
        if(isReconnecting) {
            mainGrid.add(loadReconnectionLayer(),0,0, mainGrid.getColumnCount(), mainGrid.getRowCount());
        }
        else {
            if(reconnectionLayer != null) {
                mainGrid.getChildren().remove(reconnectionLayer);
            }
        }
    }

    private BorderPane loadReconnectionLayer() {
        return (reconnectionLayer == null) ? createReconnectionLayer() : reconnectionLayer;
    }

    private BorderPane createReconnectionLayer() {

        BorderPane layer = new BorderPane();
        layer.setId("bgConnection");

        GridPane innerGrid = new GridPane();
        layer.setCenter(innerGrid);
        addColumns(innerGrid,8);
        addRows(innerGrid,6);

        BorderPane wifiSymbol = new BorderPane();
        wifiSymbol.setId("wifiSymbol");
        innerGrid.add(wifiSymbol,3,1,2,2);

        reconnectionLayer = layer;
        return layer;
    }

    public static GridPane createGrid(int rows, int columns) {
        GridPane out = new GridPane();
        GenericController.addRows(out,rows);
        GenericController.addColumns(out,columns);
        return out;
    }

    public static void addColumns(GridPane grid, int quantity) {
        for(int column = 0; column < quantity; column++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(1);
            c.setPrefWidth(1);
            c.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(c);
        }
    }

    public static void addRows(GridPane grid, int quantity) {
        for(int row = 0; row < quantity; row++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(1);
            r.setPrefHeight(1);
            r.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(r);
        }
    }


}

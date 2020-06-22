package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public abstract class GenericController {

    @FXML
    public Pane main;
    @FXML
    public GridPane mainGrid;
    protected GenericController windowName;
    protected GuiManager gui;
    private BorderPane reconnectionLayer;

    public static GridPane createGrid(int rows, int columns) {
        GridPane out = new GridPane();
        GenericController.addRows(out, rows);
        GenericController.addColumns(out, columns);
        return out;
    }

    public static void addColumns(GridPane grid, int quantity) {
        for (int column = 0; column < quantity; column++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(1);
            c.setPrefWidth(1);
            c.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(c);
        }
    }

    public static void addRows(GridPane grid, int quantity) {
        for (int row = 0; row < quantity; row++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(1);
            r.setPrefHeight(1);
            r.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(r);
        }
    }

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

    protected void setFontRatio(Control node) {
        Platform.runLater(() -> handleResizableElement(node));
    }

    protected void handleResizableElement(Control node) {
        double defaultWidth = GuiManager.getInstance().getDefaultWidth();
        Insets defaultPadding = node.getPadding();
        if (Labeled.class.isAssignableFrom(node.getClass())) {
            setFontSize((Labeled) node, defaultWidth, defaultPadding);
        } else if (TextInputControl.class.isAssignableFrom(node.getClass())) {
            setFontSize((TextInputControl) node, defaultWidth, defaultPadding);
        }
    }

    private void setFontSize(Labeled node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }

    private void setFontSize(TextInputControl node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }

    private void setResizeEvent(Control node, double defaultWidth, Insets defaultPadding, Font font) {
        double defaultFontSize = font.getSize();
        String defaultFamily = font.getFamily();
        String defaultStyle = font.getStyle();
        changeFontParameters(node, GuiManager.getInstance().getStage().getWidth(), defaultWidth, defaultFontSize, defaultFamily, defaultStyle, defaultPadding);
        GuiManager.getInstance().getStage().widthProperty().addListener((o, oldWidth, newWidth) -> changeFontParameters(node, newWidth.doubleValue(), defaultWidth, defaultFontSize, defaultFamily, defaultStyle, defaultPadding));
    }

    private void changeFontParameters(Control node, double newWidth, double defaultWidth, double defaultFontSize, String defaultFamily, String defaultStyle, Insets defaultPadding) {
        double widthRatio = newWidth / defaultWidth;
        node.setStyle("-fx-font-size: " + (int) (defaultFontSize * widthRatio) + ";" +
                "-fx-font-family: '" + defaultFamily + "';" +
                (defaultStyle.equals("Bold") ? "-fx-font-weight: bold;" : "") +
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;");
        setPadding(node, defaultPadding, widthRatio);
    }

    private void setPadding(Region node, Insets defaultPadding, double ratio) {
        node.setPadding(new Insets(
                defaultPadding.getTop() * ratio,
                defaultPadding.getRight() * ratio,
                defaultPadding.getBottom() * ratio,
                defaultPadding.getLeft() * ratio
        ));
    }

    public void setPaddingRatio(Pane pane) {
        Insets defaultPadding = pane.getPadding();
        double defaultWidth = GuiManager.getInstance().getDefaultWidth();
        GuiManager.getInstance().getStage().widthProperty().addListener((o, oldWidth, newWidth) -> resizePadding(pane, newWidth.doubleValue(), defaultWidth, defaultPadding));
    }

    private void resizePadding(Pane pane, double newWidth, double defaultWidth, Insets defaultPadding) {
        double widthRatio = newWidth / defaultWidth;
        setPadding(pane, defaultPadding, widthRatio);
    }

    public void showReconnection(boolean isReconnecting) {
        if (isReconnecting) {
            mainGrid.add(loadReconnectionLayer(), 0, 0, mainGrid.getColumnCount(), mainGrid.getRowCount());
        } else {
            if (reconnectionLayer != null) {
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
        addColumns(innerGrid, 8);
        addRows(innerGrid, 6);

        BorderPane wifiSymbol = new BorderPane();
        wifiSymbol.setId("wifiSymbol");
        innerGrid.add(wifiSymbol, 3, 1, 2, 2);

        reconnectionLayer = layer;
        layer.setPickOnBounds(false);

        return layer;
    }


}

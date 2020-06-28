package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

import it.polimi.ingsw.mvc.view.gui.Gui;
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

/**
 * FXML controller containing methods shared or used by all the windows
 */
public abstract class GenericController {

    @FXML
    public Pane main;
    @FXML
    public GridPane mainGrid;
    protected GenericController windowName;
    protected GuiManager gui;
    private BorderPane reconnectionLayer;

    /**
     * Creates a grid pane with a given number of rows and columns
     *
     * @param columns number of columns
     * @param rows number of rows
     * @return the grid pane
     */
    public static GridPane createGrid(int rows, int columns) {
        GridPane out = new GridPane();
        GenericController.addRows(out, rows);
        GenericController.addColumns(out, columns);
        return out;
    }

    /**
     * Adds a defined number of columns to a given grid pane
     *
     * @param grid targeted grid pane
     * @param quantity number of columns
     */
    public static void addColumns(GridPane grid, int quantity) {
        for (int column = 0; column < quantity; column++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(1);
            c.setPrefWidth(1);
            c.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(c);
        }
    }

    /**
     * Adds a defined number of rows to a given grid pane
     *
     * @param grid targeted grid pane
     * @param quantity number of rows
     */
    public static void addRows(GridPane grid, int quantity) {
        for (int row = 0; row < quantity; row++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(1);
            r.setPrefHeight(1);
            r.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(r);
        }
    }

    /**
     * Getter for the name of the window
     *
     * @return a generic class
     *
     */
    public Class<?> getWindowName() {
        return windowName.getClass();
    }

    /**
     * Sets a controller as the current one
     *
     * @param controller targeted controller
     */
    public void initialize(GenericController controller) {
        gui = GuiManager.getInstance();
        gui.setCurrentController(controller);
        windowName = controller;
    }

    /**
     * Getter for the scene
     *
     * @return the scene
     */
    public Scene getScene() {
        return main.getScene();
    }

    /**
     * Hides an element of the scene
     *
     * @param node targeted element
     */
    protected void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    /**
     * Shows an element of the scene
     *
     * @param node targeted element
     */
    protected void showNode(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }

    /**
     * Calls a method with a delay in order to give the JavaFx thread the needed time to correctly load the values
     */
    protected void setFontRatio(Control node) {
        Platform.runLater(() -> handleResizableElement(node));
    }

    /**
     * Updates the size of the text accordingly to the size of the element which contains it
     *
     * @param node targeted element
     */
    protected void handleResizableElement(Control node) {
        double defaultWidth = GuiManager.getInstance().getDefaultWidth();
        Insets defaultPadding = node.getPadding();
        if (Labeled.class.isAssignableFrom(node.getClass()))
            setFontSizeLabeled((Labeled) node, defaultWidth, defaultPadding);
        else if (TextInputControl.class.isAssignableFrom(node.getClass()))
            setFontSizeText((TextInputControl) node, defaultWidth, defaultPadding);
    }

    /**
     * Calls a method to modify the size of the text for a label
     *
     * @param node targeted element
     * @param defaultPadding padding of the text
     * @param defaultWidth old size of the text
     */
    private void setFontSizeLabeled(Labeled node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }

    /**
     * Calls a method to modify the size of the text put by a player
     *
     * @param node targeted element
     * @param defaultPadding padding of the text
     * @param defaultWidth old size of the text
     */
    private void setFontSizeText(TextInputControl node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }


    /**
     * Updates the size of the text for scaling purpose
     *
     * @param node targeted element
     * @param defaultWidth old size of the element
     * @param defaultPadding old value of padding of the text
     */
    private void setResizeEvent(Control node, double defaultWidth, Insets defaultPadding, Font font) {
        double defaultFontSize = font.getSize();
        String defaultFamily = font.getFamily();
        String defaultStyle = font.getStyle();
        changeFontParameters(node, GuiManager.getInstance().getStage().getWidth(), defaultWidth, defaultFontSize, defaultFamily, defaultStyle, defaultPadding);
        GuiManager.getInstance().getStage().widthProperty().addListener((o, oldWidth, newWidth) -> changeFontParameters(node, newWidth.doubleValue(), defaultWidth, defaultFontSize, defaultFamily, defaultStyle, defaultPadding));
    }

    /**
     * Sets a new size for the text and also changes other features
     *
     * @param node targeted element
     * @param defaultWidth old size of the element
     * @param newWidth new size of the element
     * @param defaultPadding old value of padding of the text
     * @param defaultFamily old family of the text
     * @param defaultFontSize old size of the text
     * @param defaultStyle old style of the text
     *
     */
    private void changeFontParameters(Control node, double newWidth, double defaultWidth, double defaultFontSize, String defaultFamily, String defaultStyle, Insets defaultPadding) {
        double widthRatio = newWidth / defaultWidth;
        node.setStyle("-fx-font-size: " + (int) (defaultFontSize * widthRatio) + ";" +
                "-fx-font-family: '" + defaultFamily + "';" +
                (defaultStyle.equals("Bold") ? "-fx-font-weight: bold;" : "") +
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;");
        setPadding(node, defaultPadding, widthRatio);
    }

    /**
     * Sets the padding of a element accordingly to a give value
     *
     * @param node  targeted element
     * @param defaultPadding old values of padding
     * @param ratio value used to update the padding
     */
    private void setPadding(Region node, Insets defaultPadding, double ratio) {
        node.setPadding(new Insets(
                defaultPadding.getTop() * ratio,
                defaultPadding.getRight() * ratio,
                defaultPadding.getBottom() * ratio,
                defaultPadding.getLeft() * ratio
        ));
    }


    /**
     * Upgrades the padding of an element for scaling purpose
     *
     * @param pane targeted element
     */
    public void setPaddingRatio(Pane pane) {
        Insets defaultPadding = pane.getPadding();
        double defaultWidth = GuiManager.getInstance().getDefaultWidth();
        GuiManager.getInstance().getStage().widthProperty().addListener((o, oldWidth, newWidth) -> resizePadding(pane, newWidth.doubleValue(), defaultWidth, defaultPadding));
    }

    /**
     * Determines a ratio for updating the padding
     *
     * @param defaultPadding old values of padding
     * @param pane targeted element
     * @param newWidth new size of the element
     * @param defaultWidth old size of the element
     */
    private void resizePadding(Pane pane, double newWidth, double defaultWidth, Insets defaultPadding) {
        double widthRatio = newWidth / defaultWidth;
        setPadding(pane, defaultPadding, widthRatio);
    }


    /**
     * Displays a reconnection screen with all the needed information
     *
     * @param isReconnecting {@code true} if a player is trying to reconnect to a game
     */
    public void showReconnection(boolean isReconnecting) {
        if (isReconnecting) {
            mainGrid.add(loadReconnectionLayer(), 0, 0, mainGrid.getColumnCount(), mainGrid.getRowCount());
        } else {
            if (reconnectionLayer != null) {
                mainGrid.getChildren().remove(reconnectionLayer);
            }
        }
    }


    /**
     * Returns a layout fot the reconnection
     *
     * @return the layout
     */
    private BorderPane loadReconnectionLayer() {
        return (reconnectionLayer == null) ? createReconnectionLayer() : reconnectionLayer;
    }


    /**
     * Creates a layout in case of a reconnection
     *
     * @return the layout
     */
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

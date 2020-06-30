package it.polimi.ingsw.mvc.view.gui.fxmlcontrollers;

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
     * Adds the defined number of columns to a given grid pane
     *
     * @param grid     targeted grid pane
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
     * Adds the defined number of rows to a given grid pane
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
     * Getter for the class of the fxml controller
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
     * @param node the node where set the font ratio
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
     * Adds a listener that corrects the size of the text in a Labeled node when the window is rescaled
     *
     * @param node targeted element
     * @param defaultWidth default size of the stage with the scene containing the node
     * @param defaultPadding padding of the text
     */
    private void setFontSizeLabeled(Labeled node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }

    /**
     * Adds a listener that corrects the size of the text in a TextInputControl node when the window is rescaled
     *
     * @param node targeted element
     * @param defaultWidth default size of the stage with the scene containing the node
     * @param defaultPadding padding of the text
     */
    private void setFontSizeText(TextInputControl node, double defaultWidth, Insets defaultPadding) {
        setResizeEvent(node, defaultWidth, defaultPadding, node.getFont());
    }


    /**
     * Given the default value of padding and font of a Control node creates
     * the event of rescaling
     *
     * @param node targeted element
     * @param defaultWidth default size of the stage with the scene containing the node
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
     * Changes the size of the text and fixes the padding accordingly
     *
     * @param node targeted Control node
     * @param defaultWidth default size of the stage with the scene containing the node
     * @param newWidth new size of the stage with the scene containing the node
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
     * Adds a listener that corrects the size of the padding in a Region node when the window is rescaled
     *
     * @param node targeted element
     * @param defaultPadding default value of padding
     * @param ratio ratio new stage width / default stage width
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
     * Adds a listener that corrects the size of the padding in a Pane node when the window is rescaled
     *
     * @param pane targeted element
     */
    public void setPaddingRatio(Pane pane) {
        Insets defaultPadding = pane.getPadding();
        double defaultWidth = GuiManager.getInstance().getDefaultWidth();
        GuiManager.getInstance().getStage().widthProperty().addListener((o, oldWidth, newWidth) -> resizePadding(pane, newWidth.doubleValue(), defaultWidth, defaultPadding));
    }

    /**
     * Calculates the ratio new stage width / default stage width and
     * adds a listener that corrects the size of the padding in a Region node when the window is rescaled
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
     * Displays the reconnection layer on screen
     *
     * @param isReconnecting {@code true} if a player is trying to reconnect to a game
     */
    public void showReconnection(boolean isReconnecting) {
        if (isReconnecting)
            mainGrid.add(loadReconnectionLayer(), 0, 0, mainGrid.getColumnCount(), mainGrid.getRowCount());
        else {
            if (reconnectionLayer != null)
                mainGrid.getChildren().remove(reconnectionLayer);
        }
    }


    /**
     * Gets the Singleton instance of the reconnection layer
     *
     * @return layer as borderPane
     */
    private BorderPane loadReconnectionLayer() {
        return (reconnectionLayer == null) ? createReconnectionLayer() : reconnectionLayer;
    }


    /**
     * Creates the reconnection layer
     *
     * @return layer as borderPane
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

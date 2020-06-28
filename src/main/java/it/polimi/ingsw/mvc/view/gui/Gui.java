package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlcontrollers.TitleController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Glu is trash. u do dis.
 */
public class Gui extends Application {

    private static Gui instance = null;
    private static Stage stage;

    /**
     * Creates a unique instance of Gui
     *
     * @return the {@link Gui gui} instance
     */
    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    /**
     * Getter for the stage
     *
     * @return the current stage
     */
    protected static Stage getStage() {
        return stage;
    }


    /**
     * Sets the stage through a given parameter
     *
     * @param passedStage the stage
     */
    protected static void setStage(Stage passedStage) {stage=passedStage;}


    /**
     * Initializes all elements needed to start a game
     *
     * @param stage the main stage
     */
    @Override
    public void start(Stage stage) {
        setStage(stage);
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("/css/connection.css");
        stage.setScene(scene);
        Platform.runLater(() -> bindScene(stage.getScene()));
        GuiManager.setLayout(stage.getScene(), GuiManager.getFxmlPath(TitleController.class));
        setWindowIcon();
        stage.setTitle("Santorini");
        stage.show();
        GuiManager.getInstance();
        GuiManager.setDefaultWidth(stage.getWidth());
    }

    /**
     * Sets a customize icon for the mouse in a given scene
     *
     * @param scene targeted scene
     */
    public void setMouse(Scene scene) {
        try {
            scene.setCursor(new ImageCursor(new Image(String.valueOf(Gui.class.getClassLoader().getResource("texture2D_sorted/Misti/godpower_hand.png")))));
        } catch (Exception e) {
            GuiManager.getInstance().log.warning("[GUI] Personalized mouse set failed with error: " + e.getMessage());
        }
    }

    /**
     * Sets a customize icon for the app of the graphic user interface
     */
    private void setWindowIcon() {
        try {
            stage.getIcons().add(new Image(String.valueOf(Gui.class.getClassLoader().getResource("texture2D_sorted/app_icon.png"))));
        } catch (Exception e) {
            GuiManager.getInstance().log.warning("[GUI] Personalized icon set failed with error: " + e.getMessage());
        }
    }

    /**
     * Starts the graphic user interface, setting the main parameters
     *
     * @param ip server ip
     * @param port connection port
     * @param log parameter which stores any occurred event
     */
    public void init(String ip, int port, boolean log) {
        TitleController.setConnectionConfig(ip, port);
        GuiManager.getInstance(log);
        Application.launch(Gui.class);
    }

    /**
     * Handles the closure of the window
     */
    @Override
    public void stop() {
        GuiManager.getInstance().disconnectClient();
        System.exit(0);
    }


    /**
     * Binds the height and the width to each other for a given scene; on top of that sets a minimum size for the window
     *
     * @param scene targeted scene
     */
    public void bindScene(Scene scene) {
        double h = scene.getHeight();
        double w = scene.getWidth();
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > 398.4) { // min screen width
                stage.setWidth(newVal.doubleValue() * w / h);
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            }
        });
        stage.minWidthProperty().bind(new SimpleDoubleProperty(591.9));
        stage.minHeightProperty().bind(new SimpleDoubleProperty(398.4));
        stage.maxWidthProperty().bind(stage.heightProperty().multiply(w / h));
    }

}

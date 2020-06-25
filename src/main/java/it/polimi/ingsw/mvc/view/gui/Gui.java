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


public class Gui extends Application {

    private static Gui instance = null;
    private static Stage stage;


    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    protected static Stage getStage() {
        return stage;
    }

    protected static void setStage(Stage passedStage) {stage=passedStage;}

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

    public void setMouse(Scene scene) {
        try {
            scene.setCursor(new ImageCursor(new Image(String.valueOf(Gui.class.getClassLoader().getResource("texture2D_sorted/Misti/godpower_hand.png")))));
        } catch (Exception e) {
            GuiManager.getInstance().log.warning("[GUI] Personalized mouse set failed with error: " + e.getMessage());
        }
    }

    private void setWindowIcon() {
        try {
            stage.getIcons().add(new Image(String.valueOf(Gui.class.getClassLoader().getResource("texture2D_sorted/app_icon.png"))));
        } catch (Exception e) {
            GuiManager.getInstance().log.warning("[GUI] Personalized icon set failed with error: " + e.getMessage());
        }
    }

    public void init(String ip, int port, boolean log) {
        TitleController.setConnectionConfig(ip, port);
        GuiManager.getInstance(log);
        Application.launch(Gui.class);
    }

    @Override
    public void stop() {
        GuiManager.getInstance().disconnectClient();
        System.exit(0);
    }

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

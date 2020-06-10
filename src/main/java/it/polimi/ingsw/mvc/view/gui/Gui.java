package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.BoardController;
import it.polimi.ingsw.mvc.view.gui.fxmlControllers.TitleController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Gui extends Application {

    private static Gui instance = null;
    private static Stage stage;


    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    @Override
    public void start(Stage stage) {
        Gui.stage = stage;
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("/css/connection.css");
        stage.setScene(scene);
        //GuiManager.setLayout(stage.getScene(), GuiManager.getFxmlPath(TitleController.class)); // start screen
        Platform.runLater(() -> bindScene(stage.getScene()));
        GuiManager.setLayout(stage.getScene(), GuiManager.getFxmlPath(TitleController.class));
        //GuiManager.setLayout(stage.getScene(), GuiManager.getFxmlPath(BoardController.class)); // start screen
        stage.show();
        GuiManager.getInstance().setDefaultWidth(stage.getWidth());
    }

    protected static Stage getStage() {
        return stage;
    }

    public void init(String ip, int port) {
        TitleController.setConnectionConfig(ip, port);
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
        stage.heightProperty().addListener((obs, oldVal, newVal) -> stage.setWidth(newVal.doubleValue()*w/h));
        stage.minWidthProperty().bind(stage.heightProperty().multiply(w/h));
        stage.maxWidthProperty().bind(stage.heightProperty().multiply(w/h));
    }

}

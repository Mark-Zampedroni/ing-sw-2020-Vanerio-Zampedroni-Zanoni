package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.GodSelectionController;
import it.polimi.ingsw.mvc.view.gui.fxmlControllers.TitleController;
import it.polimi.ingsw.utility.enumerations.GuiWindow;
import javafx.application.Application;
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
        GuiManager.setLayout(stage.getScene(), GuiWindow.getFxmlPath(TitleController.class)); // start screen
        //GuiManager.setLayout(stage.getScene(), GuiWindow.getFxmlPath(GodSelectionController.class));
        stage.show();

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
        stage.minWidthProperty().bind(scene.heightProperty().multiply(2));
        stage.minHeightProperty().bind(scene.widthProperty().divide(2));
    }

}

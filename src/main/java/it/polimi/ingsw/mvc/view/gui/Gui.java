package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.TitleController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Gui extends Application {

    private static Gui instance = null;
    private static Stage stage;


    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setScene(new Scene(new Pane()));

        GuiManager.setLayout(stage.getScene(), "/fxmlFiles/Title.fxml");
        //GuiManager.setLayout(stage.getScene(), "/fxmlFiles/GodSelection.fxml");
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

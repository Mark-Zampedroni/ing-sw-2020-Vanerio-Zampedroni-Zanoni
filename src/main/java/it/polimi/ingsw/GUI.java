package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.gui.objects3D.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.obj.WorkerObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardGrid;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.enumerations.Action;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class GUI extends Application {


    private final String BACKGROUND = "/texture/background_color.png";

    private static final float WIDTH = 840;
    private static final float HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Gruppo oggetti che compongono la board (fissi + tutti le torri invisibili)
        BoardObj board = new BoardObj();
        //Griglia della board
        BoardGrid grid = new BoardGrid();
        grid.setVisible(false);
        //Animazioni sulla board (movimento, build, select, add)
        // Gruppo con tutti gli oggetti esistenti
        Group objects = new Group();
        WorkerObj worker;
        objects.getChildren().addAll(
                board,
                grid,
                worker = new WorkerObj(new BoardCoords3D(4,0,0))
        );



        board.getTile(0,0).increaseHeight();
        board.getTile(1,0).increaseHeight();
        board.getTile(1,0).increaseHeight();
        board.getTile(1,0).addEffect(Action.MOVE);
        board.getTile(0,0).addEffect(Action.MOVE);
        board.getTile(2,0).addEffect(Action.BUILD);
        board.getTile(0,4).grabWorker(worker);
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).addEffect(Action.SELECT_WORKER);
        board.getTile(0,2).increaseHeight();
        board.getTile(0,2).increaseHeight();
        board.getTile(0,2).increaseHeight();
        board.getTile(2,2).increaseHeight();
        board.getTile(2,2).placeDome();

        SubScene scene = new BoardScene(objects,board,WIDTH,HEIGHT);
        new BoardCamera(scene);
        //scene.setCursor(Cursor.OPEN_HAND); // si può cambiare il cursore

        // Evento! Movimento worker sul perimetro + scia di effetti
        worker.setOnMouseClicked(event -> {
            BoardCoords3D newCoords = new BoardCoords3D(0,0,0);
            board.getTile(worker.getCoords().getValueX(),worker.getCoords().getValueY()).addEffect(Action.MOVE);
            if(worker.getCoords().getValueX() == 4 && worker.getCoords().getValueY() != 4) { newCoords = new BoardCoords3D(worker.getCoords().getValueX(),worker.getCoords().getValueY()+1,0); }
            else if(worker.getCoords().getValueY() == 4 && worker.getCoords().getValueX() != 0) { newCoords = new BoardCoords3D(worker.getCoords().getValueX()-1,worker.getCoords().getValueY(),0); }
            else if(worker.getCoords().getValueX() == 0 && worker.getCoords().getValueY() != 0) { newCoords = new BoardCoords3D(worker.getCoords().getValueX(),worker.getCoords().getValueY()-1,0); }
            else if(worker.getCoords().getValueY() == 0 && worker.getCoords().getValueX() != 4) { newCoords = new BoardCoords3D(worker.getCoords().getValueX()+1,worker.getCoords().getValueY(),0); }
            board.getTile(newCoords.getValueX(),newCoords.getValueY()).grabWorker(worker);
        });

        // 2D
        FXMLLoader loader = new FXMLLoader();
        BorderPane pane = loader.load(getClass().getResource("/fxmlFiles/sidebar.fxml"));
        BorderPane loadingPane = loader.load(getClass().getResource("/fxmlFiles/loading.fxml"));

        SubScene sideScene = new SubScene(pane, 200, 700);
        BorderPane generalPane = new BorderPane();
        BorderPane insidePane = new BorderPane();


        generalPane.setLeft(insidePane);
        generalPane.setRight(sideScene);
        generalPane.setCenter(loadingPane);
        loadingPane.setVisible(true);
        Timer timer = new Timer ();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadingPane.setVisible(false);
            }
        }, 8000);

        insidePane.setLeft(scene); //prima center


        scene.setManaged(false);

        sideScene.heightProperty().bind(generalPane.heightProperty());
        sideScene.widthProperty().bind(generalPane.widthProperty().multiply(0.22));
        scene.heightProperty().bind((generalPane.heightProperty()));
        scene.widthProperty().bind(generalPane.widthProperty().multiply(0.78));

        Scene scene1 = new Scene(generalPane);

        //Timer timer = new Timer();
        //timer.wait(5000);

        /*
        BorderPane pane = new BorderPane();

        pane.setCenter(scene);

        /*Button button = new Button("Show lines");
        CheckBox checkBox = new CheckBox("Line");
        ToolBar toolBar = new ToolBar(button, checkBox); //button, checkBox
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setMinWidth(200);
        pane.setRight(toolBar);
        pane.setPrefSize(600,600);*/
        /*
        button.setOnMouseClicked(event -> {
                grid.switchVisibility();
                button.setText(grid.visibleProperty().getValue() ? "Hide lines" : "Show lines");
        });
        */
        // Crea una finestra e visualizza la schermata
        primaryStage.setWidth(1040);
        primaryStage.setHeight(700);
        primaryStage.setTitle("Santorini test gioco 3D");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }



}

package it.polimi.ingsw;

import it.polimi.ingsw.objects3D.animation.ActionAnimation;
import it.polimi.ingsw.objects3D.obj.BoardObj;
import it.polimi.ingsw.objects3D.obj.WorkerObj;
import it.polimi.ingsw.objects3D.utils.BoardCamera;
import it.polimi.ingsw.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.objects3D.utils.BoardGrid;
import it.polimi.ingsw.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.enumerations.Action;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application {


    private final String BACKGROUND = "/texture/background_color.png";

    private static final float WIDTH = 1000;
    private static final float HEIGHT = 600;

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
        BorderPane pane = new BorderPane();
        pane.setCenter(scene);
        Button button = new Button("Show lines");
        CheckBox checkBox = new CheckBox("Line");
        ToolBar toolBar = new ToolBar(button, checkBox); //button, checkBox
        toolBar.setOrientation(Orientation.VERTICAL);
        pane.setRight(toolBar);
        pane.setPrefSize(300,300);
        Scene scene1 = new Scene(pane);

        button.setOnMouseClicked(event -> {
                grid.switchVisibility();
                button.setText(grid.visibleProperty().getValue() ? "Hide lines" : "Show lines");
        });

        // Crea una finestra e visualizza la schermata
        primaryStage.setTitle("Santorini test gioco 3D");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }



}
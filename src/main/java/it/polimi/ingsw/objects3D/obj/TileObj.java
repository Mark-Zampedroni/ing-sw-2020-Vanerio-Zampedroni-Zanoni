package it.polimi.ingsw.objects3D.obj;

import it.polimi.ingsw.objects3D.TrackedGroup;
import it.polimi.ingsw.objects3D.animation.ActionAnimation;
import it.polimi.ingsw.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.objects3D.utils.NodeOperation;
import it.polimi.ingsw.utility.enumerations.Action;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

public class TileObj extends TrackedGroup {

    private final TowerObj tower;
    private final BoardObj board;
    private Shape3D tileSquare;
    private ActionAnimation animation;
    private boolean VISIBLE = false;
    private int x,y;

    public TileObj(BoardObj board, int x, int y) throws Exception {
        super(-12.3,12.6,0.3,-3.6,-7.3,-9.7);

        this.x = x;
        this.y = y;
        this.board = board;

        tower = new TowerObj(new BoardCoords3D(x,y,0));
        board.getChildren().add(tower);

        convertShape(new Box(5.7, 0.1, 5.7));

        setCoords(new BoardCoords3D(x,y,0));

        /*
        setOnMouseClicked(event -> {
            box.setDepth(box.getDepth()+0.1);
            box.setWidth(box.getWidth()+0.1);
            System.out.println(box.getWidth());
        });*/
    }

    public void increaseHeight() {
        tower.increaseHeight();
        setCoords(new BoardCoords3D(x,y,tower.getHeight()));
        if(tower.getHeight() == 3) {
            convertShape(new Cylinder(2.6,0.35));
        }
    }

    public int getHeight() {
        return tower.getHeight();
    }

    public void placeDome() {
        if(tileSquare != null) {
            tower.placeDome();
            getChildren().remove(tileSquare);
            tileSquare = null;
        }
    }

    public void addEffect(Action action) {
        removeEffect(); // replaces effect
        animation = new ActionAnimation(action, new BoardCoords3D(x,y,tower.getHeight()));
        board.getChildren().add(animation);
        addClickEvent(animation);
    }

    public void grabWorker(WorkerObj worker) {
        removeEffect(); // <------------------------------------------------- May remove later for SELECT_WORKER
        worker.setCoords(new BoardCoords3D(x,y,tower.getHeight()));
    }

    public void removeEffect() {
        if(animation != null) {
            board.getChildren().remove(animation);
            animation = null;
        }
    }

    private void printRed(Shape3D object) {
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseColor(Color.RED);
        object.setMaterial(texture);
    }

    private void convertShape(Shape3D object) { //
        getChildren().remove(tileSquare);
        tileSquare = object;
        printRed(tileSquare);
        getChildren().add(tileSquare);
        if(!VISIBLE) { object.setOpacity(0); }// Invisible but keeps mouse events
        addClickEvent(tileSquare);
    }

    private void addClickEvent(Node object) {
        object.setOnMouseClicked(event -> System.out.println("Clicked on "+x+","+y));
    }
}

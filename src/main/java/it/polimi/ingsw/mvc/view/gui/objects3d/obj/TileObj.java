package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.animation.ActionAnimation;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.ObservableTileEvent;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;

import java.io.IOException;

/**
 * 3D object representing a {@link it.polimi.ingsw.mvc.model.map.Tile tile} on the 3D SubScene
 */
public class TileObj extends TrackedGroup {

    private final TowerObj tower;
    private Shape3D tileSquare;
    private ActionAnimation animation;
    private static final boolean VISIBLE = false;
    private int x;
    private int y;
    private ObservableTileEvent eventResponse;
    private WorkerObj worker;

    /**
     * Constructor of the tile
     *
     * @param board         3D object containing all the tiles
     * @param x             coordinate of the tile on the X axis
     * @param y             coordinate of the tile on the Y axis
     * @param eventResponse observable triggered when the tile or an element on the tile is clicked
     * @throws IOException if any of the fxml files can't be read
     */
    public TileObj(BoardObj board, int x, int y, ObservableTileEvent eventResponse) throws IOException {
        super(-12.3, 12.6, 0.3, -3.6, -7.3, -9.7);

        this.eventResponse = eventResponse;
        this.x = x;
        this.y = y;

        tower = new TowerObj(new BoardCoordinates3D(x, y, 0));
        board.getChildren().add(tower);

        convertShape(new Box(5.7, 0.1, 5.7));

        setCoordinates(new BoardCoordinates3D(x, y, 0));
    }

    /**
     * Increases the height of the tower on the tile by 1
     */
    public void increaseHeight() {
        tower.increaseHeight();
        setCoordinates(new BoardCoordinates3D(x, y, tower.getHeight()));
        if (tower.getHeight() == 3)
            convertShape(new Cylinder(2.6, 0.35));
    }

    /**
     * Gets the height of the tower on the tile
     *
     * @return height of the tower on the tile
     */
    public int getHeight() {
        return tower.getHeight();
    }

    /**
     * Places a dome at the top of the tower on the tile
     */
    public void placeDome() {
        if (tileSquare != null) {
            tower.placeDome();
            getChildren().remove(tileSquare);
            tileSquare = null;
        }
    }

    /**
     * Adds an animation effect at the top of the tower on the tile
     *
     * @param action action corresponding to the effect
     * @return animation created
     */
    public ActionAnimation addEffect(Action action) {
        removeEffect();
        animation = new ActionAnimation(action, new BoardCoordinates3D(x, y, tower.getHeight()));
        addClickEvent(animation);
        return animation;
    }

    /**
     * If exists, removes the animation effect on the tile
     */
    public void removeEffect() {
        if (animation != null) {
            animation = null;
        }
    }

    /**
     * Adds a worker at the top of the tower on the tile
     *
     * @param objects group of the workers
     * @param color   color of the worker to add
     * @throws IOException if any of the fxml files can't be read
     */
    public void addWorker(Group objects, Colors color) throws IOException {
        if (worker == null) {
            worker = new WorkerObj(new BoardCoordinates3D(x, y, tower.getHeight()), color);
            objects.getChildren().add(worker);
            addClickEvent(worker);
        }
    }

    /**
     * If exists, gets the worker on the tile, otherwise returns null.
     * Removes the worker from the tile when called
     *
     * @return the worker on the tile
     */
    public WorkerObj getWorker() {
        WorkerObj temp = worker;
        worker = null;
        return temp;
    }

    /**
     * Moves a worker on the tile
     *
     * @param worker worker to move on the tile
     */
    public void setWorker(WorkerObj worker) {
        removeEffect();
        this.worker = worker;
        worker.setCoordinates(new BoardCoordinates3D(x, y, tower.getHeight()));
        addClickEvent(worker);
    }

    /**
     * Deletes the worker on the tile
     *
     * @param objects group of the workers
     */
    public void deleteWorker(Group objects) {
        if (worker != null) {
            objects.getChildren().remove(worker);
            worker = null;
        }
    }

    /**
     * Colors the tile of red and makes it visible (DEBUGGING PURPOSES)
     *
     * @param object 3D shape to paint of red
     */
    private void printRed(Shape3D object) {
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseColor(Color.RED);
        object.setMaterial(texture);
    }

    /**
     * Changes the shape of the object tile depending on the tower height.
     * At heights 0, 1 and 2 it's a square.
     * At height 3 it's a circle
     *
     * @param object 3D shape to deform
     */
    private void convertShape(Shape3D object) {
        getChildren().remove(tileSquare);
        tileSquare = object;
        printRed(tileSquare);
        getChildren().add(tileSquare);
        if (!VISIBLE) // Default is !false -> true, may be false when debugging
            object.setOpacity(0);
        addClickEvent(tileSquare);
    }

    /**
     * Adds an event on a Node on mouse click
     *
     * @param object node to which is added the event
     */
    protected void addClickEvent(Node object) {
        object.setOnMouseClicked(event -> {
            eventResponse.setCallerCoordinates(x, y);
            eventResponse.run();
        });
    }
}

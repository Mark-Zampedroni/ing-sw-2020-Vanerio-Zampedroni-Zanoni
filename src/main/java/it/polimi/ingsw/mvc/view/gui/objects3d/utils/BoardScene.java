package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

import it.polimi.ingsw.mvc.view.gui.objects3d.animation.ActionAnimation;
import it.polimi.ingsw.mvc.view.gui.objects3d.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3d.obj.TileObj;
import it.polimi.ingsw.mvc.view.gui.objects3d.obj.WorkerObj;
import it.polimi.ingsw.utility.dto.DtoBoard;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.dto.DtoWorker;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * SubScene containing 3D objects; shown on the BoardController scene
 */
public class BoardScene extends SubScene {

    private static BoardScene boardScene;
    private static ObservableTileEvent tileEvent;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty prevAngleX = new SimpleDoubleProperty(0);
    private final DoubleProperty prevAngleY = new SimpleDoubleProperty(0);
    private final DoubleProperty prevPosX = new SimpleDoubleProperty(0);
    private final DoubleProperty prevPosY = new SimpleDoubleProperty(0);
    private final Object boardLock = new Object();
    private final Map<String, Colors> players;
    private static final double X_ROTATION_SPEED = 0.35;
    private static final double Y_ROTATION_SPEED = 0.3;
    private final Logger log;
    private BoardObj board;
    private Map<Action, Group> animations;
    private DtoSession localSession;
    private Group objects;
    private Group workersObj = new Group();
    private Group preWorkers = new Group();

    /**
     * Constructor
     *
     * @param sceneObjects group containing all the 3D objects
     * @param players      map of players names to their colors (needed to load the {@link WorkerObj workers})
     * @param width        width of the SubScene
     * @param height       height of the SubScene
     * @param log          logger where the events will be recorded
     * @throws IOException if one or more of the fxml files used during the initialization (board and scenario objects) can't be read
     */
    public BoardScene(Group sceneObjects, Map<String, Colors> players, float width, float height, Logger log) throws IOException {
        super(sceneObjects, width, height, true, SceneAntialiasing.DISABLED);
        objects = sceneObjects;
        this.players = players;
        this.log = log;
        preloadBoard();
        setFill(Color.web("#47cbf4")); // Sky color
        addLight();
        initRotation(board);
    }

    /**
     * Creates the board object, loading the models used
     *
     * @param players map of players names to their colors (needed to load the {@link WorkerObj workers})
     * @param log     logger where the events will be recorded
     * @throws IOException if any of the fxml files used can't be read
     */
    public static void startBoardLoad(Map<String, Colors> players, Logger log) throws IOException {
        tileEvent = new ObservableTileEvent();
        boardScene = new BoardScene(new Group(), players, 840, 700, log);
    }

    /**
     * Gets the BoardScene previously loaded
     *
     * @return the BoardScene with all its static models loaded (board, background, buildings...)
     */
    public static BoardScene getBoardLoadedScene() {
        return boardScene;
    }

    /**
     * Gets the shared ObservableTileEvent
     *
     * @return the tileEvent
     */
    public static ObservableTileEvent getTileEvent() {
        return tileEvent;
    }

    /**
     * Clears the static variables of the SubScene
     */
    private static void clearStatic() {
        tileEvent = null;
        boardScene = null;
    }

    /**
     * Adds a white light to the SubScene (default light is darker)
     */
    private void addLight() {
        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);
        objects.getChildren().addAll(light);
    }

    /**
     * Sets the rotation event on mouse drag
     *
     * @param board the node to rotate
     */
    private void initRotation(Node board) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        objects.getTransforms().addAll(xRotate, yRotate);
        yRotate.setPivotX(board.getTranslateX());
        yRotate.setPivotZ(board.getTranslateZ());

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        angleX.set(37); // Starting angle

        setOnMousePressed(event -> {
            prevPosX.setValue(event.getSceneX());
            prevPosY.setValue(event.getSceneY());
            prevAngleY.setValue(angleY.getValue());
            prevAngleX.setValue(angleX.getValue());
        });

        setOnMouseDragged(event -> {
            double newAngleX = prevAngleX.getValue() - (prevPosY.getValue() - event.getSceneY()) * Y_ROTATION_SPEED;
            if (newAngleX >= 36.5 && newAngleX <= 90) {
                angleX.set(newAngleX);
            }

            if (Math.abs(prevAngleY.getValue()) > 10000000) {
                angleY.set(0);
            }
            angleY.set(prevAngleY.getValue() - (prevPosX.getValue() - event.getSceneX()) * X_ROTATION_SPEED);
        });
    }

    /**
     * Loads the subScene before showing it
     *
     * @throws IOException if any of the fxml files used can't be read
     */
    private void preloadBoard() throws IOException {
        board = new BoardObj(tileEvent);
        assignGroups();
        preLoadWorkers(players);
    }

    /**
     * Adds each node type to the correct group
     */
    private void assignGroups() {
        objects.getChildren().add(workersObj);
        objects.getChildren().add(preWorkers);
        preWorkers.setVisible(false);
        objects.getChildren().add(board);
    }

    /**
     * Loads the workers before adding them to the board
     *
     * @param players map of each player's name to its color
     */
    private void preLoadWorkers(Map<String, Colors> players) {
        if (players == null) {
            return;
        }
        players.values().forEach(v -> preWorkers.getChildren().addAll(
                board.createWorker(v),
                board.createWorker(v)));
    }

    /**
     * Loads all the animations on each possible position for the actions in the map.
     * Creates a map and saves each animation group (for action)
     *
     * @param possibleActions map of possible actions to their positions candidates
     */
    private void addAnimations(Map<Action, List<DtoPosition>> possibleActions) {
        animations = new HashMap<>();
        for (Map.Entry<Action, List<DtoPosition>> e : possibleActions.entrySet()) {
            Group temp = new Group();
            temp.setVisible(false);
            e.getValue().forEach(p -> temp.getChildren().add(board.getTile(p.getX(), p.getY()).addEffect(e.getKey())));
            board.getChildren().add(temp);
            animations.put(e.getKey(), temp);
        }
    }

    /**
     * Clears all the animations nodes (and groups) previously added
     */
    private void clearAnimations() {
        if (animations != null) {
            animations.values().forEach(group -> board.getChildren().remove(group));
            animations = null;
        }
    }

    /**
     * Shows the animations (previously loaded) in the group of a given action
     *
     * @param action action of the group that will be shown
     */
    public void showAnimations(Action action) {
        animations.keySet().forEach(k -> animations.get(k).setVisible(k == action));
    }

    /**
     * Updates the board elements; loads the animations of the actions given
     *
     * @param possibleActions map of the actions to their positions candidates
     * @param session         DtoSession containing the updated board to show
     */
    public void turnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session) {
        updateBoard(session);
        addAnimations(possibleActions);
    }

    /**
     * Updates the board elements
     *
     * @param session DtoSession containing the updated board to show
     */
    public void updateBoard(DtoSession session) {
        synchronized (boardLock) {
            updateWorkers(session.getWorkers());
            updateBuildings(session.getBoard());
        }
        localSession = session;
    }

    /**
     * Updates the {@link WorkerObj workers} positions
     *
     * @param workers updated list of workers
     */
    private void updateWorkers(List<DtoWorker> workers) {
        if (workers == null || localSession == null) {
            return;
        }
        Map<DtoPosition, String> localPositions = new HashMap<>();
        localSession.getWorkers().forEach(w -> localPositions.put(w.getPosition(), w.getMasterUsername()));

        if (localSession.getWorkers().size() < workers.size()) { // A worker is added
            workers.stream().filter(w -> localPositions.keySet().stream().noneMatch(k -> k.isSameAs(w.getPosition()))).forEach(this::addWorker);
        } else {
            moveWorkers(workers);
        }
    }

    /**
     * Updates the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.TowerObj towers} heights
     *
     * @param newBoard updated board
     */
    private void updateBuildings(DtoBoard newBoard) {
        if (!(newBoard == null || localSession == null)) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    increaseBuildingHeight(newBoard, x, y);
                    placeDome(newBoard, x, y);
                }
            }
        }
    }

    /**
     * Increases the height of the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.TowerObj tower} at the given coordinates
     *
     * @param newBoard updated board
     * @param x        coordinate on the X axis
     * @param y        coordinate on the Y axis
     */
    private void increaseBuildingHeight(DtoBoard newBoard, int x, int y) {
        if (newBoard.getTile(x, y).getHeight() > localSession.getBoard().getTile(x, y).getHeight()) {
            board.getTile(x, y).increaseHeight();
            WorkerObj temp = board.getTile(x, y).getWorker();
            if (temp != null) {
                board.getTile(x, y).setWorker(temp);
            }
        }
    }

    /**
     * Places a dome on the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.TowerObj tower} at the given coordinates
     *
     * @param newBoard updated board
     * @param x        coordinate on the X axis
     * @param y        coordinate on the Y axis
     */
    private void placeDome(DtoBoard newBoard, int x, int y) {
        if (newBoard.getTile(x, y).hasDome() && !localSession.getBoard().getTile(x, y).hasDome()) {
            board.getTile(x, y).placeDome();
        }
    }

    /**
     * Copies a DtoWorker and adds a {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.WorkerObj worker} of the same
     * color at the same position
     *
     * @param w DtoWorker to add as WorkerObj
     */
    private void addWorker(DtoWorker w) {
        try {
            WorkerObj newWorker = preWorkers.getChildren().stream()
                    .map(WorkerObj.class::cast)
                    .filter(n -> n.getColor() == players.get(w.getMasterUsername()))
                    .findFirst()
                    .orElse(new WorkerObj(new BoardCoordinates3D(-1, -1, 0), players.get(w.getMasterUsername())));
            workersObj.getChildren().add(newWorker);
            board.getTile(w.getPosition().getX(), w.getPosition().getY()).setWorker(newWorker);
            newWorker.resetRotation();
        } catch (Exception e) {
            log.severe("[BOARD_SCENE] Couldn't load a worker");
        }
    }

    /**
     * Updates the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.WorkerObj workers} positions
     *
     * @param workers updated workers
     */
    private void moveWorkers(List<DtoWorker> workers) {
        Map<WorkerObj, TileObj> m = buildMovementMap(workers);
        m.forEach((w, t) -> t.setWorker(w));
    }

    /**
     * Creates a map with as key the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.WorkerObj workers} that moved on the previous turn, as value
     * the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.TileObj} on where they moved
     *
     * @param workers updated workers
     * @return map of moved workers to their new position
     */
    private Map<WorkerObj, TileObj> buildMovementMap(List<DtoWorker> workers) {
        List<DtoWorker> movedWorkers = workers.stream().filter(wn ->
                localSession.getWorkers().stream().noneMatch(wo -> wo.getPosition().isSameAs(wn.getPosition()) && wo.getMasterUsername().equals(wn.getMasterUsername()))).collect(Collectors.toList());
        List<DtoWorker> oldMovedWorkers = localSession.getWorkers().stream().filter(wo ->
                workers.stream().noneMatch(wn -> wn.getPosition().isSameAs(wo.getPosition()) && wn.getMasterUsername().equals(wo.getMasterUsername()))).collect(Collectors.toList());

        Map<WorkerObj, TileObj> temp = new HashMap<>();
        movedWorkers.forEach(wn ->
                oldMovedWorkers.stream().filter(wo -> wo.getMasterUsername().equals(wn.getMasterUsername()))
                        .forEach(wo -> temp.put(board.getTile(wo.getPosition().getX(), wo.getPosition().getY()).getWorker(), board.getTile(wn.getPosition().getX(), wn.getPosition().getY()))));
        return temp;
    }

    /**
     * Removes the {@link it.polimi.ingsw.mvc.view.gui.objects3d.obj.WorkerObj workers} of the player who lost
     *
     * @param playerName name of the player who lost
     */
    public void notifyLose(String playerName) {
        localSession.getWorkers().stream()
                .filter(w -> w.getMasterUsername().equals(playerName))
                .map(DtoWorker::getPosition)
                .forEach(p -> board.getTile(p.getX(), p.getY()).deleteWorker(workersObj));
    }

    /**
     * Clears the SubScene
     */
    public void clear() {
        clearAnimations();
        ActionAnimation.clear();
        clearStatic();
    }

}

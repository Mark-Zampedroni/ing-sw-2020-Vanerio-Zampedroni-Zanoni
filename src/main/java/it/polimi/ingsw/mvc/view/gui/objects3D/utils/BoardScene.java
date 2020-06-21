package it.polimi.ingsw.mvc.view.gui.objects3D.utils;

import it.polimi.ingsw.mvc.view.gui.objects3D.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.obj.TileObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.obj.WorkerObj;
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
    private final double X_ROTATION_SPEED = 0.35;
    private final double Y_ROTATION_SPEED = 0.3;
    private final Logger LOG;
    private BoardObj board;
    private Map<Action, Group> animations;
    private DtoSession localSession;
    private Group objects;
    private Group workersObj = new Group();
    private Group preWorkers = new Group();

    public BoardScene(Group sceneObjects, Map<String, Colors> players, float width, float height, Logger log) throws IOException {
        super(sceneObjects, width, height, true, SceneAntialiasing.DISABLED);
        objects = sceneObjects;
        this.players = players;
        this.LOG = log;
        preloadBoard();
        setFill(Color.web("#47cbf4")); // Sky color
        addLight();
        initRotation(board);
    }

    public static void startBoardLoad(Map<String, Colors> players, Logger LOG) throws Exception {
        tileEvent = new ObservableTileEvent();
        boardScene = new BoardScene(new Group(), players, 840, 700, LOG);
    }

    public static BoardScene getBoardLoadedScene() {
        return boardScene;
    }

    public static ObservableTileEvent getTileEvent() {
        return tileEvent;
    }

    private void addLight() {
        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);
        objects.getChildren().addAll(light);
    }

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

    private void preloadBoard() throws IOException {
        board = new BoardObj(tileEvent);
        assignGroups();
        preLoadWorkers(players);
    }

    private void assignGroups() {
        objects.getChildren().add(workersObj);
        objects.getChildren().add(preWorkers);
        preWorkers.setVisible(false);
        objects.getChildren().add(board);
    }

    private void preLoadWorkers(Map<String, Colors> players) {
        if (players == null) {
            return;
        }
        players.values().forEach(v -> preWorkers.getChildren().addAll(
                board.createWorker(v),
                board.createWorker(v)));
    }

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

    private void clearAnimations() {
        if (animations != null) {
            animations.values().forEach(group -> board.getChildren().remove(group));
            animations = null;
        }
    }

    public void showAnimations(Action action) {
        animations.keySet().forEach(k -> animations.get(k).setVisible(k == action));
    }

    public void turnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session) {
        updateBoard(session);
        addAnimations(possibleActions);
    }

    public void updateBoard(DtoSession session) {
        synchronized (boardLock) {
            updateWorkers(session.getWorkers());
            updateBuildings(session.getBoard());
        }
        localSession = session;
    }

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

    private void updateBuildings(DtoBoard newBoard) {
        if (newBoard == null || localSession == null) {
            return;
        }
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (newBoard.getTile(x, y).getHeight() > localSession.getBoard().getTile(x, y).getHeight()) {
                    board.getTile(x, y).increaseHeight();
                    WorkerObj temp = board.getTile(x, y).getWorker();
                    if (temp != null) {
                        board.getTile(x, y).setWorker(temp);
                    }

                }
                if (newBoard.getTile(x, y).hasDome() && !localSession.getBoard().getTile(x, y).hasDome()) {
                    board.getTile(x, y).placeDome();
                }
            }
        }
    }

    private void addWorker(DtoWorker w) {
        try {
            WorkerObj newWorker = (WorkerObj) preWorkers.getChildren().stream()
                    .filter(n -> ((WorkerObj) n).getColor() == players.get(w.getMasterUsername()))
                    .findFirst()
                    .orElse(new WorkerObj(new BoardCoords3D(-1, -1, 0), players.get(w.getMasterUsername())));
            workersObj.getChildren().add(newWorker);
            board.getTile(w.getPosition().getX(), w.getPosition().getY()).setWorker(newWorker);
            newWorker.resetRotation();
        } catch (Exception e) {
            LOG.severe("[BOARD_SCENE] Couldn't load a worker");
        }
    }

    private void moveWorkers(List<DtoWorker> workers) {
        Map<WorkerObj, TileObj> m = buildMovementMap(workers);
        m.forEach((w, t) -> t.setWorker(w));
    }

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

    public void notifyLose(String playerName) {
        localSession.getWorkers().stream()
                .filter(w -> w.getMasterUsername().equals(playerName))
                .map(DtoWorker::getPosition)
                .forEach(p -> board.getTile(p.getX(), p.getY()).deleteWorker(workersObj));
    }

    public void clear() {
        clearAnimations();
        tileEvent = null;
        boardScene = null;
    }

}

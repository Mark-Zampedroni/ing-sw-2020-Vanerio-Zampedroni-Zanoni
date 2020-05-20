package it.polimi.ingsw.mvc.view.gui.objects3D.utils;

import it.polimi.ingsw.mvc.view.gui.objects3D.TrackedGroup;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import java.io.IOException;

public class NodeOperation {

    public final static double TILES_OFFSET = 6.315;

    public static void setScale(Node node, double factor) {
        node.setScaleX(node.getScaleX() * factor);
        node.setScaleY(node.getScaleY() * factor);
        node.setScaleZ(node.getScaleZ() * factor);
    }

    public static void setTranslate(Node node, double transX, double transZ, double transY) {
        node.setTranslateX(transX);
        node.setTranslateZ(transZ);
        node.setTranslateY(transY);
    }

    public static Node getModel(String objRelativePath, String textureRelativePath) throws IOException {
        MeshView model = FXMLLoader.load(NodeOperation.class.getResource(objRelativePath));
        if(textureRelativePath != null) {
            PhongMaterial texture = new PhongMaterial();
            texture.setDiffuseMap(new Image(textureRelativePath, true));
            model.setMaterial(texture);
        }
        return model;
    }

    public static void putOnCorrectTile(TrackedGroup node, BoardCoords3D coords) {
        node.setTranslateX(node.getZeroX() + TILES_OFFSET*coords.getValueX());
        node.setTranslateZ(node.getZeroZ() - TILES_OFFSET*coords.getValueY());
        node.setFloor(coords.getValueZ());
    }

}

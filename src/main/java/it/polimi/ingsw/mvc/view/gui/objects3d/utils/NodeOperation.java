package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import java.io.IOException;

/**
 * Utility class with static methods used by the 3D objects
 */
public class NodeOperation {

    /**
     * Distance between two adjacent tiles centers
     */
    public static final double TILES_OFFSET = 6.315;

    /**
     * This class is for utility and not instantiable
     *
     * @throws NotInstantiableClass when instantiated
     */
    private NodeOperation() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * Rescales the node
     *
     * @param node   node to rescale
     * @param factor rescale factor to apply to its current size (es: 2 doubles the size, 0.5 halves it)
     */
    public static void setScale(Node node, double factor) {
        node.setScaleX(node.getScaleX() * factor);
        node.setScaleY(node.getScaleY() * factor);
        node.setScaleZ(node.getScaleZ() * factor);
    }

    /**
     * Moves a node
     *
     * @param node   node to move
     * @param transX translation on X axis
     * @param transZ translation on Z axis
     * @param transY translation on Y axis
     */
    public static void setTranslate(Node node, double transX, double transZ, double transY) {
        node.setTranslateX(transX);
        node.setTranslateZ(transZ);
        node.setTranslateY(transY);
    }

    /**
     * Loads an object (3D) from a Fxml file
     *
     * @param objRelativePath     path of the fxml containing the .obj model
     * @param textureRelativePath texture of the model
     * @return loaded model as Node
     * @throws IOException if the fxml or texture file can't be read
     */
    public static Node getModel(String objRelativePath, String textureRelativePath) throws IOException {
        MeshView model = FXMLLoader.load(NodeOperation.class.getResource(objRelativePath));
        if (textureRelativePath != null) {
            PhongMaterial texture = new PhongMaterial();
            texture.setDiffuseMap(new Image(textureRelativePath, true));
            model.setMaterial(texture);
        }
        return model;
    }

    /**
     * Gets a texture from a file
     *
     * @param textureRelativePath image file of the texture
     * @return the texture object
     */
    public static PhongMaterial getTexture(String textureRelativePath) {
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(textureRelativePath, true));
        return texture;
    }

    /**
     * Loads an object (3D) from a Fxml file
     *
     * @param objRelativePath fxml file containing the .obj model
     * @param texture         texture which was previously loaded and saved as an object
     * @return loaded model as Node
     * @throws IOException if the fxml file can't be read
     */
    public static Node getModel(String objRelativePath, PhongMaterial texture) throws IOException {
        MeshView model = FXMLLoader.load(NodeOperation.class.getResource(objRelativePath));
        model.setMaterial(texture);
        return model;
    }

    /**
     * Moves a TrackedGroup (group of 3D nodes) to the given coordinates
     *
     * @param node           group to move
     * @param newCoordinates coordinates where to move the group
     */
    public static void putOnCorrectTile(TrackedGroup node, BoardCoordinates3D newCoordinates) {
        node.setTranslateX(node.getZeroX() + TILES_OFFSET * newCoordinates.getValueX());
        node.setTranslateZ(node.getZeroZ() - TILES_OFFSET * newCoordinates.getValueY());
        node.setFloor(newCoordinates.getValueZ());
    }

}

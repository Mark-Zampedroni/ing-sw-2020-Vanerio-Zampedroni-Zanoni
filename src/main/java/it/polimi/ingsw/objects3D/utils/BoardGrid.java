package it.polimi.ingsw.objects3D.utils;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class BoardGrid extends Group {

    public BoardGrid() {
        for(double x = 0; x < 6; x++) {
            double newX = -15.5+6.4*x;
            this.getChildren().add(
                    createConnection(new Point3D(newX,0,26),new Point3D(newX,0,-6))
            );
        }
        for(double y = 0; y < 6; y++) {
            double newY = 26-6.4*y;
            this.getChildren().add(
                    createConnection(new Point3D(-15.5, 0, newY), new Point3D(16.5, 0, newY))
            );
        }
    }

    public void switchVisibility() {
        setVisible(!visibleProperty().getValue());
    }

    private Cylinder createConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(1, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseColor(Color.RED);
        line.setMaterial(texture);

        return line;
    }

}

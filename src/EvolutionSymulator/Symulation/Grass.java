package Symulation;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Grass extends AbstractWorldMapElement{

    private Rectangle representation;
    private Pane world;
    private Color representationColor = Color.DARKGREEN;

    private final Vector2d position;
    public Grass(Vector2d position,Pane world)
    {
        this.position = position;
        this.world = world;
        this.representation = new Rectangle(5,5,representationColor);
        representation.setStroke(Color.BLACK);
        world.getChildren().add(representation);
    }
    @Override
    public Vector2d getPosition()
    {
        return this.position;
    }
    public String toString()
    {
        return "*";
    }

    public void draw()
    {
        representation.setArcHeight(5);
        representation.setArcWidth(5);
        representation.setLayoutX(this.position.x);
        representation.setLayoutY(this.position.y);
    }
}

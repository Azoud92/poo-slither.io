package fr.team92.serpents.game.model;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class TwoPlayersMode implements GameMode {
    int CELL_SIZE;

    @Override
    public void drawSegments(Pane pane, GameController controller, int cellSize) {
        CELL_SIZE = cellSize;
        pane.getChildren().clear();
        for (Segment segment : controller.getGrid().values()) {

            Position pos = segment.getPosition();
            double diameter = segment.getDiameter() * CELL_SIZE;
            double x = pos.x() * CELL_SIZE + CELL_SIZE / 2.0;
            double y = pos.y() * CELL_SIZE + CELL_SIZE / 2.0;
            if (x >= 0 && x <= pane.getWidth() && y >= 0 && y <= pane.getHeight()) {
                Circle circle = new Circle(x, y, diameter / 2.0);
                if (segment.isDead()) {
                    circle.setFill(Color.ORANGE);
                } else {
                    circle.setFill(Color.RED);
                }
                if (segment.getBehavior() instanceof BurrowingSegmentBehavior)
                    circle.setFill(Color.BLUE);
                pane.getChildren().add(circle);
            }
        }
    }

}

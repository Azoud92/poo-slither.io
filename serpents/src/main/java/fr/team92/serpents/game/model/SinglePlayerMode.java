package fr.team92.serpents.game.model;

import java.util.ArrayList;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public final class SinglePlayerMode implements GameMode {
    private static int CELL_SIZE;
    private static ArrayList<Circle> snakeCircles = new ArrayList<>();

    @Override
    public void drawSegments(Pane pane, GameController controller, int cellSize) {
        pane.getChildren().removeIf(node -> node instanceof Circle);

        Segment headSegment = controller.getHumanSnake().getSegments().getFirst();
        Position headPos = headSegment.getPosition();
        CELL_SIZE = cellSize;

        for (Segment segment : controller.getGrid().values()) {
            Position pos = segment.getPosition();
            double x = (pos.x() - headPos.x()) * CELL_SIZE + pane.getWidth() / 2.0;
            double y = (pos.y() - headPos.y()) * CELL_SIZE + pane.getHeight() / 2.0;
            double radius = segment.getDiameter() * CELL_SIZE / 2.0;

            // Si une partie du segment est à un bord de la grille, on le dessine aussi
            // de l'autre côté
            boolean left = x - radius < 0;
            boolean right = x + radius > pane.getWidth();
            boolean top = y - radius < 0;
            boolean bottom = y + radius > pane.getHeight();

            if (left) {
                drawSegment(pane, segment, x + pane.getWidth(), y);
            } else if (right) {
                drawSegment(pane, segment, x - pane.getWidth(), y);
            }
            if (top) {
                drawSegment(pane, segment, x, y + pane.getHeight());
            } else if (bottom) {
                drawSegment(pane, segment, x, y - pane.getHeight());
            }

            // Si le segment est dans un coin, on le dessine aussi dans le coin opposé
            if (left && top) {
                drawSegment(pane, segment, x + pane.getWidth(), y + pane.getHeight());
            } else if (right && top) {
                drawSegment(pane, segment, x - pane.getWidth(), y + pane.getHeight());
            } else if (right && bottom) {
                drawSegment(pane, segment, x - pane.getWidth(), y - pane.getHeight());
            } else if (left && bottom) {
                drawSegment(pane, segment, x + pane.getWidth(), y - pane.getHeight());
            }

            // Ensuite, on dessine le segment à sa position actuelle
            drawSegment(pane, segment, x, y);
        }

        // ajout des cercles des serpents après les segments morts pour qu'ils soient au
        // dessus dans l'affichage
        for (Circle circle : snakeCircles) {
            pane.getChildren().add(circle);
        }
        snakeCircles.clear();
    }

    private void drawSegment(Pane pane, Segment segment, double x, double y) {
        double diameter = segment.getDiameter() * CELL_SIZE;
        Circle circle = new Circle(x, y, diameter / 2.0);
        if (segment.getBehavior() instanceof BurrowingSegmentBehavior) {
            circle.setFill(Color.BLUE);
        } else if (segment.isDead()) {
            circle.setFill(Color.ORANGE);
        }
        if (!(segment.getBehavior() instanceof BurrowingSegmentBehavior) && !segment.isDead()) {
            circle.setFill(Color.RED);
        }

        if (!segment.isDead()) {
            snakeCircles.add(circle);
        } else {
            pane.getChildren().add(circle);
        }

    }

    @Override
    public void updateScore(Text scoreText, GameController controller, Pane pane) {
        scoreText.setText("Score : " + controller.getScore());

    }

}

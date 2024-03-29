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

public final class TwoPlayersMode implements GameMode {
    private static int CELL_SIZE;
    private int score1 = 0;
    private int score2 = 0;
    ArrayList<Circle> snakeCircles = new ArrayList<>();

    @Override
    public void drawSegments(Pane pane, GameController controller, int cellSize) {
        CELL_SIZE = cellSize;
        pane.getChildren().removeIf(node -> node instanceof Circle);

        for (Segment segment : controller.getGrid().values()) {
            Position pos = segment.getPosition();
            double diameter = segment.getDiameter() * CELL_SIZE;
            double x = pos.x() * CELL_SIZE + CELL_SIZE / 2.0;
            double y = pos.y() * CELL_SIZE + CELL_SIZE / 2.0;
            if (x >= 0 && x <= pane.getWidth() && y >= 0 && y <= pane.getHeight()) {
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
        }
        // ajout des cercles des serpents après les segments morts pour qu'ils soient au
        // dessus dans l'affichage
        for (Circle circle : snakeCircles) {
            pane.getChildren().add(circle);
        }
        snakeCircles.clear();
    }

    @Override
    public void updateScore(Text scoreText, GameController controller, Pane pane) {
        try {
            score1 = controller.getPlayer1().getLength();
            score2 = controller.getPlayer2().getLength();
        } catch (IllegalStateException e) {
        }

        scoreText.setText(String.format("Joueur 1 - Score : %d\nJoueur 2 - Score : %d", score1, score2));

    }

}

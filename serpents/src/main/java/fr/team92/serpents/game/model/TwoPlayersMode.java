package fr.team92.serpents.game.model;

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

    @Override
    public void updateScore(Text scoreText, GameController controller, Pane pane) {
        int score1 = controller.getPlayer1().getLength();
        int score2 = controller.getPlayer2().getLength();

        scoreText.setText(String.format("Joueur 1 - Score : %d\nJoueur 2 - Score : %d", score1, score2));

    }

}

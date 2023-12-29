package fr.team92.serpents.game.view;

import java.util.List;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public final class GameView implements Observer {

    private final GameController controller;
    private final Pane pane;
    private static int CELL_SIZE;

    public GameView(Observable model, GameController controller, Pane pane) {
        this.pane = pane;
        this.controller = controller;
        CELL_SIZE = controller.getCellSize();

        model.addObserver(this);
        this.update();
    }

    @Override
    public void update() {
        drawSegments();
        drawSnakes();
        // endGame();
    }

    private void drawSnakes() {
        double overlap = 0.01; // valeur pour changer le chevauchement
        for (Snake snake : controller.getSnakes()) {
            List<Segment> segments = snake.getSegments();
            for (int i = 0; i < segments.size() - 1; i++) {
                Segment segment1 = segments.get(i);
                Segment segment2 = segments.get(i + 1);
                drawCircle(segment1.getPosition(), overlap);
                // Insère des positions entre segment1 et segment2
                for (double t = 0.5; t < 1.0; t += 0.001) {
                    Position interPos = segment1.getPosition().inter(segment2.getPosition(), t);
                    drawCircle(interPos, overlap);
                }
            }
            // Dessin du dernier segment
            drawCircle(segments.get(segments.size() - 1).getPosition(), overlap);
        }
    }

    private void drawCircle(Position pos, double overlap) {
        double diameter = CELL_SIZE;
        double x = (pos.x() - overlap) * CELL_SIZE + CELL_SIZE / 2.0;
        double y = (pos.y() - overlap) * CELL_SIZE + CELL_SIZE / 2.0;
        if (x >= 0 && x <= pane.getWidth() && y >= 0 && y <= pane.getHeight()) {
            Circle circle = new Circle(x, y, diameter / 2.0);
            circle.setFill(Color.RED);
            pane.getChildren().add(circle);
        }
    }

    private void drawSegments() {
        pane.getChildren().clear();
        for (Segment segment : controller.getGrid().values()) {
            if (segment.isDead()) {
                Position pos = segment.getPosition();
                double diameter = segment.getDiameter() * CELL_SIZE;
                double x = pos.x() * CELL_SIZE + CELL_SIZE / 2.0;
                double y = pos.y() * CELL_SIZE + CELL_SIZE / 2.0;
                if (x >= 0 && x <= pane.getWidth() && y >= 0 && y <= pane.getHeight()) {
                    Circle circle = new Circle(x, y, diameter / 2.0);
                    pane.getChildren().add(circle);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void endGame() {
        if (!controller.gameFinished())
            return;

        pane.getChildren().clear();
        Rectangle gameOverRect = new Rectangle(0, 0, pane.getWidth(), pane.getHeight());
        gameOverRect.setFill(Color.BLACK);
        gameOverRect.setOpacity(0.7);

        Text gameOverText = new Text("La partie est terminée ! L'un des serpents a perdu");
        gameOverText.setFont(Font.font("Arial", 28));
        gameOverText.setFill(Color.WHITE);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setLayoutX((pane.getWidth() - gameOverText.getLayoutBounds().getWidth()) / 2);
        gameOverText.setLayoutY((pane.getHeight() - gameOverText.getLayoutBounds().getHeight()) / 2);

        pane.getChildren().addAll(gameOverRect, gameOverText);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), gameOverText);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public static void setCellSize(int cellSize) {
        CELL_SIZE = cellSize;
    }
}

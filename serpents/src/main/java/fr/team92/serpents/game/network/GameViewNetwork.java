package fr.team92.serpents.game.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.team92.serpents.game.model.GameMode;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameViewNetwork implements Observer {
    private Pane pane;
    private GameControllerNetwork gameController;
    private ArrayList<Circle> snakeCircles = new ArrayList<>();

    public GameViewNetwork(GameControllerNetwork gameController) {
        this.pane = gameController.getPane();
        this.gameController = gameController;

        Image backgroundImage = new Image(
                getClass().getResource("/fr/team92/serpents/main/ressources/background.jpg").toExternalForm());

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));
    }

    public synchronized void update() {
        Platform.runLater(() -> {
            pane.getChildren().clear();
            List<Segment> segments = gameController.getSegments();
            Position headPos = gameController.getHeadPos();

            if (headPos == null) {
                return;
            }

            int cellSize = gameController.getCellSize() * 15;

            for (Segment segment : segments) {
                Position pos = segment.getPosition();
                double x = (pos.x() - headPos.x()) * cellSize + pane.getWidth() / 2.0;
                double y = (pos.y() - headPos.y()) * cellSize + pane.getHeight() / 2.0;
                double radius = segment.getDiameter() * cellSize / 2.0;

                // Si une partie du segment est à un bord de la grille, on le dessine aussi
                // de l'autre côté
                boolean left = x - radius < 0;
                boolean right = x + radius > pane.getWidth();
                boolean top = y - radius < 0;
                boolean bottom = y + radius > pane.getHeight();

                if (left) {
                    drawSegment(pane, segment, x + pane.getWidth(), y, cellSize);
                } else if (right) {
                    drawSegment(pane, segment, x - pane.getWidth(), y, cellSize);
                }
                if (top) {
                    drawSegment(pane, segment, x, y + pane.getHeight(), cellSize);
                } else if (bottom) {
                    drawSegment(pane, segment, x, y - pane.getHeight(), cellSize);
                }

                // Si le segment est dans un coin, on le dessine aussi dans le coin opposé
                if (left && top) {
                    drawSegment(pane, segment, x + pane.getWidth(), y + pane.getHeight(), cellSize);
                } else if (right && top) {
                    drawSegment(pane, segment, x - pane.getWidth(), y + pane.getHeight(), cellSize);
                } else if (right && bottom) {
                    drawSegment(pane, segment, x - pane.getWidth(), y - pane.getHeight(), cellSize);
                } else if (left && bottom) {
                    drawSegment(pane, segment, x + pane.getWidth(), y - pane.getHeight(), cellSize);
                }

                // Ensuite, on dessine le segment à sa position actuelle
                drawSegment(pane, segment, x, y, cellSize);
            }

            // ajout des cercles des serpents après les segments morts pour qu'ils soient au
            // dessus dans l'affichage
            for (Circle circle : snakeCircles) {
                pane.getChildren().add(circle);
            }
            snakeCircles.clear();
        });
    }

    private void drawSegment(Pane pane, Segment segment, double x, double y, int cellSize) {
        double diameter = segment.getDiameter() * cellSize;
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
    public GameMode getGameMode() {
        return null;
    }

}
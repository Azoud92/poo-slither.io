package fr.team92.serpents.game.view;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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

        Image backgroundImage = new Image(
                getClass().getResource("/fr/team92/serpents/main/ressources/background.jpg").toExternalForm());

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));

        model.addObserver(this);
        this.update();
    }

    @Override
    public void update() {
        drawSegments();
        // endGame();
    }

    private void drawSegments() {
        pane.getChildren().clear();
        Segment headSegment = controller.getHumanSnake().getSegments().getFirst();

        for (Segment segment : controller.getGrid().values()) {
            Position pos = segment.getPosition();
            double x = pos.x() * CELL_SIZE + CELL_SIZE / 2.0;
            double y = pos.y() * CELL_SIZE + CELL_SIZE / 2.0;

            // Dessinez le segment à sa position actuelle
            drawSegment(segment, x, y);

            // Si le segment est hors de la vue, dessinez un segment supplémentaire de
            // l'autre côté
            if (x <= 0) {
                drawSegment(segment, x + pane.getWidth(), y);
            } else if (x >= pane.getWidth()) {
                drawSegment(segment, x - pane.getWidth(), y);
            }
            if (y <= 0) {
                drawSegment(segment, x, y + pane.getHeight());
            } else if (y >= pane.getHeight()) {
                drawSegment(segment, x, y - pane.getHeight());
            }
        }

        // Après avoir dessiné tous les segments, on ajuste la position de la vue
        if (headSegment != null) {
            Position headPos = headSegment.getPosition();
            double headX = headPos.x() * CELL_SIZE + CELL_SIZE / 2.0;
            double headY = headPos.y() * CELL_SIZE + CELL_SIZE / 2.0;

            // on calcule le décalage de la tête par rapport au centre de la fenêtre
            double offsetX = pane.getWidth() / 2 - headX;
            double offsetY = pane.getHeight() / 2 - headY;

            // on déplace tous les enfants de la fenêtre par le décalage inverse
            for (Node child : pane.getChildren()) {
                child.setTranslateX(offsetX);
                child.setTranslateY(offsetY);
            }
        }
    }

    private void drawSegment(Segment segment, double x, double y) {
        double diameter = segment.getDiameter() * CELL_SIZE;
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

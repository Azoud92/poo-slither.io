package fr.team92.serpents.game.view;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
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
    private static Text scoreText;

    public GameView(Observable model, GameController controller, Pane pane) {
        this.pane = pane;
        this.controller = controller;
        CELL_SIZE = controller.getCellSize();

        Image backgroundImage = new Image(
                getClass().getResource("/fr/team92/serpents/main/ressources/background.jpg").toExternalForm());

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));

        scoreText = new Text();
        scoreText.setFont(Font.font("Arial", 20));
        scoreText.setFill(Color.WHITE);
        pane.getChildren().add(scoreText);

        model.addObserver(this);
        this.update();
    }

    private void updateScore() {
        int score = controller.getScore();
        System.out.println(score);
        scoreText.setText("Score: " + score);
        scoreText.setX(pane.getWidth() - scoreText.getLayoutBounds().getWidth() - 20);
        scoreText.setY(20);
        if (!pane.getChildren().contains(scoreText)) {
            pane.getChildren().add(scoreText);
        }
    }

    @Override
    public void update() {
        drawSegments();
        updateScore();
        // endGame();
    }

    private void drawSegments() {
        pane.getChildren().clear();
        pane.getChildren().add(scoreText);

        Segment headSegment = controller.getHumanSnake().getSegments().getFirst();
        Position headPos = headSegment.getPosition();

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
                drawSegment(segment, x + pane.getWidth(), y);
            } else if (right) {
                drawSegment(segment, x - pane.getWidth(), y);
            }
            if (top) {
                drawSegment(segment, x, y + pane.getHeight());
            } else if (bottom) {
                drawSegment(segment, x, y - pane.getHeight());
            }

            // Si le segment est dans un coin, on le dessine aussi dans le coin opposé
            if (left && top) {
                drawSegment(segment, x + pane.getWidth(), y + pane.getHeight());
            } else if (right && top) {
                drawSegment(segment, x - pane.getWidth(), y + pane.getHeight());
            } else if (right && bottom) {
                drawSegment(segment, x - pane.getWidth(), y - pane.getHeight());
            } else if (left && bottom) {
                drawSegment(segment, x + pane.getWidth(), y - pane.getHeight());
            }

            // Ensuite, on dessine le segment à sa position actuelle
            drawSegment(segment, x, y);
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

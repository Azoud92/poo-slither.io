package fr.team92.serpents.game.view;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.Segment;
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
    private static final int CELL_SIZE = 10;

    public GameView(Observable model, GameController controller, Pane pane) {
        this.pane = pane;
        this.controller = controller;

        model.addObserver(this);
        this.update();
    }


    @Override
    public void update() {
        drawSegments();
        //endGame();
    }

    private void drawSegments() {
        pane.getChildren().clear();
    for (Segment segment : controller.getGrid().values()) {
        Position pos = segment.getPosition();
        double diameter = segment.getDiameter() * CELL_SIZE;
        Circle circle = new Circle(pos.x() * CELL_SIZE + CELL_SIZE / 2.0, pos.y() * CELL_SIZE + CELL_SIZE / 2.0, diameter / 2.0);

        if (segment.isDead()) {
            circle.setFill(Color.ORANGE);
        } else {
            circle.setFill(Color.RED);
        }

        pane.getChildren().add(circle);
    }
}

    @SuppressWarnings("unused")
    private void endGame() {
        if (!controller.gameFinished()) return;

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
}


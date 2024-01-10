package fr.team92.serpents.game.view;

import java.io.IOException;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameMode;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;

public final class GameView implements Observer {

    private final GameController controller;
    private final Pane pane;
    private static int CELL_SIZE;
    private static Text scoreText;
    private final GameMode gameMode;

    public GameView(Observable model, GameController controller, Pane pane, GameMode gameMode) {
        this.pane = pane;
        this.controller = controller;
        CELL_SIZE = controller.getCellSize();
        this.gameMode = gameMode;

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
        endGame();
    }

    private void drawSegments() {
        gameMode.drawSegments(pane, controller, CELL_SIZE);
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
        fadeTransition.setCycleCount(3);

        fadeTransition.setOnFinished(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
                Stage stage = (Stage) gameOverText.getScene().getWindow();
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        fadeTransition.play();
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public static void setCellSize(int cellSize) {
        CELL_SIZE = cellSize;
    }
}

package fr.team92.serpents.game.view;

import java.io.IOException;

import fr.team92.serpents.game.controller.GameController;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * Cette classe représente la vue du jeu.
 * Elle implémente l'interface Observer.
 */
public final class GameView implements Observer {

    /**
     * Le contrôleur du jeu.
     */
    private final GameController controller;

    /**
     * Le Pane sur lequel le jeu est dessiné.
     */
    private final Pane pane;

    /**
     * Le texte affichant le score.
     */
    private static Text scoreText;

    /**
     * Le HBox contenant le texte du score.
     */
    private final HBox hbox = new HBox();

    /**
     * Le mode de jeu actuel.
     */
    private final GameMode gameMode;

    /**
     * Initialise la vue du jeu.
     * @param model le modèle du jeu
     * @param controller le contrôleur du jeu
     * @param pane le Pane sur lequel le jeu est dessiné
     * @param gameMode le mode de jeu actuel
     */
    public GameView(Observable model, GameController controller, Pane pane, GameMode gameMode) {
        this.pane = pane;
        this.controller = controller;
        this.gameMode = gameMode;

        Image backgroundImage = new Image(
                getClass().getResource("/fr/team92/serpents/main/ressources/background.jpg").toExternalForm());

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));

        scoreText = new Text();
        scoreText.setFont(Font.font("Arial", 20));
        scoreText.setFill(Color.WHITE);
        scoreText.setTextAlignment(TextAlignment.CENTER);

        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.getChildren().add(scoreText);
        pane.getChildren().add(hbox);
        hbox.toFront();

        model.addObserver(this);
        this.update();
    }

    /**
     * Met à jour le score affiché.
     */
    private void updateScore() {
        gameMode.updateScore(scoreText, controller, pane);
    }

    /**
     * Met à jour la vue du jeu.
     */
    @Override
    public void update() {
        drawSegments();
        updateScore();
        endGame();
    }

    /**
     * Dessine les segments du serpent sur le plateau de jeu.
     */
    private void drawSegments() {
        gameMode.drawSegments(pane, controller);
        hbox.toFront();
    }

    /**
     * Affiche un écran de fin de partie si la partie est terminée.
     */
    private void endGame() {
        if (!controller.gameFinished())
            return;

        pane.getChildren().clear();
        Rectangle gameOverRect = new Rectangle(0, 0, pane.getWidth(), pane.getHeight());
        gameOverRect.setFill(Color.BLACK);
        gameOverRect.setOpacity(0.7);

        Text gameOverText = new Text("La partie est terminée ! Plus de serpents humains en jeu!");
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
        fadeTransition.setCycleCount(2);

        fadeTransition.setOnFinished(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fr/team92/serpents/home/view/Homepage.fxml"));
                Stage stage = (Stage) gameOverText.getScene().getWindow();
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeTransition.play();
    }

    /**
     * Obtenir le mode de jeu actuel.
     * @return le mode de jeu actuel
     */
    @Override
    public GameMode getGameMode() {
        return gameMode;
    }
}

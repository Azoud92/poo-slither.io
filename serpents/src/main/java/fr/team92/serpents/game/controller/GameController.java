package fr.team92.serpents.game.controller;

import java.util.List;
import java.util.Map;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.GameState;
import fr.team92.serpents.utils.Position;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Représente le contrôleur du jeu
 */
public final class GameController {

    /**
     * Le modèle du jeu
     */
    private final GameModel model;

    /**
     * Le timer du jeu
     */
    private AnimationTimer gameLoop;

    private double lastUpdate;

    /**
     * Constructeur du contrôleur du jeu
     * 
     * @param model le modèle du jeu
     * @param scene la scène JavaFX
     */
    public GameController(GameModel model, Scene scene) {
        this.model = model;
        setKeyListeners(scene);
        setMouseListeners(scene);
    }

    public void gameStart() {
        if (model.getState() != GameState.WAITING)
            throw new IllegalStateException("Game is not waiting to start");
        if (gameLoop != null)
            throw new IllegalStateException("Game loop already started");
        model.gameStart();
        lastUpdate = System.nanoTime();

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (model.getState() != GameState.RUNNING) {
                    this.stop();
                } else if (model.getState() == GameState.RUNNING) {
                    double elapsedTimeInSeconds = (now - lastUpdate) / 1_000_000_000.0;
                    updateGame(elapsedTimeInSeconds);
                    lastUpdate = now;
                }
            }

        };
        gameLoop.start();
    }

    /**
     * Ajoute les écouteurs d'événements clavier
     * 
     * @param scene la scène JavaFX
     */
    private void setKeyListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            handleKeyPressed(event);
        });
    }

    /**
     * Applique les actions du joueur courant en fonction de l'événement clavier
     * 
     * @param event l'événement clavier
     */
    private void handleKeyPressed(KeyEvent event) {
        if (model.getState() != GameState.RUNNING) {
            return;
        }

        for (Snake snake : model.getSnakes()) {
            System.out.println("keyPressed");
            SnakeController controller = snake.getController();

            if (controller instanceof HumanSnakeController) {
                ((HumanSnakeController) controller).setEvent(event);
            }
        }
    }

    /**
     * Ajoute les écouteurs d'événements de la souris
     * 
     * @param scene la scène JavaFX
     */
    private void setMouseListeners(Scene scene) {
        scene.setOnMouseMoved(event -> {
            handleMouseMoved(event);
        });
    }

    /**
     * Applique les actions du joueur courant en fonction de l'événement de la
     * souris
     * 
     * @param event l'événement de la souris
     */
    private void handleMouseMoved(MouseEvent event) {
        if (model.getState() != GameState.RUNNING) {
            return;
        }

        for (Snake snake : model.getSnakes()) {
            SnakeController controller = snake.getController();

            if (controller instanceof HumanSnakeController) {
                ((HumanSnakeController) controller).setEvent(event);
            }
        }
    }

    /**
     * Met à jour le jeu
     * 
     * @param controller le contrôleur du serpent
     */
    private void updateGame(double lastUpdate) {
        if (model.getState() != GameState.RUNNING) {
            return;
        }
        for (Snake snake : model.getSnakes()) {
            SnakeController controller = snake.getController();
            controller.controlSnake(snake, model, lastUpdate);
        }
        model.moveSnakes(lastUpdate);
    }

    /**
     * Obtenir la largeur de la grille
     * 
     * @return la largeur de la grille
     */
    public int getWidth() {
        return model.getWidth();
    }

    /**
     * Obtenir la hauteur de la grille
     * 
     * @return la hauteur de la grille
     */
    public int getHeight() {
        return model.getHeight();
    }

    /**
     * Obtenir la grille
     * 
     * @return la grille
     */
    public Map<Position, Segment> getGrid() {
        return model.getGrid();
    }

    /**
     * Vérifier si la partie est finie
     * 
     * @return true si la partie est finie, false sinon
     */
    public boolean gameFinished() {
        return model.getState() == GameState.FINISHED;
    }

    /**
     * Obtenir la liste des serpents
     * 
     * @return la liste des serpents
     */
    public List<Snake> getSnakes() {
        return model.getSnakes();
    }

    public int getCellSize() {
        return model.getCellSize();
    }

    public Snake getHumanSnake() {
        for (Snake snake : model.getSnakes()) {
            if (snake.getController() instanceof HumanSnakeController) {
                return snake;
            }
        }
        return null;
    }
}

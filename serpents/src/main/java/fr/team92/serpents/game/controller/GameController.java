package fr.team92.serpents.game.controller;

import java.util.Map;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.bot.BotController;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Position;
import javafx.scene.Scene;

/**
 * Représente le contrôleur du jeu
 */
public final class GameController {

    /**
     * Le modèle du jeu
     */
    private GameModel model;

    /**
     * Constructeur du contrôleur du jeu
     * @param model le modèle du jeu
     * @param scene la scène JavaFX
     */
    public GameController(GameModel model, Scene scene) {
        this.model = model;
        setKeyListeners(scene);
        botPlay();
    }

    /**
     * Ajoute les écouteurs d'événements clavier
     * @param scene la scène JavaFX
     */
    private void setKeyListeners(Scene scene) {
        scene.setOnKeyReleased(event -> {
            Snake currentSnake = model.getCurrentPlayer();
            SnakeController controller = currentSnake.getController();

            if (controller instanceof HumanSnakeController) {
                ((HumanSnakeController) controller).setEvent(event);              
            }

            try {
                controller.controlSnake(model.getCurrentPlayer(), model);
                model.moveSnake();
                botPlay();
            }
            catch (IllegalArgumentException e) { }            
        });
    }

    private void botPlay() {
        if (model.getCurrentPlayer().getController() instanceof BotController) {
            SnakeController controller = model.getCurrentPlayer().getController();
            controller.controlSnake(model.getCurrentPlayer(), model);
            model.moveSnake();
        }
    }

    /**
     * Obtenir la largeur de la grille
     * @return la largeur de la grille
     */
    public int getWidth() {
        return model.getWidth();
    }

    /**
     * Obtenir la hauteur de la grille
     * @return la hauteur de la grille
     */
    public int getHeight() {
        return model.getHeight();
    }

    /**
     * Obtenir la grille
     * @return la grille
     */
    public Map<Position, Segment> getGrid() {
        return model.getGrid();
    }
}

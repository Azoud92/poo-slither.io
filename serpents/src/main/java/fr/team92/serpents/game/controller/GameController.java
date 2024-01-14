package fr.team92.serpents.game.controller;

import java.util.Map;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.utils.GameState;
import fr.team92.serpents.utils.Position;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Représente le contrôleur du jeu. Cette classe est responsable de la gestion des entrées de l'utilisateur
 * et de la mise à jour de l'état du jeu.
 */
public final class GameController {

    /**
     * Le modèle du jeu. Contient l'état actuel du jeu.
     */
    private final GameModel model;

    /**
     * La boucle de jeu. Appelle la méthode de mise à jour à chaque itération.
     */
    private GameLoop gameLoop;

    /**
     * La scène JavaFX. Utilisée pour gérer les entrées de l'utilisateur.
     */
    private Scene scene;

    /**
     * Initialise le contrôleur du jeu hors ligne
     * @param model le modèle du jeu
     * @param scene la scène JavaFX
     */
    public GameController(GameModel model, Scene scene) {
        this.scene = scene;
        this.model = model;
        setKeyListeners(scene);
        setMouseListeners(scene);
        gameLoop = new OfflineGameLoop(this::loopTask);
    }

    /**
     * Initialise le contrôleur du jeu en ligne
     * @param model le modèle du jeu
     */
    public GameController(GameModel model) {
        this.model = model;
        gameLoop = new ServerGameLoop(this::loopTask);
    }

    /**
     * La tâche à exécuter à chaque itération de la boucle de jeu
     * @param deltaTime le temps écoulé depuis la dernière itération
     */
    private void loopTask(long deltaTime) {
        if (model.getState() != GameState.RUNNING) {
            gameLoop.stop();
            return;
        }
        else if (model.getState() == GameState.RUNNING) {
            double deltaTimeInSeconds = deltaTime / 1_000_000_000.0;
            updateGame(deltaTimeInSeconds);
        }
    }

    /**
     * Met à jour l'état du jeu
     * @param lastUpdate le temps écoulé depuis la dernière itération
     */
    private void updateGame(double lastUpdate) {
        if (model.getState() != GameState.RUNNING) {
            return;
        }
        for (Snake snake : model.getSnakes()) {
            SnakeController controller = snake.getController();
            controller.controlSnake(snake, model, lastUpdate, scene);
            if (snake.isAccelerating()) {
                snake.accelerate();
            } else {
                snake.decelerate();
            }
        }
        model.moveSnakes(lastUpdate);
    }

    /**
     * Démarre le jeu
     */
    public void gameStart() {
        if (model.getState() != GameState.WAITING || gameLoop.isRunning())
            throw new IllegalStateException("La partie a déjà été lancée");
        model.gameStart();
        gameLoop.start();
    }

    /**
     * Ajoute les écouteurs d'événements clavier
     * @param scene la scène JavaFX
     */
    private void setKeyListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            handleKey(event);
        });

        scene.setOnKeyReleased(event -> {
            handleKey(event);
        });
    }

    /**
     * Applique les actions du joueur courant en fonction de l'événement clavier
     * @param event l'événement clavier
     */
    private void handleKey(KeyEvent event) {
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
     * Ajoute les écouteurs d'événements souris
     * @param scene la scène JavaFX
     */
    private void setMouseListeners(Scene scene) {
        scene.setOnMouseMoved(event -> {
            handleMouse(event, true);
        });
        scene.setOnMouseDragged(event -> {
            handleMouse(event, true);
        });
        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleMouse(event, false);
            }
        });
        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleMouse(event, false);
            }
        });
    }

    /**
     * Applique les actions du joueur courant en fonction de l'événement souris
     * @param event l'événement souris
     * @param move true si l'événement est un mouvement de souris, false sinon
     */
    private void handleMouse(MouseEvent event, boolean move) {
        if (model.getState() != GameState.RUNNING) {
            return;
        }

        for (Snake snake : model.getSnakes()) {
            SnakeController controller = snake.getController();

            if (controller instanceof HumanSnakeController) {
                if (move) {
                    ((HumanSnakeController) controller).setEvent(event);
                } else {
                    ((HumanSnakeController) controller).setOtherMouseEvent(event);
                }
            }
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
        return model.getSnakesSegmentsGrid();
    }

    /**
     * Vérifie si la partie est terminée
     * @return true si la partie est terminée, false sinon
     */
    public boolean gameFinished() {
        return model.getState() == GameState.FINISHED;
    }

    /**
     * Obtenir la taille d'une cellule de la grille
     * @return la taille d'une cellule de la grille
     */
    public int getCellSize() {
        return model.getCellSize();
    }

    /**
     * Obtenir le premier joueur humain trouvé
     * @return le premier joueur humain trouvé
     */
    public Snake getHumanSnake() {
        for (Snake snake : model.getSnakes()) {
            if (snake.getController() instanceof HumanSnakeController) {
                return snake;
            }
        }
        return null;
    }

    /**
     * Obtenir le score des joueurs humains
     * @return le score des joueurs humains
     */
    public int getScore() {
        if (getHumanSnake() == null) {
            return 0;
        }
        return getHumanSnake().getLength();
    }

    public Snake getPlayer1() {// TODO : a changer
        for (Snake snake : model.getSnakes()) {
            if (snake.getController() instanceof HumanSnakeController) {
                return snake;
            }
        }
        throw new IllegalStateException("Player 1 does not exist");
    }

    public Snake getPlayer2() { // TODO: a changer
        boolean foundFirstPlayer = false;
        for (Snake snake : model.getSnakes()) {
            if (snake.getController() instanceof HumanSnakeController) {
                if (foundFirstPlayer) {
                    return snake;
                } else {
                    foundFirstPlayer = true;
                }
            }
        }
        throw new IllegalStateException("Player 2 does not exist");
    }

}

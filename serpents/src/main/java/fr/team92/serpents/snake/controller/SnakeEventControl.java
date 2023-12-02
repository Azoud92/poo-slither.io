package fr.team92.serpents.snake.controller;

import fr.team92.serpents.snake.model.Snake;
import javafx.scene.input.InputEvent;

/**
 * Interface pour gérer le contrôle du serpent
 */
public interface SnakeEventControl {

    /**
     * Gérer le contrôle du serpent en fonction de l'événement
     * @param snake le serpent
     * @param event l'événement
     */
    void handleControl(Snake snake, InputEvent event);
}

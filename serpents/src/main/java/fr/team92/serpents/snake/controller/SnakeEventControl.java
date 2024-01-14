package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.view.GameMode;
import fr.team92.serpents.snake.model.Snake;
import javafx.scene.input.InputEvent;

/**
 * Interface pour gérer le contrôle du serpent en fonction de l'événement de la scène
 */
public sealed interface SnakeEventControl permits KeyboardControl, MouseControl {

    /**
     * Gère le contrôle du serpent en fonction de l'événement d'entrée.
     *
     * @param snake le serpent à contrôler
     * @param event l'événement d'entrée
     * @param gameMode le mode de jeu
     * @param cellSize la taille de la cellule
     * @param windowWidth la largeur de la fenêtre
     * @param windowHeight la hauteur de la fenêtre
     */
    void handleControl(Snake snake, InputEvent event, GameMode gameMode, int cellSize, double windowWidth,
            double windowHeight);
}

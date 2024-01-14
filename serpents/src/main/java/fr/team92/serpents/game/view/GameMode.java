package fr.team92.serpents.game.view;

import fr.team92.serpents.game.controller.GameController;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Cette interface scellée représente un mode de jeu.
 * Elle définit les méthodes nécessaires pour dessiner les segments du serpent et mettre à jour le score.
 * Seules les classes SinglePlayerMode et TwoPlayersMode peuvent implémenter cette interface.
 */
public sealed interface GameMode permits SinglePlayerMode, TwoPlayersMode {

    /**
     * Dessine les segments du serpent sur le plateau de jeu.
     * @param pane le Pane sur lequel dessiner les segments.
     * @param controller le GameController qui contrôle le jeu.
     */
    void drawSegments(Pane pane, GameController controller);

    /**
     * Met à jour le score affiché à l'écran.
     * @param scoreText le Text où afficher le score.
     * @param controller le GameController qui contrôle le jeu.
     * @param pane le Pane sur lequel le score est affiché.
     */
    void updateScore(Text scoreText, GameController controller, Pane pane);

}

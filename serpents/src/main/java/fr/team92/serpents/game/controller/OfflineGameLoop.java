package fr.team92.serpents.game.controller;

import javafx.animation.AnimationTimer;

/**
 * Représente la boucle de jeu pour la partie client hors ligne (utilisant un AnimationTimer de JavaFX)
 */
public final class OfflineGameLoop implements GameLoop {

    /**
     * AnimationTimer de JavaFX
     */
    private final AnimationTimer animationTimer;

    /**
     * Indique si la boucle de jeu est en cours d'exécution
     */
    private boolean running;

    /**
     * Initialise une boucle de jeu pour la partie client hors ligne
     * @param runnable le runnable à exécuter à chaque rafraichissement
     */
    public OfflineGameLoop(Runnable runnable) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                runnable.run();
            }
        };
    }

    @Override
    public void start() {
        animationTimer.start();
        running = true;
    }

    @Override
    public void stop() {
        animationTimer.stop();
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}

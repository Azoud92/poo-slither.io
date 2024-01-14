package fr.team92.serpents.game.controller;

import java.util.function.Consumer;

import javafx.animation.AnimationTimer;

/**
 * Cette classe représente la boucle de jeu pour le mode hors ligne.
 * Elle utilise un AnimationTimer de JavaFX pour gérer la boucle de jeu.
 */
public final class OfflineGameLoop implements GameLoop {

    /**
     * L'AnimationTimer de JavaFX utilisé pour gérer la boucle de jeu.
     */
    private final AnimationTimer animationTimer;

    /**
     * Un booléen indiquant si la boucle de jeu est en cours d'exécution.
     */
    private boolean running;

    /**
     * Le temps depuis la dernière mise à jour de la boucle de jeu.
     */
    private long lastUpdate = 0;

    /**
     * Crée une nouvelle boucle de jeu pour le mode hors ligne.
     * @param runnable le code à exécuter à chaque mise à jour de la boucle de jeu
     */
    public OfflineGameLoop(Consumer<Long> runnable) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    long deltaTime = now - lastUpdate;
                    runnable.accept(deltaTime);
                }
                lastUpdate = now;
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

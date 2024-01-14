package fr.team92.serpents.game.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Cette classe représente la boucle de jeu pour la partie serveur.
 * Elle utilise un ScheduledExecutorService pour gérer la boucle de jeu.
 */
public final class ServerGameLoop implements GameLoop {

    /**
     * Le ScheduledExecutorService utilisé pour exécuter la boucle de jeu.
     */
    private ScheduledExecutorService executorService;

    /**
     * Le Runnable à exécuter à chaque rafraîchissement de la boucle de jeu.
     */
    private final Consumer<Long> runnable;

    /**
     * Le taux de rafraîchissement du jeu, en images par seconde (FPS).
     */
    private static final int rate = 60;

    /**
     * Un booléen indiquant si la boucle de jeu est en cours d'exécution.
     */
    private boolean running;

    /**
     * Le temps depuis la dernière mise à jour de la boucle de jeu.
     */
    private long lastUpdate = 0;

    /**
     * Crée une nouvelle boucle de jeu pour la partie serveur.
     * @param runnable le code à exécuter à chaque rafraîchissement de la boucle de jeu.
     */
    public ServerGameLoop(Consumer<Long> runnable) {
        this.runnable = runnable;
    }

    @Override
    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            long now = System.nanoTime();
            if (lastUpdate > 0) {
                long deltaTime = now - lastUpdate;
                runnable.accept(deltaTime);
            }
            lastUpdate = now;
        }, 0, 1000 / rate, TimeUnit.MILLISECONDS);
        running = true;
    }

    @Override
    public void stop() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}

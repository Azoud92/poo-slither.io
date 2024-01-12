package fr.team92.serpents.game.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Représente la boucle de jeu pour la partie serveur (utilisant un ExecutorService)
 */
public final class ServerGameLoop implements GameLoop {

    /**
     * ExecutorService utilisé pour exécuter la boucle de jeu
     */
    private ScheduledExecutorService executorService;

    /**
     * Runnable à exécuter à chaque rafraichissement
     */
    private final Runnable runnable;

    /**
     * Taux de rafraichissement du jeu (en FPS)
     */
    private static final int rate = 60;

    /**
     * Indique si la boucle de jeu est en cours d'exécution
     */
    private boolean running;

    /**
     * Initialise une boucle de jeu pour la partie serveur en ligne 
     * @param runnable le runnable à exécuter à chaque rafraichissement
     */
    public ServerGameLoop(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(runnable, 0, 1000 / rate, TimeUnit.MILLISECONDS);
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

package fr.team92.serpents.game.controller;

/**
 * Représente la boucle de jeu
 */
public interface GameLoop {

    /**
     * Démarre la boucle de jeu
     */
    void start();

    /**
     * Arrête la boucle de jeu
     */
    void stop();

    /**
     * Indique si la boucle de jeu est en cours d'exécution
     * @return true si la boucle de jeu est en cours d'exécution, false sinon
     */
    boolean isRunning();

}

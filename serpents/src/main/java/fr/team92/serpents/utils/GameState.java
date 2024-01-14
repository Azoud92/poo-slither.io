package fr.team92.serpents.utils;

/**
 * Enumération des états possibles d'une partie.
 */
public enum GameState {

    /**
     * La partie est en attente de lancement
     */
    WAITING,

    /**
     * La partie est en cours
     */
    RUNNING,

    /**
     * La partie est en pause
     */
    PAUSED,

    /**
     * La partie est terminée
     */
    FINISHED
}

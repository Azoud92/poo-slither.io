package fr.team92.serpents.utils;

import fr.team92.serpents.game.model.GameMode;

/**
 * Interface d'un observateur
 */
public interface Observer {

    /**
     * Mettre à jour l'observateur
     */
    void update();

    GameMode getGameMode();
}

package fr.team92.serpents.utils;

/**
 * Interface d'un observable
 */
public interface Observable {

    /**
     * Ajoute un observateur à la liste des observateurs
     * @param observer l'observateur
     */
    void addObserver(Observer observer);

    /**
     * Supprime un observateur de la liste des observateurs
     * @param observer l'observateur
     */
    void removeObserver(Observer observer);

    /**
     * Notifie tous les observateurs
     */
    void notifyObservers();
}

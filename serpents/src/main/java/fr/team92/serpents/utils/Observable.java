package fr.team92.serpents.utils;

/**
 * Interface pour un objet observable.
 * Un objet observable peut avoir des observateurs qui sont notifiés lorsque l'état de l'objet observable change.
 */
public interface Observable {

    /**
     * Ajoute un observateur à la liste des observateurs de cet objet observable.
     * @param observer L'observateur à ajouter.
     */
    void addObserver(Observer observer);

    /**
     * Supprime un observateur de la liste des observateurs de cet objet observable.
     * @param observer L'observateur à supprimer.
     */
    void removeObserver(Observer observer);

    /**
     * Notifie tous les observateurs de cet objet observable.
     * Typiquement, lorsque l'état de l'objet observable change.
     */
    void notifyObservers();
}

package fr.team92.serpents.utils;

public interface Observable {

    /**
     * Add an observer to the list of observers
     * @param observer
     */
    void addObserver(Observer observer);

    /**
     * Remove an observer from the list of observers
     * @param observer
     */
    void removeObserver(Observer observer);

    /**
     * Notify all observers
     */
    void notifyObservers();
}

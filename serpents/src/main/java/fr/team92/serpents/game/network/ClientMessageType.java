package fr.team92.serpents.game.network;

/**
 * Énumération des différents types de messages que le client peut envoyer au serveur.
 * Chaque type de message représente un format de communication spécifique entre le client et le serveur.
 */
public enum ClientMessageType {

    /**
     * Représente un message de connexion au serveur.
     * Utilisé pour informer le serveur de la connexion d'un nouveau client afin que le serveur génère un serpent.
     */
    CONNECTION,

    /**
     * Représente un message de déconnexion du serveur.
     * Utilisé pour informer le serveur de la déconnexion d'un client afin que le serveur supprime les données associées.
     */
    DISCONNECTION,

    /**
     * Représente la taille de la fenêtre de jeu (côté client).
     * Utilisé pour que le serveur renvoie uniquement les segments dans le champ de vision du joueur
     */
    WINDOW_SIZE,

    /**
     * Représente la direction du serpent du joueur
     * Utilisé pour que le serveur puisse mettre à jour la direction du serpent du joueur
     */
    DIRECTION;

}

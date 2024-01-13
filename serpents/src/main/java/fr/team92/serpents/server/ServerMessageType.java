package fr.team92.serpents.server;

/**
 * Énumération des différents types de messages que le serveur peut envoyer aux
 * clients.
 * Chaque type de message représente un format de communication spécifique entre
 * le serveur et les clients.
 */
public enum ServerMessageType {

    /**
     * Représente un message d'erreur envoyé par le serveur.
     * Utilisé pour informer le client d'une situation d'erreur survenue côté
     * serveur.
     */
    ERROR,

    /**
     * Représente un serpent envoyé par le serveur.
     * Utilisé pour envoyer les informations du serpent du joueur au client afin
     * qu'il puisse l'afficher.
     */
    SNAKE,

    /**
     * Représente un tableau contenant la liste positions associées aux segments
     * visibles envoyé par le serveur.
     * Utilisé pour envoyer les informations des segments visibles au client afin
     * qu'il puisse les afficher.
     */
    VISIBLE_SEGMENTS,

    /*
     * Représente la direction du serpent envoyé par le serveur.
     */
    DIRECTION;

}

package fr.team92.serpents.server;

import com.google.gson.JsonObject;

import fr.team92.serpents.utils.SerializableToJSON;

/**
 * Énumération représentant différents types d'erreurs pouvant survenir dans le serveur.
 * Chaque erreur est associée à un code spécifique et à un message descriptif.
 */
public enum ServerError implements SerializableToJSON {

    // Définitions des erreurs spécifiques avec leurs codes et messages.
    NO_FREE_POSITION_FOR_SNAKE(1001, "Erreur lors de la création du serpent : aucune position libre"),
    CLIENT_CONNECTION_CLOSE_ERROR(1002, "Erreur lors de la fermeture de la connexion client"),
    DATA_SEND_ERROR(1003, "Erreur lors de l'envoi de données au client"),
    CLIENT_THREAD_EXECUTION_ERROR(1004, "Erreur lors de l'exécution du thread client : flux de sortie non initialisé"),
    UNKNOWN_RECEIVED_MESSAGE(1005, "Erreur : message reçu inconnu"),
    SNAKE_ALREADY_CREATED(1006, "Erreur lors de la création du serpent : le serpent a déjà été créé"),
    INVALID_WINDOW_SIZE(1007, "Erreur : taille de la fenêtre invalide"),
    WINDOW_SIZE_NOT_SET(1008, "Erreur : taille de la fenêtre non spécifiée");

    /**
     * Code numérique associé à l'erreur.
     */
    private final int code;

    /**
     * Description textuelle de l'erreur.
     */
    private final String message;

    /**
     * Constructeur privé pour initialiser chaque erreur avec son code et son message.
     * @param code Le code numérique de l'erreur.
     * @param message La description de l'erreur.
     */
    private ServerError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Fournit une représentation textuelle de l'erreur.
     * @return Une chaîne de caractères représentant l'erreur, incluant son code et son message.
     */
    @Override
    public String toString() {
        return "[ERREUR] " + code + " : " + message;
    }

    /**
     * Convertit l'erreur en un objet JSON pour une communication facile sur le réseau.
     * @return Un objet JSON représentant l'erreur, avec son code et son message.
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "error");
        json.addProperty("code", code);
        json.addProperty("message", message);
        return json;
    }

}

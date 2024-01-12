package fr.team92.serpents.server;

import com.google.gson.JsonObject;

import fr.team92.serpents.utils.SerializableToJSON;

/**
 * Représente une erreur du serveur
 */
public enum Error implements SerializableToJSON {

    NO_FREE_POSITION_FOR_SNAKE(1001, "Erreur lors de la création du serpent : aucune position libre"),
    CLIENT_CONNECTION_CLOSE_ERROR(1002, "Erreur lors de la fermeture de la connexion client"),
    DATA_SEND_ERROR(1003, "Erreur lors de l'envoi de données au client"),
    CLIENT_THREAD_EXECUTION_ERROR(1004, "Erreur lors de l'exécution du thread client : flux de sortie non initialisé"),
    UNKNOWN_RECEIVED_MESSAGE(1005, "Erreur : message reçu inconnu"),
    SNAKE_ALREADY_CREATED(1006, "Erreur lors de la création du serpent : le serpent a déjà été créé"),
    INVALID_WINDOW_SIZE(1007, "Erreur : taille de la fenêtre invalide");


    /**
     * Code de l'erreur
     */
    private final int code;

    /**
     * Message de l'erreur
     */
    private final String message;

    /**
     * Initialise l'erreur avec le code et le message spécifiés
     */
    private Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[ERREUR] " + code + " : " + message;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "error");
        json.addProperty("code", code);
        json.addProperty("message", message);
        return json;
    }

}

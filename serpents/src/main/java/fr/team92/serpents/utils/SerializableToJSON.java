package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

/**
 * Interface pour les classes qui peuvent être sérialisées en JSON en utilisant la bibliothèque GSON.
 */
@FunctionalInterface
public interface SerializableToJSON {

   /**
     * Convertit l'objet en un objet JSON en utilisant la bibliothèque GSON.
     * @return l'objet converti en un objet JSON.
     */
    JsonObject toJSON();

}

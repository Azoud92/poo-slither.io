package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

/**
 * Interface pour les classes qui peuvent être sérialisées en JSON utilisant la librairie GSON
 */
@FunctionalInterface
public interface SerializableToJSON {

    /**
     * Convertit l'objet en JSON
     * @return l'objet converti en JSON
     */
    JsonObject toJSON();

}

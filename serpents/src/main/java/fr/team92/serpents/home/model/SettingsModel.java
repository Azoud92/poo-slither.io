package fr.team92.serpents.home.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SettingsModel implements Serializable {

    /**
     * Nom du fichier de sauvegarde des paramètres
     */
    private static final String SETTINGS_FILE = "settings.ser";

    /**
     * Nombre de serpents
     */
    private int numberOfBots;

    /**
     * Nombre de nourritures
     */
    private int numberOfFood;

    public SettingsModel(int numberOfBots, int numberOfFood) {
        this.numberOfBots = numberOfBots;
        this.numberOfFood = numberOfFood;
    }

    public SettingsModel() {
        this.numberOfBots = 5;
        this.numberOfFood = 50;
    }

    /**
     * Sauvegarder les paramètres
     * @param settings les paramètres
     */
    public static void saveSettings(SettingsModel settings) {
        try {
            FileOutputStream fileOut = new FileOutputStream(SETTINGS_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(settings);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Charger les paramètres
     * @return les paramètres
     */
    public static SettingsModel loadSettings() {
        SettingsModel settings = null;
        try {
            FileInputStream fileIn = new FileInputStream(SETTINGS_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            settings = (SettingsModel) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            return new SettingsModel();
        } catch (ClassNotFoundException c) {
            return new SettingsModel();
        }
        return settings;
    }

    /**
     * Récupérer le nombre de bots
     * @return le nombre de bots
     */
    public int getNumberOfBots() {
        return numberOfBots;
    }

    /**
     * Définir le nombre de bots
     * @param numberOfBots le nombre de bots
     */
    public void setNumberOfBots(int numberOfBots) {
        this.numberOfBots = numberOfBots;
    }

    /**
     * Récupérer le nombre de nourritures
     * @return le nombre de nourritures
     */
    public int getNumberOfFood() {
        return numberOfFood;
    }

    /**
     * Définir le nombre de nourritures
     * @param numberOfFood le nombre de nourritures
     */
    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;
    }

}

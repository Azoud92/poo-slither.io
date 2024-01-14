package fr.team92.serpents.home.controller;

import java.io.IOException;

import fr.team92.serpents.home.model.SettingsModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Slider botsSlider;

    @FXML
    private Slider foodSlider;

    @FXML
    private Button saveButton;

    @FXML
    private Label botsValueLabel;

    @FXML
    private Label foodValueLabel;

    private int numberOfBots;
    private int numberOfFood;

    @FXML
    public void initialize() {
        botsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            numberOfBots = newVal.intValue();
            botsValueLabel.setText(String.format("%.0f", newVal));
        });

        foodSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            numberOfFood = newVal.intValue();
            foodValueLabel.setText(String.format("%.0f", newVal));
        });

        ButtonsAnimations.addAnimations(saveButton);

        SettingsModel gameSettings = SettingsModel.loadSettings();
        if (gameSettings != null) {
            botsSlider.setValue(gameSettings.getNumberOfBots());
            foodSlider.setValue(gameSettings.getNumberOfFood());
        }
    }

    @FXML
    public void saveButtonClicked() {
        numberOfBots = (int) botsSlider.getValue();
        numberOfFood = (int) foodSlider.getValue();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/team92/serpents/home/view/homepage.fxml"));
            Parent root = loader.load();
            Stage appStage = (Stage) saveButton.getScene().getWindow();
            Scene homePageScene = new Scene(root, appStage.getWidth(), appStage.getHeight());

            HomePageController homePageController = loader.getController();
            homePageController.setNumberOfBots(numberOfBots);
            homePageController.setNumberOfFood(numberOfFood);

            SettingsModel settings = new SettingsModel(numberOfBots, numberOfFood);
            SettingsModel.saveSettings(settings);

            appStage.setScene(homePageScene);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Sauvegarde les paramètres ici
    }

}
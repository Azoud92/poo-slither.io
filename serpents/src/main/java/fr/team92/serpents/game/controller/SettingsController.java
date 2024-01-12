package fr.team92.serpents.game.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Slider botsSlider;

    @FXML
    private Slider foodSlider;

    @FXML
    private RadioButton autoCollisionOnRadioButton;

    @FXML
    private RadioButton autoCollisionOffRadioButton;

    @FXML
    private RadioButton wallCrossingOnRadioButton;

    @FXML
    private RadioButton wallCrossingOffRadioButton;

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
        // Initialisation du contrôleur
        ToggleGroup autoCollisionGroup = new ToggleGroup();
        autoCollisionOnRadioButton.setToggleGroup(autoCollisionGroup);
        autoCollisionOffRadioButton.setToggleGroup(autoCollisionGroup);
        autoCollisionOnRadioButton.setSelected(true); // TODO: a adapter plus tard

        ToggleGroup wallCrossingGroup = new ToggleGroup();
        wallCrossingOnRadioButton.setToggleGroup(wallCrossingGroup);
        wallCrossingOffRadioButton.setToggleGroup(wallCrossingGroup);
        wallCrossingOnRadioButton.setSelected(true); // TODO: a adapter plus tard
    }

    @FXML
    public void saveButtonClicked() {
        numberOfBots = (int) botsSlider.getValue();
        numberOfFood = (int) foodSlider.getValue();
        boolean autoCollision = autoCollisionOnRadioButton.isSelected();// TODO: a utiliser plus tard
        boolean wallCrossing = wallCrossingOnRadioButton.isSelected();// TODO: a utiliser plus tard

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
            Parent root = loader.load();
            Stage appStage = (Stage) saveButton.getScene().getWindow();
            Scene homePageScene = new Scene(root, appStage.getWidth(), appStage.getHeight());

            HomePageController homePageController = loader.getController();
            homePageController.setNumberOfBots(numberOfBots);
            homePageController.setNumberOfFood(numberOfFood);
            appStage.setScene(homePageScene);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Sauvegarde les paramètres ici
    }

}
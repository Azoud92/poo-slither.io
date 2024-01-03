package fr.team92.serpents.game.controller;

import java.io.IOException;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangeControlsController {

    @FXML
    private RadioButton mouseRadioButton;

    @FXML
    private RadioButton keyboardRadioButton;

    private ToggleGroup controlToggleGroup;

    @FXML
    private TextField leftKeyTextField;

    @FXML
    private TextField rightKeyTextField;

    @FXML
    private VBox keyboardControls;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        ButtonsAnimations.addAnimations(saveButton);

        controlToggleGroup = new ToggleGroup();
        mouseRadioButton.setToggleGroup(controlToggleGroup);
        keyboardRadioButton.setToggleGroup(controlToggleGroup);

        mouseRadioButton.setSelected(true);// TODO: a adapter plus tard

        controlToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == keyboardRadioButton) {
                keyboardControls.setVisible(true);
            } else {
                keyboardControls.setVisible(false);
            }
        });

        leftKeyTextField.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            event.consume();
            leftKeyTextField.setText(character.toUpperCase());
        });

        rightKeyTextField.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            event.consume();
            rightKeyTextField.setText(character.toUpperCase());
        });

        leftKeyTextField.setOnKeyPressed(event -> {
            event.consume();
            ((TextField) event.getSource()).setText(event.getCode().toString());
        });

        rightKeyTextField.setOnKeyPressed(event -> {
            event.consume();
            ((TextField) event.getSource()).setText(event.getCode().toString());
        });
    }

    private void loadHomePage(KeyCode leftKey, KeyCode rightKey) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
            Parent root = loader.load();

            HomePageController homePageController = loader.getController();
            if (keyboardRadioButton.isSelected()) {
                homePageController.setControlChoice("keyboard");
                homePageController.setLeftKey(leftKey);
                homePageController.setRightKey(rightKey);
            } else if (mouseRadioButton.isSelected()) {
                homePageController.setControlChoice("mouse");
            }

            Stage appStage = (Stage) saveButton.getScene().getWindow();
            Scene homePageScene = new Scene(root, appStage.getWidth(), appStage.getHeight());

            appStage.setScene(homePageScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private KeyCode keyCode(String keyName) {
        try {
            return KeyCode.valueOf(keyName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @FXML
    public void saveButtonClicked() {
        if (keyboardRadioButton.isSelected()) {
            String leftKey = leftKeyTextField.getText();
            String rightKey = rightKeyTextField.getText();

            if (leftKey.isEmpty() || rightKey.isEmpty()) {
                System.out.println("Veuillez entrer des touches pour les commandes de gauche et de droite.");

            } else if (leftKey.equals(rightKey)) {
                System.out
                        .println("Veuillez entrer des touches différentes pour les commandes de gauche et de droite.");
            } else {
                KeyCode leftKeyCode = keyCode(leftKey);
                KeyCode rightKeyCode = keyCode(rightKey);
                if (leftKeyCode != null && rightKeyCode != null) {
                    loadHomePage(leftKeyCode, rightKeyCode);
                }
            }
        } else if (mouseRadioButton.isSelected()) {
            loadHomePage(null, null);
        }
    }
}
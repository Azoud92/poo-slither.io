package fr.team92.serpents.game.controller;

import java.io.IOException;

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
    private TextField accelerateKeyTextField;

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

        leftKeyTextField.setOnKeyReleased(event -> {
            leftKeyTextField.setText(event.getCode().toString());
        });

        rightKeyTextField.setOnKeyReleased(event -> {
            rightKeyTextField.setText(event.getCode().toString());
        });

        accelerateKeyTextField.setOnKeyReleased(event -> {
            accelerateKeyTextField.setText(event.getCode().toString());
        });

    }

    private void loadHomePage(KeyCode leftKey, KeyCode rightKey, KeyCode accelerateKey) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
            Parent root = loader.load();

            HomePageController homePageController = loader.getController();
            if (keyboardRadioButton.isSelected()) {
                homePageController.setControlChoice("keyboard");
                homePageController.setLeftKey(leftKey);
                homePageController.setRightKey(rightKey);
                homePageController.setAccelerateKey(accelerateKey);
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
            String accelerateKey = accelerateKeyTextField.getText();

            if (!leftKey.isEmpty() && !rightKey.isEmpty() && !accelerateKey.isEmpty() && !leftKey.equals(rightKey)
                    && !leftKey.equals(accelerateKey) && !rightKey.equals(accelerateKey)) {
                KeyCode leftKeyCode = keyCode(leftKey);
                KeyCode rightKeyCode = keyCode(rightKey);
                KeyCode accelerateKeyCode = keyCode(accelerateKey);
                if (leftKeyCode != null && rightKeyCode != null && accelerateKeyCode != null) {
                    loadHomePage(leftKeyCode, rightKeyCode, accelerateKeyCode);
                }
            }
        } else if (mouseRadioButton.isSelected()) {
            loadHomePage(null, null, null);
        }
    }
}
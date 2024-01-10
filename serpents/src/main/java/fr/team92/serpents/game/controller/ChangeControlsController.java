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
    private RadioButton mouseRadioButton1;
    @FXML
    private RadioButton mouseRadioButton2;

    @FXML
    private RadioButton keyboardRadioButton1;
    @FXML
    private RadioButton keyboardRadioButton2;

    private ToggleGroup controlToggleGroup1;
    private ToggleGroup controlToggleGroup2;

    @FXML
    private TextField leftKeyTextField1;

    @FXML
    private TextField rightKeyTextField1;

    @FXML
    private TextField accelerateKeyTextField1;

    @FXML
    private VBox keyboardControls1;

    @FXML
    private TextField leftKeyTextField2;

    @FXML
    private TextField rightKeyTextField2;

    @FXML
    private TextField accelerateKeyTextField2;

    @FXML
    private VBox keyboardControls2;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {

        ButtonsAnimations.addAnimations(saveButton);

        controlToggleGroup1 = new ToggleGroup();
        mouseRadioButton1.setToggleGroup(controlToggleGroup1);
        keyboardRadioButton1.setToggleGroup(controlToggleGroup1);

        mouseRadioButton1.setSelected(true);// TODO: a adapter plus tard

        controlToggleGroup1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == keyboardRadioButton1) {
                keyboardControls1.setVisible(true);
            } else {
                keyboardControls1.setVisible(false);
            }
        });

        leftKeyTextField1.setOnKeyReleased(event -> {
            leftKeyTextField1.setText(event.getCode().toString());
        });

        rightKeyTextField1.setOnKeyReleased(event -> {
            rightKeyTextField1.setText(event.getCode().toString());
        });

        accelerateKeyTextField1.setOnKeyReleased(event -> {
            accelerateKeyTextField1.setText(event.getCode().toString());
        });

        controlToggleGroup2 = new ToggleGroup();
        mouseRadioButton2.setToggleGroup(controlToggleGroup2);
        keyboardRadioButton2.setToggleGroup(controlToggleGroup2);

        mouseRadioButton2.setSelected(true);// TODO: a adapter plus tard

        controlToggleGroup2.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == keyboardRadioButton2) {
                keyboardControls2.setVisible(true);
            } else {
                keyboardControls2.setVisible(false);
            }
        });

        leftKeyTextField2.setOnKeyReleased(event -> {
            leftKeyTextField2.setText(event.getCode().toString());
        });

        rightKeyTextField2.setOnKeyReleased(event -> {
            rightKeyTextField2.setText(event.getCode().toString());
        });

        accelerateKeyTextField2.setOnKeyReleased(event -> {
            accelerateKeyTextField2.setText(event.getCode().toString());
        });

    }

    private void loadHomePage(KeyCode leftKey1, KeyCode rightKey1, KeyCode accelerateKey1, KeyCode leftKey2,
            KeyCode rightKey2, KeyCode accelerateKey2) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
            Parent root = loader.load();

            HomePageController homePageController = loader.getController();
            if (keyboardRadioButton1.isSelected()) {
                homePageController.setControlChoice("keyboard", true);
                homePageController.setLeftKey(leftKey1, true);
                homePageController.setRightKey(rightKey1, true);
                homePageController.setAccelerateKey(accelerateKey1, true);
            } else if (mouseRadioButton1.isSelected()) {
                homePageController.setControlChoice("mouse", true);
            }

            if (keyboardRadioButton2.isSelected()) {
                homePageController.setControlChoice("keyboard", false);
                homePageController.setLeftKey(leftKey2, false);
                homePageController.setRightKey(rightKey2, false);
                homePageController.setAccelerateKey(accelerateKey2, false);
            } else if (mouseRadioButton2.isSelected()) {
                homePageController.setControlChoice("mouse", false);
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
        if (mouseRadioButton1.isSelected() && mouseRadioButton2.isSelected()) {
            return;
        }

        KeyCode leftKeyCode1 = null;
        KeyCode rightKeyCode1 = null;
        KeyCode accelerateKeyCode1 = null;

        if (keyboardRadioButton1.isSelected()) {
            String leftKey1 = leftKeyTextField1.getText();
            String rightKey1 = rightKeyTextField1.getText();
            String accelerateKey1 = accelerateKeyTextField1.getText();

            if (!leftKey1.isEmpty() && !rightKey1.isEmpty() && !accelerateKey1.isEmpty() && !leftKey1.equals(rightKey1)
                    && !leftKey1.equals(accelerateKey1) && !rightKey1.equals(accelerateKey1)) {
                leftKeyCode1 = keyCode(leftKey1);
                rightKeyCode1 = keyCode(rightKey1);
                accelerateKeyCode1 = keyCode(accelerateKey1);
            }
        }

        KeyCode leftKeyCode2 = null;
        KeyCode rightKeyCode2 = null;
        KeyCode accelerateKeyCode2 = null;

        if (keyboardRadioButton2.isSelected()) {
            String leftKey2 = leftKeyTextField2.getText();
            String rightKey2 = rightKeyTextField2.getText();
            String accelerateKey2 = accelerateKeyTextField2.getText();

            if (!leftKey2.isEmpty() && !rightKey2.isEmpty() && !accelerateKey2.isEmpty() && !leftKey2.equals(rightKey2)
                    && !leftKey2.equals(accelerateKey2) && !rightKey2.equals(accelerateKey2)) {
                leftKeyCode2 = keyCode(leftKey2);
                rightKeyCode2 = keyCode(rightKey2);
                accelerateKeyCode2 = keyCode(accelerateKey2);
            }
        }

        if ((mouseRadioButton1.isSelected()
                || (leftKeyCode1 != null && rightKeyCode1 != null && accelerateKeyCode1 != null))
                && (mouseRadioButton2.isSelected()
                        || (leftKeyCode2 != null && rightKeyCode2 != null && accelerateKeyCode2 != null))) {
            loadHomePage(leftKeyCode1, rightKeyCode1, accelerateKeyCode1, leftKeyCode2, rightKeyCode2,
                    accelerateKeyCode2);
        }
    }
}
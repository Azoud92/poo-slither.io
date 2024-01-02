package fr.team92.serpents.game.controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class ChangeControlsController {

    @FXML
    private RadioButton mouseRadioButton;

    @FXML
    private RadioButton keyboardRadioButton;

    @FXML
    private ToggleGroup controlToggleGroup;

    @FXML
    private TextField leftKeyTextField;

    @FXML
    private TextField rightKeyTextField;

    @FXML
    private VBox keyboardControls;

    @FXML
    public void initialize() {
        // Ajouter un écouteur pour le groupe de boutons radio
        controlToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == keyboardRadioButton) {
                keyboardControls.setVisible(true);
            } else {
                keyboardControls.setVisible(false);
            }
        });

        // Ajouter des écouteurs pour les champs de texte
        leftKeyTextField.setOnKeyPressed(event -> leftKeyTextField.setText(event.getCode().toString()));
        rightKeyTextField.setOnKeyPressed(event -> rightKeyTextField.setText(event.getCode().toString()));
    }

    @FXML
    public void saveButtonClicked() {
        if (keyboardRadioButton.isSelected()) {
            String leftKey = leftKeyTextField.getText();
            String rightKey = rightKeyTextField.getText();

            if (leftKey.isEmpty() || rightKey.isEmpty()) {
                // Afficher un message d'erreur si les champs de texte sont vides
                System.out.println("Veuillez entrer des touches pour les commandes de gauche et de droite.");
            } else if (leftKey.equals(rightKey)) {
                // Afficher un message d'erreur si les touches sont identiques
                System.out
                        .println("Veuillez entrer des touches différentes pour les commandes de gauche et de droite.");
            } else {
                // Sauvegarder les commandes
                KeyCode leftKeyCode = KeyCode.getKeyCode(leftKey);
                KeyCode rightKeyCode = KeyCode.getKeyCode(rightKey);

                // TODO: Utilisez leftKeyCode et rightKeyCode pour configurer les commandes du
                // jeu
            }
        } else {
            // TODO: Configurer le jeu pour utiliser les commandes de la souris
        }
    }
}
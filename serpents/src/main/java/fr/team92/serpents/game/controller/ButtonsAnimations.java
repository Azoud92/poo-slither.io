package fr.team92.serpents.game.controller;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ButtonsAnimations {

    public static void addAnimations(Button button) {
        // effet d'ombre quand la souris passe sur le bouton
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(new DropShadow(20, Color.BLACK));
        });

        // Supprime l'effet d'ombre quand la souris quitte le bouton
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(null);
        });

        // animation de zoom lorsque le bouton est cliqué
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.5);
            st.setToY(1.5);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
        });
    }
}

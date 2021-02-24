package com.github.woocash2.Chess.controller;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import com.github.woocash2.Chess.model.Piece;
import javafx.util.Duration;

public class GameResult {
    private final Label result;

    public GameResult(Label label) {
        result = label;
        result.setOnMouseClicked(e -> {
            FadeTransition transitionIn = new FadeTransition(new Duration(200), result);
            transitionIn.setFromValue(1.0);
            transitionIn.setToValue(0.0);
            transitionIn.setOnFinished(e1 -> {
                label.setVisible(false);
            });
            transitionIn.play();
        });
    }

    public void show(Piece.Team winner) {
        if (winner == null)
            result.setText("DRAW");
        if (winner == Piece.Team.WHITE)
            result.setText("WHITE WINS");
        if (winner == Piece.Team.BLACK)
            result.setText("BLACK WINS");

        result.setOpacity(0.0);
        result.setVisible(true);
        FadeTransition transitionIn = new FadeTransition(new Duration(200), result);
        transitionIn.setFromValue(0.0);
        transitionIn.setToValue(1.0);
        transitionIn.setOnFinished(e -> {
            result.setOpacity(1.0);
        });
        transitionIn.play();
    }
}

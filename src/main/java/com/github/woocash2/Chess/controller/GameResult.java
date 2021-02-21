package com.github.woocash2.Chess.controller;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Piece;
import javafx.util.Duration;

public class GameResult {
    private final Label result;

    public GameResult(Label label) {
        result = label;
    }

    public void show(Piece.team winner) {
        if (winner == null)
            result.setText("DRAW");
        if (winner == Piece.team.WHITE)
            result.setText("WHITE WINS");
        if (winner == Piece.team.BLACK)
            result.setText("BLACK WINS");

        result.setVisible(true);
        FadeTransition transitionIn = new FadeTransition(new Duration(500), result);
        transitionIn.setFromValue(0.0);
        transitionIn.setToValue(1.0);
        transitionIn.play();
        SequentialTransition seqTransition = new SequentialTransition();
    }
}

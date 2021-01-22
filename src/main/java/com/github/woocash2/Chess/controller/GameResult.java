package com.github.woocash2.Chess.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Piece;

public class GameResult {
    private Label result;
    private Rectangle background;
    private Button okButton;

    public GameResult(Label label, Rectangle rectangle, Button button) {
        result = label;
        background = rectangle;
        okButton = button;
        okButton.setOnAction(e -> hide());
    }

    public void show(Piece.team winner) {
        if (winner == null)
            result.setText("DRAW");
        if (winner == Piece.team.WHITE)
            result.setText("WHITE WINS");
        if (winner == Piece.team.BLACK)
            result.setText("BLACK WINS");

        background.setVisible(true);
        result.setVisible(true);
        okButton.setVisible(true);
    }

    public void hide() {
        background.setVisible(false);
        result.setVisible(false);
        okButton.setVisible(false);
    }
}

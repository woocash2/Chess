package com.github.woocash2.Chess.model.utils;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.function.Function;

public interface LabelProvider {

    public static Label getLabel(String s) {
        Label label = new Label(s);
        label.setTextFill(Color.WHITE);
        label.setPrefSize(100, 100);
        label.setFont(new Font(25));
        label.setPadding(new Insets(10, 15, 10, 15));
        return label;
    };
}

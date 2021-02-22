package com.github.woocash2.Chess.model.utils;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public interface CoordinateProvider {
    public static Pair<Integer, Integer> tileCoordsFromMousePosition(double a, double b) {
        int x = (int) a;
        x /= 100;
        if (x == 8)
            x--;
        int y = (int) b;
        y /= 100;
        if (y == 8)
            y--;
        return new Pair<>(y, x);
    }
}

package com.github.woocash2.Chess.model.utils;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public interface CoordinateProvider {
    public static Pair<Integer, Integer> tileCoordsFromMousePosition(MouseEvent e) {
        int x = (int) e.getX();
        x /= 100;
        if (x == 8)
            x--;
        int y = (int) e.getY();
        y /= 100;
        if (y == 8)
            y--;
        return new Pair<>(y, x);
    }
}

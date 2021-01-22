package com.github.woocash2.Chess.model.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import com.github.woocash2.Chess.model.Piece;

public interface ImageCropper {

    public static Image grid = new Image("/image/pieces.png");

    private static WritableImage cropImg(int x, int y) {
        x *= 212;
        x += 10;
        y *= 212;
        y += 10;
        return new WritableImage(grid.getPixelReader(), x, y, 200, 200);
    }

    public static WritableImage getImage(Piece piece) {
        return getImageByName(piece.onBoard);
    }

    public static WritableImage getImageByName(char name) {
        int x, y;
        if (Character.isLowerCase(name)) // this means White
            y = 0;
        else
            y = 1;

        x = switch (Character.toLowerCase(name)) {
            case 'k' -> 0;
            case 'q' -> 1;
            case 'b' -> 2;
            case 'n' -> 3;
            case 'r' -> 4;
            default -> 5;
        };

        return cropImg(x, y);
    }
}

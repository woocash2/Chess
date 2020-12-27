package model.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import model.Piece;

public interface ImageCropper {

    public static Image grid = new Image("/view/img/pieces.png");

    private static WritableImage cropImg(int x, int y) {
        x *= 212;
        x += 10;
        y *= 212;
        y += 10;
        return new WritableImage(grid.getPixelReader(), x, y, 200, 200);
    }

    public static WritableImage getImage(Piece piece) {
        int x, y;
        if (piece.getColor() == Piece.team.WHITE)
            y = 0;
        else
            y = 1;

        x = switch (Character.toLowerCase(piece.getOnBoard())) {
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

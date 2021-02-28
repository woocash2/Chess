package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.PieceFactory;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public interface ImageCropper {

    public static Image grid = new Image("/image/pieces.png");

    private static WritableImage cropImg(int x, int y) {
        x *= 212;
        x += 10;
        y *= 212;
        y += 10;
        return new WritableImage(grid.getPixelReader(), x, y, 200, 200);
    }

    public static WritableImage getImageByTeamAndType(Board.Team team, Board.Piece piece) {
        int x, y;
        if (team == Board.Team.WHITE) // this means White
            y = 0;
        else
            y = 1;

        x = switch (piece) {
            case KING, KINGM -> 0;
            case QUEEN -> 1;
            case BISHOP -> 2;
            case KNIGHT -> 3;
            case ROOK, ROOKM -> 4;
            case PAWN, PAWNM, PAWNJ -> 5;
            default -> 0;
        };

        return cropImg(x, y);
    }
}

package controller;

import javafx.scene.image.ImageView;
import javafx.util.Pair;
import model.Pawn;
import model.Piece;
import model.utils.ImageCropper;

public class PieceImg extends ImageView {

    protected GameController gameController;
    protected Piece piece;

    public PieceImg(Piece piece, GameController gameController) {
        super(ImageCropper.getImage(piece));
        setFitWidth(100);
        setFitHeight(100);
        this.piece = piece;
        this.gameController = gameController;
    }

    public void move(int x, int y) {
        piece.move(x, y);
        if (piece.getClass() == Pawn.class && !((Pawn)piece).moved)
            ((Pawn)piece).moved = true;
        gameController.piecesGrid.getChildren().remove(this);
        gameController.piecesGrid.add(this, y, x);
    }

    public void showReachableAndTakeable() {
        piece.updatePositions();
        for (Pair<Integer, Integer> pos : piece.reachablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeReachable();
        }
        for (Pair<Integer, Integer> pos : piece.takeablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeTakeable();
        }
    }

    public void hideReachableAndTakeable() {
        for (Pair<Integer, Integer> pos : piece.reachablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeUnreachable();
        }
        for (Pair<Integer, Integer> pos : piece.takeablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeUnTakeable();
        }
    }

    public void die() {
        gameController.piecesGrid.getChildren().remove(this);
    }
}

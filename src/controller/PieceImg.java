package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;
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
        gameController.piecesGrid.getChildren().remove(this);
        gameController.piecesGrid.add(this, y, x);
    }

    public void displayReachableTiles() {
        piece.updateReachablePositions();
        for (Pair<Integer, Integer> pos : piece.reachablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeReachable();
        }
    }

    public void hideReachableTiles() {
        for (Pair<Integer, Integer> pos : piece.reachablePositions) {
            int x = pos.getKey(), y = pos.getValue();
            gameController.tiles[x][y].makeUnreachable();
        }
    }
}

package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
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
}

package com.github.woocash2.Chess.controller;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.Pair;
import com.github.woocash2.Chess.model.King;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.ImageCropper;

public class PieceImg extends ImageView {

    private final double transitionTime = 0.25;

    protected GameController gameController;
    protected Piece piece;

    public PieceImg(Piece piece, GameController gameController) {
        super(ImageCropper.getImage(piece));
        setFitWidth(100);
        setFitHeight(100);
        this.piece = piece;
        this.gameController = gameController;
    }

    public void move(Tile target) {
        int x = target.x;
        int y = target.y;
        if (piece.getClass() == King.class && Math.abs(piece.y - y) > 1) { // this means castling
            if (piece.y > y) {
                gameController.selectedPiece = piece.color == Piece.team.WHITE ? gameController.whiteRooks.get(0) : gameController.blackRooks.get(0);
                gameController.tiles[x][y + 1].makeMoveToUs();
            }
            else {
                gameController.selectedPiece = piece.color == Piece.team.WHITE ? gameController.whiteRooks.get(1) : gameController.blackRooks.get(1);
                gameController.tiles[x][y - 1].makeMoveToUs();
            }
            gameController.notifyTurnMade();
        }

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);
        transition.setToX(target.getCenter().getKey() - getX());
        transition.setToY(target.getCenter().getValue() - getY());
        transition.play();

        piece.move(x, y);
    }

    public void placeInstantly(Tile tile) {
        double x = tile.getCenter().getKey();
        double y = tile.getCenter().getValue();
        setX(x);
        setY(y);
    }

    public void showReachableAndTakeable() {
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
        gameController.pieces.remove(this);
        gameController.piecesAnchor.getChildren().remove(this);
    }
}

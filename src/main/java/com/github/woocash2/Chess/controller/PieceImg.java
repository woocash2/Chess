package com.github.woocash2.Chess.controller;

import javafx.scene.image.ImageView;
import javafx.util.Pair;
import com.github.woocash2.Chess.model.King;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.ImageCropper;

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

        piece.move(x, y);
        gameController.piecesGrid.getChildren().remove(this);
        gameController.piecesGrid.add(this, y, x);
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
        gameController.piecesGrid.getChildren().remove(this);
    }
}
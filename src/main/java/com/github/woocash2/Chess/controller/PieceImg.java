package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Move;
import com.github.woocash2.Chess.model.utils.PositionUpdater;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.ImageCropper;

public class PieceImg extends ImageView {

    public GameController gameController;
    Board board;
    public Board.Piece piece;
    public Board.Team team;
    public int x, y;

    public PieceImg(Board board, Board.Piece piece, Board.Team team, int x, int y, GameController gameController) {
        super(ImageCropper.getImageByTeamAndType(team, piece));
        setFitWidth(100);
        setFitHeight(100);
        this.board = board;
        this.piece = piece;
        this.team = team;
        this.gameController = gameController;
        this.x = x;
        this.y = y;
    }

    public void move(Tile target, Board.Piece promo, boolean took) {
        int a = target.x;
        int b = target.y;

        Move move = PositionUpdater.getMove(board, x, y ,a ,b);

        if (promo != null)
            move.post = promo;

        Move additional = board.move(move);
        x = a;
        y = b;

        piece = board.pieces[x][y];
        gameController.boardManager.handleAdditional(additional);
        gameController.boardCover.setVisible(true);
        playTransitionAndNotifyTurn(target, took);
    }

    public void placeInstantly(Tile tile) {
        double x = tile.getCenter().getKey();
        double y = tile.getCenter().getValue();
        setX(x);
        setY(y);
    }

    public void playTransitionAndNotifyTurn(Tile target, boolean took) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);
        transition.setToX(target.getCenter().getKey() - getX());
        transition.setToY(target.getCenter().getValue() - getY());

        transition.setOnFinished(e -> {
            setX(getX() + translateXProperty().getValue());
            setY(getY() + translateYProperty().getValue());
            translateXProperty().set(0);
            translateYProperty().set(0);
            gameController.turnManager.notifyTurnMade();
        });

        if (took) gameController.soundPlayer.playCapture();
        else gameController.soundPlayer.playMove();
        transition.play();
    }

    public void playTransition(Tile target) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);
        transition.setToX(target.getCenter().getKey() - getX());
        transition.setToY(target.getCenter().getValue() - getY());

        transition.setOnFinished(e -> {
            setX(getX() + translateXProperty().getValue());
            setY(getY() + translateYProperty().getValue());
            translateXProperty().set(0);
            translateYProperty().set(0);
        });
        transition.play();
    }

    public void transform(Board.Piece piece) {
        setImage(ImageCropper.getImageByTeamAndType(team, piece));
        this.piece = piece;
    }

    public void die() {
        gameController.boardManager.pieces.remove(this);
        gameController.piecesPane.getChildren().remove(this);
    }
}

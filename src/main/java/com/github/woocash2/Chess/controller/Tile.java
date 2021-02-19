package com.github.woocash2.Chess.controller;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Piece;
import javafx.util.Pair;

public class Tile extends Rectangle {

    private final Board board;
    public final int x, y;

    protected boolean reachable = false;
    protected boolean takeable = false;

    protected Circle reachShadow;
    protected Rectangle takeShadow;
    protected Group shadows;

    private final GameController gameController;

    PieceImg pieceImg;

    public Tile(Board board, int x, int y, Color color, GameController gameController) {
        super(100, 100, color);
        this.x = x;
        this.y = y;
        this.board = board;
        this.setX(x);
        this.setY(y);
        this.gameController = gameController;

        reachShadow = new Circle(15, Color.DARKGRAY);
        reachShadow.setMouseTransparent(true);
        reachShadow.setVisible(false);

        takeShadow = new Rectangle(90, 90, Color.TRANSPARENT);
        takeShadow.setStrokeWidth(10);
        takeShadow.setStroke(Color.DARKGRAY);
        takeShadow.setMouseTransparent(true);
        takeShadow.setVisible(false);

        shadows = new Group();
        shadows.getChildren().add(reachShadow);
        shadows.getChildren().add(takeShadow);
        shadows.setMouseTransparent(true);

        setOnMousePressed(e -> {
            PieceImg selected = gameController.selectedPiece;

            if (selected == null && pieceImg != null) {
                // select our piece
                if (gameController.turn == pieceImg.piece.color) {
                    gameController.selectedPiece = pieceImg;
                    pieceImg.showReachableAndTakeable();
                }
            }
            else if (selected != null && pieceImg == selected) {
                // cancel our selection
                selected.hideReachableAndTakeable();
                gameController.selectedPiece = null;
            }
            else if (selected != null && pieceImg != null && pieceImg != selected) {
                // change selection to our piece
                if (gameController.turn == pieceImg.piece.color) {
                    selected.hideReachableAndTakeable();
                    gameController.selectedPiece = pieceImg;
                    pieceImg.showReachableAndTakeable();
                }
                else if (takeable) { // take opponent's piece
                    pieceImg.die();
                    makeMoveToUs();
                }
            }
            else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
                makeMoveToUs();
            }
            else if (selected != null && pieceImg == null && takeable) {  // that can only be en passant
                Tile tile;
                if (gameController.selectedPiece.piece.color == Piece.team.WHITE)
                    tile = gameController.tiles[x + 1][y];
                else
                    tile = gameController.tiles[x - 1][y];
                // decompose en passant into two separate moves
                tile.pieceImg.die();
                tile.makeMoveToUs();
                gameController.notifyTurnMade();
                gameController.selectedPiece = selected;
                makeMoveToUs();
            }
        });
    }

    public void makeMoveToUs() {
        PieceImg selected = gameController.selectedPiece;
        int a = selected.piece.x;
        int b = selected.piece.y;
        selected.hideReachableAndTakeable();
        gameController.tiles[a][b].takePieceFrom();
        selected.move(this);
        putPieceOn(selected);
        gameController.selectedPiece = null;
        gameController.notifyTurnMade();
    }

    public void makeReachable() {
        reachable = true;
        reachShadow.setVisible(true);
    }

    public void makeUnreachable() {
        reachable = false;
        reachShadow.setVisible(false);
    }

    public void makeTakeable() {
        takeable = true;
        takeShadow.setVisible(true);
    }

    public void makeUnTakeable() {
        takeable = false;
        takeShadow.setVisible(false);
    }

    private boolean selectedPieceStandsOn() {
        return gameController.selectedPiece.piece.x == x && gameController.selectedPiece.piece.y == y;
    }

    public void putPieceOn(PieceImg img) {
        pieceImg = img;
    }

    public void takePieceFrom() {
        pieceImg = null;
    }

    Pair<Double, Double> getCenter() {
        return new Pair<>(y * 100.0, x * 100.0);
    }
}

package com.github.woocash2.Chess.controller;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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
    protected Circle takeShadow;
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

        takeShadow = new Circle(0, 0, 40, Color.TRANSPARENT);
        takeShadow.setStrokeWidth(10);
        takeShadow.setStroke(Color.DARKGRAY);
        takeShadow.setMouseTransparent(true);
        takeShadow.setVisible(false);

        shadows = new Group();
        shadows.getChildren().add(reachShadow);
        shadows.getChildren().add(takeShadow);
        shadows.setMouseTransparent(true);
        setMouseTransparent(true);
    }

    public void mousePressBehavior(MouseEvent e) {
        PieceImg selected = gameController.selectedPiece;

        if (selected == null && pieceImg != null) {
            // select our piece
            if (gameController.turn == pieceImg.piece.color) {
                makeSelection(e);
            }
        }
        else if (selected != null && pieceImg == selected) {
            gameController.repositionSelected(e);
        }
        else if (selected != null && pieceImg != null && pieceImg != selected) {
            // change selection to our piece
            if (gameController.turn == pieceImg.piece.color) {
                selected.hideReachableAndTakeable();
                makeSelection(e);
            }
            else if (takeable) { // take opponent's piece
                pieceImg.die();
                makeMoveToUs();
                gameController.notifyTurnMade();
            }
            else {
                deselect();
            }
        }
        else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
            makeMoveToUs();
            gameController.notifyTurnMade();
        }
        else if (selected != null && pieceImg == null && takeable) {  // that can only be en passant
            enPassantTake();
        }
        else if (selected != null) {
            deselect();
        }
    }

    public void mouseReleaseBehavoiur(MouseEvent e) {
        PieceImg selected = gameController.selectedPiece;
        if (selected == null)
            return;

        gameController.releaseCnt++;

        if (selected == pieceImg) {
            if (gameController.releaseCnt > 1) {
                gameController.restoreSelectedPosition();
                deselect();
                gameController.releaseCnt = 0;
            }
            else
                gameController.restoreSelectedPosition();
        }
        else if (selected != null && pieceImg != null && pieceImg != selected) {
            // change selection to our piece
            if (takeable) { // take opponent's piece
                pieceImg.die();
                selected.placeInstantly(this);
                makeMoveToUs();
                gameController.notifyTurnMade();
            }
            else
                gameController.restoreSelectedPosition();
        }
        else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
            selected.placeInstantly(this);
            makeMoveToUs();
            gameController.notifyTurnMade();
        }
        else if (selected != null && pieceImg == null && takeable) {  // that can only be en passant
            selected.placeInstantly(this);
            enPassantTake();
        }
        else if (selected != null && pieceImg == null) {
            gameController.restoreSelectedPosition();
        }
    }

    public void makeSelection(MouseEvent e) {
        gameController.releaseCnt = 0;
        gameController.selectedPiece = pieceImg;
        gameController.selectedOriginX = pieceImg.getX();
        gameController.selectedOriginY = pieceImg.getY();
        pieceImg.showReachableAndTakeable();
        gameController.putSelectedOnTop();
        gameController.repositionSelected(e);
    }

    public void deselect() {
        gameController.selectedPiece.hideReachableAndTakeable();
        gameController.selectedPiece = null;
    }

    public void enPassantTake() {
        PieceImg selected = gameController.selectedPiece;
        Tile tile;
        if (gameController.selectedPiece.piece.color == Piece.team.WHITE)
            tile = gameController.tiles[x + 1][y];
        else
            tile = gameController.tiles[x - 1][y];
        // decompose en passant into two separate moves
        tile.pieceImg.die();
        gameController.selectedPiece = selected;
        makeMoveToUs();
        gameController.notifyTurnMade();
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

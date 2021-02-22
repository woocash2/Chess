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
    public Color defaultColor;
    public static Color highlightColor = Color.LIGHTBLUE;

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
        this.defaultColor = color;

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
        PieceImg selected = gameController.actionManager.selectedPiece;

        if (selected == null && pieceImg != null) {
            // select our piece
            if (gameController.turnManager.turn == pieceImg.piece.color) {
                makeSelection(e);
            }
        }
        else if (selected != null && pieceImg == selected) {
            gameController.actionManager.repositionSelected(e);
        }
        else if (selected != null && pieceImg != null && pieceImg != selected) {
            // change selection to our piece
            if (gameController.turnManager.turn == pieceImg.piece.color) {
                deselect();
                makeSelection(e);
            }
            else if (takeable) { // take opponent's piece
                pieceImg.die();
                makeMoveToUs();
                gameController.soundPlayer.playCapture();
                gameController.turnManager.notifyTurnMade();
            }
            else {
                deselect();
            }
        }
        else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
            makeMoveToUs();
            gameController.soundPlayer.playMove();
            gameController.turnManager.notifyTurnMade();
        }
        else if (selected != null && pieceImg == null && takeable) {  // that can only be en passant
            gameController.soundPlayer.playCapture();
            enPassantTake();
        }
        else if (selected != null) {
            deselect();
        }
    }

    public void mouseReleaseBehavoiur(MouseEvent e) {
        PieceImg selected = gameController.actionManager.selectedPiece;
        if (selected == null)
            return;

        gameController.actionManager.releaseCnt++;

        if (selected == pieceImg) {
            if (gameController.actionManager.releaseCnt > 1) {
                gameController.actionManager.restoreSelectedPosition();
                deselect();
                gameController.actionManager.releaseCnt = 0;
            }
            else
                gameController.actionManager.restoreSelectedPosition();
        }
        else if (selected != null && pieceImg != null && pieceImg != selected) {
            if (takeable) { // take opponent's piece
                pieceImg.die();
                selected.placeInstantly(this);
                makeMoveToUs();
                gameController.soundPlayer.playCapture();
                gameController.turnManager.notifyTurnMade();
            }
            else
                gameController.actionManager.restoreSelectedPosition();
        }
        else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
            selected.placeInstantly(this);
            makeMoveToUs();
            gameController.soundPlayer.playMove();
            gameController.turnManager.notifyTurnMade();
        }
        else if (selected != null && pieceImg == null && takeable) {  // that can only be en passant
            selected.placeInstantly(this);
            gameController.soundPlayer.playCapture();
            enPassantTake();
        }
        else if (selected != null && pieceImg == null) {
            gameController.actionManager.restoreSelectedPosition();
        }
    }

    public void makeSelection(MouseEvent e) {
        gameController.actionManager.releaseCnt = 0;
        gameController.actionManager.selectedPiece = pieceImg;
        gameController.actionManager.selectedTile = this;
        gameController.actionManager.selectedOriginX = pieceImg.getX();
        gameController.actionManager.selectedOriginY = pieceImg.getY();
        pieceImg.showReachableAndTakeable();
        gameController.actionManager.putSelectedOnTop();
        gameController.actionManager.repositionSelected(e);
        setFill(highlightColor);
    }

    public void deselect() {
        gameController.actionManager.selectedPiece.hideReachableAndTakeable();
        if (gameController.actionManager.selectedTile != null)
            gameController.actionManager.selectedTile.setFill(gameController.actionManager.selectedTile.defaultColor);
        gameController.actionManager.selectedPiece = null;
        gameController.actionManager.selectedTile = null;
    }

    public void enPassantTake() {
        PieceImg selected = gameController.actionManager.selectedPiece;
        Tile tile;
        if (gameController.actionManager.selectedPiece.piece.color == Piece.team.WHITE)
            tile = gameController.boardManager.tiles[x + 1][y];
        else
            tile = gameController.boardManager.tiles[x - 1][y];
        // decompose en passant into two separate moves
        tile.pieceImg.die();
        gameController.actionManager.selectedPiece = selected;
        makeMoveToUs();
        gameController.turnManager.notifyTurnMade();
    }

    public void makeMoveToUs() {
        PieceImg selected = gameController.actionManager.selectedPiece;
        int a = selected.piece.x;
        int b = selected.piece.y;
        gameController.boardManager.tiles[a][b].takePieceFrom();
        putPieceOn(selected);
        deselect();
        selected.move(this);
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
        return gameController.actionManager.selectedPiece.piece.x == x && gameController.actionManager.selectedPiece.piece.y == y;
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

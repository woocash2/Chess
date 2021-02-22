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

    public void mousePressBehavior(double x, double y) {
        PieceImg selected = gameController.actionManager.selectedPiece;

        // What is the type of the mouse press?

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.piece.color == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.piece.color == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.piece.color != gameController.turnManager.turn) && !takeable;
        boolean onEmpty = (pieceImg == null) && !reachable && !takeable;
        boolean onReachable = reachable;
        boolean onTakeableNonEmpty = takeable && (pieceImg != null);
        boolean onTakeableEmpty = takeable && (pieceImg == null);

        if (onOurNotSelected) {
            deselect();
            makeSelection(x, y);
            gameController.actionManager.repositionSelected(x, y);
        }
        else if (onOurSelected) {
            gameController.actionManager.repositionSelected(x, y);
        }
        else if (onTheirNotTakeable || onEmpty) {
            deselect();
        }
        else if (onReachable) { // castling is being handled in PieceImg class
            gameController.soundPlayer.playMove();
            makeMoveToUs();
            gameController.turnManager.notifyTurnMade();
        }
        else if (onTakeableNonEmpty) {
            gameController.soundPlayer.playCapture();
            pieceImg.die();
            makeMoveToUs();
            gameController.turnManager.notifyTurnMade();
        }
        else if (onTakeableEmpty) { // en passant
            gameController.soundPlayer.playCapture();
            enPassantTake();
        }
    }

    public void mouseReleaseBehavoiur(MouseEvent e) {
        PieceImg selected = gameController.actionManager.selectedPiece;
        if (selected == null)
            return;

        gameController.actionManager.releaseCnt++;

        // What is the type of the mouse release?

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.piece.color == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.piece.color == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.piece.color != gameController.turnManager.turn) && !takeable;
        boolean onEmpty = (pieceImg == null) && !reachable && !takeable;
        boolean onReachable = reachable;
        boolean onTakeableNonEmpty = takeable && (pieceImg != null);
        boolean onTakeableEmpty = takeable && (pieceImg == null);

        if (onOurSelected) {
            if (gameController.actionManager.releaseCnt > 1) {
                gameController.actionManager.restoreSelectedPosition();
                deselect();
                gameController.actionManager.releaseCnt = 0;
            }
            else
                gameController.actionManager.restoreSelectedPosition();
        }
        else if (onOurNotSelected || onTheirNotTakeable || onEmpty) {
            gameController.actionManager.restoreSelectedPosition();
        }
        else if (onReachable) { // castling is being handled in PieceImg class
            selected.placeInstantly(this);
            makeMoveToUs();
            gameController.soundPlayer.playMove();
            gameController.turnManager.notifyTurnMade();
        }
        else if (onTakeableNonEmpty) {
            gameController.soundPlayer.playCapture();
            pieceImg.die();
            selected.placeInstantly(this);
            makeMoveToUs();
            gameController.turnManager.notifyTurnMade();
        }
        else if (onTakeableEmpty) { // en passant
            gameController.soundPlayer.playCapture();
            selected.placeInstantly(this);
            enPassantTake();
        }
    }

    public void makeSelection(double x, double y) {
        gameController.actionManager.releaseCnt = 0;
        gameController.actionManager.selectedPiece = pieceImg;
        gameController.actionManager.selectedTile = this;
        gameController.actionManager.selectedOriginX = pieceImg.getX();
        gameController.actionManager.selectedOriginY = pieceImg.getY();
        pieceImg.showReachableAndTakeable();
        gameController.actionManager.putSelectedOnTop();
        setFill(highlightColor);
    }

    public void deselect() {
        if (gameController.actionManager.selectedPiece != null)
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
        tile.pieceImg.die();
        tile.takePieceFrom();
        gameController.boardManager.board.takeAway(tile.x, tile.y);

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

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
    protected Circle takeShadow;
    protected Group shadows;
    public Color defaultColor;
    public static Color highlightColor = Color.LIGHTBLUE;

    public final GameController gameController;
    public PieceImg pieceImg;

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

    public void mousePressBehavior(double a, double b) {
        PieceImg selected = gameController.actionManager.selectedPiece;

        // What is the type of the mouse press?

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.piece.team == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.piece.team == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.piece.team != gameController.turnManager.turn) && !takeable;
        boolean onEmpty = (pieceImg == null) && !reachable && !takeable;
        boolean onReachable = reachable;
        boolean onTakeable = takeable;

        if (onOurNotSelected) {
            deselect();
            makeSelection(a, b);
            gameController.actionManager.repositionSelected(a, b);
        }
        else if (onOurSelected) {
            gameController.actionManager.repositionSelected(a, b);
        }
        else if (onTheirNotTakeable || onEmpty) {
            deselect();
        }
        else if (onReachable) { // castling is being handled in PieceImg class
            gameController.soundPlayer.playMove();
            boolean notifyTurn = !willReachPromotion();
            makeMoveToUs(notifyTurn);
        }
        else if (onTakeable) {
            gameController.soundPlayer.playCapture();
            if (pieceImg != null)
                pieceImg.die();
            boolean notifyTurn = !willReachPromotion();
            makeMoveToUs(notifyTurn);
        }
    }

    public void mouseReleaseBehavoiur(double x, double y) {
        PieceImg selected = gameController.actionManager.selectedPiece;
        if (selected == null)
            return;

        gameController.actionManager.releaseCnt++;

        // What is the type of the mouse release?

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.piece.team == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.piece.team == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.piece.team != gameController.turnManager.turn) && !takeable;
        boolean onEmpty = (pieceImg == null) && !reachable && !takeable;
        boolean onReachable = reachable;
        boolean onTakeable = takeable;

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
        else if (onReachable) {
            gameController.soundPlayer.playMove();
            selected.placeInstantly(this);
            boolean notifyTurn = !willReachPromotion();
            makeMoveToUs(notifyTurn);
        }
        else if (onTakeable) {
            gameController.soundPlayer.playCapture();
            if (pieceImg != null)
                pieceImg.die();
            selected.placeInstantly(this);
            boolean notifyTurn = !willReachPromotion();
            makeMoveToUs(notifyTurn);
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

    public boolean willReachPromotion() {
        PieceImg selected = gameController.actionManager.selectedPiece;
        if (selected.piece.type == Piece.Type.PAWN) {
            int a = selected.piece.x;
            if ((selected.piece.team == Piece.Team.WHITE && a == 1 && x == 0) || (selected.piece.team == Piece.Team.BLACK && a == 6 && x == 7)) {
                if (gameController.turnManager.computerGame && gameController.turnManager.turn != gameController.turnManager.playerTeam) {
                    selected.hideReachableAndTakeable();
                    gameController.actionManager.promotionPanel.chooseType(selected, gameController.turnManager.computerPromoType);
                    return false; // rather unpleasant workaround
                }
                else
                    gameController.actionManager.promotionPanel.show(selected);
                return true;
            }
        }
        return false;
    }

    public void makeMoveToUs(boolean notifyTurn) {
        PieceImg selected = gameController.actionManager.selectedPiece;
        int a = selected.piece.x;
        int b = selected.piece.y;
        gameController.boardManager.tiles[a][b].takePieceFrom();
        putPieceOn(selected);
        deselect();
        selected.move(this, notifyTurn);
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

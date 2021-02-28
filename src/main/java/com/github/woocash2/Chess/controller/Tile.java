package com.github.woocash2.Chess.controller;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Board;
import javafx.util.Pair;

import java.util.ArrayList;

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
    public ArrayList<Tile> reachableTiles = new ArrayList<>();
    public ArrayList<Tile> takeableTiles = new ArrayList<>();

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

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.team == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.team == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.team != gameController.turnManager.turn) && !takeable;
        boolean onEmpty = (pieceImg == null) && !reachable && !takeable;
        boolean onReachable = reachable;
        boolean onTakeable = takeable;

        if (onOurNotSelected) {
            deselect();
            makeSelection();
            gameController.actionManager.repositionSelected(a, b);
        }
        else if (onOurSelected) {
            gameController.actionManager.repositionSelected(a, b);
        }
        else if (onTheirNotTakeable || onEmpty) {
            deselect();
        }
        else if (onReachable) { // castling is being handled in PieceImg class
            possessPiece();
            moveOrPromote(false);
        }
        else if (onTakeable) {
            if (pieceImg != null)
                pieceImg.die();
            possessPiece();
            moveOrPromote(true);
        }
    }

    public void mouseReleaseBehavoiur(double x, double y) {
        PieceImg selected = gameController.actionManager.selectedPiece;
        if (selected == null)
            return;

        gameController.actionManager.releaseCnt++;

        // What is the type of the mouse release?

        boolean onOurNotSelected = (pieceImg != null) && (pieceImg.team == gameController.turnManager.turn) && (pieceImg != selected);
        boolean onOurSelected = (pieceImg != null) && (pieceImg.team == gameController.turnManager.turn) && (pieceImg == selected);
        boolean onTheirNotTakeable = (pieceImg != null) && (pieceImg.team != gameController.turnManager.turn) && !takeable;
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
            selected.placeInstantly(this);
            possessPiece();
            moveOrPromote(false);
        }
        else if (onTakeable) {
            if (pieceImg != null)
                pieceImg.die();
            selected.placeInstantly(this);
            possessPiece();
            moveOrPromote(true);
        }
    }

    public void makeSelection() {
        gameController.actionManager.releaseCnt = 0;
        gameController.actionManager.selectedPiece = pieceImg;
        gameController.actionManager.selectedTile = this;
        gameController.actionManager.selectedOriginX = pieceImg.getX();
        gameController.actionManager.selectedOriginY = pieceImg.getY();
        showReachableAndTakeable();
        gameController.actionManager.putSelectedOnTop();
        setFill(highlightColor);
    }

    public void deselect() {
        if (gameController.actionManager.selectedTile != null) {
            gameController.actionManager.selectedTile.hideReachableAndTakeable();
            gameController.actionManager.selectedTile.setFill(gameController.actionManager.selectedTile.defaultColor);
        }
        gameController.actionManager.selectedPiece = null;
        gameController.actionManager.selectedTile = null;
    }

    public void moveOrPromote(boolean took) {
        deselect();
        if (pieceImg.piece == Board.Piece.PAWNM) {
            int a = pieceImg.x;
            if ((pieceImg.team == Board.Team.WHITE && a == 1 && x == 0) || (pieceImg.team == Board.Team.BLACK && a == 6 && x == 7)) {
                if (gameController.turnManager.computerGame && gameController.turnManager.turn != gameController.turnManager.playerTeam)
                    gameController.actionManager.promotionPanel.chooseType(pieceImg, gameController.turnManager.computerPromoType, this, took);
                else
                    gameController.actionManager.promotionPanel.show(pieceImg, this, took);
            }
            else
                pieceImg.move(this, null, took);

        }
        else
            pieceImg.move(this, null, took);
    }

    public void possessPiece() {
        PieceImg selected = gameController.actionManager.selectedPiece;
        int a = selected.x;
        int b = selected.y;
        gameController.boardManager.tiles[a][b].takePieceFrom();
        putPieceOn(selected);
    }

    public void showReachableAndTakeable() {
        for (Tile tile : reachableTiles) {
            tile.makeReachable();
        }
        for (Tile tile : takeableTiles) {
            tile.makeTakeable();
        }
    }

    public void hideReachableAndTakeable() {
        for (Tile tile : reachableTiles) {
            tile.makeUnreachable();
        }
        for (Tile tile : takeableTiles) {
            tile.makeUnTakeable();
        }
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

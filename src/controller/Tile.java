package controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Board;

import java.sql.Struct;

public class Tile extends Rectangle {

    private Board board;
    private int x, y;
    protected boolean reachable = false;
    protected Circle shadow;
    private GameController gameController;
    PieceImg pieceImg;

    public Tile(Board board, int x, int y, Color color, GameController gameController) {
        super(100, 100, color);
        this.x = x;
        this.y = y;
        this.board = board;
        this.setX(x);
        this.setY(y);
        this.gameController = gameController;
        shadow = new Circle(15, Color.DARKGRAY);
        shadow.setMouseTransparent(true);
        shadow.setVisible(false);

        setOnMouseClicked(e -> {
            PieceImg selected = gameController.selectedPiece;
            if (selected == null && pieceImg != null) {
                gameController.selectedPiece = pieceImg;
                pieceImg.displayReachableTiles();
            }
            else if (selected != null && pieceImg == selected) {
                selected.hideReachableTiles();
                gameController.selectedPiece = null;
            }
            else if (selected != null && pieceImg != null && pieceImg != selected) {
                selected.hideReachableTiles();
                gameController.selectedPiece = pieceImg;
                pieceImg.displayReachableTiles();
            }
            else if (selected != null && pieceImg == null && reachable) {
                int a = selected.piece.x;
                int b = selected.piece.y;
                selected.hideReachableTiles();
                gameController.tiles[a][b].takePieceFrom();
                selected.move(x, y);
                putPieceOn(selected);
                gameController.selectedPiece = null;
            }
        });
    }

    public void makeReachable() {
        reachable = true;
        shadow.setVisible(true);
    }

    public void makeUnreachable() {
        reachable = false;
        shadow.setVisible(false);
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
}

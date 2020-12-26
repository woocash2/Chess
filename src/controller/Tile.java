package controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Board;

public class Tile extends Rectangle {

    private Board board;
    private int x, y;

    public Tile(Board board, int x, int y, Color color) {
        super(100, 100, color);
        this.x = x;
        this.y = y;
        this.board = board;
        this.setX(x);
        this.setY(y);
    }
}

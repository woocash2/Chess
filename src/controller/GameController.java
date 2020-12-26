package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Board;

public class GameController {

    Board board;
    Color darkColor = Color.DARKCYAN;
    Color lightColor = Color.LIGHTGRAY;
    Tile[][] tiles;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        board = new Board();
        tiles = new Tile[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1)
                    tiles[i][j] = new Tile(board, i, j, darkColor);
                else
                    tiles[i][j] = new Tile(board, i, j, lightColor);
                GridPane.setConstraints(tiles[i][j], j, i);
                gridPane.getChildren().add(tiles[i][j]);
            }
        }
    }

}

package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import java.util.function.Function;
import model.Board;


public class GameController {

    public static String[] letters = {" ", "A", "B", "C", "D", "E", "F", "G", "H", " "};
    public static String[] numbers = {"8", "7", "6", "5", "4", "3", "2", "1"};

    private final Function<String, Label> labelFactory = s -> {
        Label label = new Label(s);
        label.setTextFill(Color.WHITE);
        label.setPrefSize(100, 100);
        label.setFont(new Font(25));
        label.setPadding(new Insets(10, 15, 10, 15));
        return label;
    };

    Board board;
    Color strokeColor = Color.BLACK;
    Color darkColor = Color.DARKCYAN;
    Color lightColor = Color.LIGHTGRAY;
    Tile[][] tiles;
    Rectangle stroke;

    @FXML
    private GridPane gridPane;
    private TilePane tilePane;

    @FXML
    public void initialize() {
        stroke = (Rectangle) gridPane.getChildren().get(0); // according to game.fxml
        stroke.setFill(strokeColor);
        stroke.setStroke(strokeColor);
        tilePane = (TilePane) gridPane.getChildren().get(1); // according to game.fxml

        tiles = new Tile[8][8];
        board = new Board();

        for (int j = 0; j < 10; j++) {
            Label letterTop = labelFactory.apply(letters[j]);
            letterTop.setAlignment(Pos.BOTTOM_CENTER);
            tilePane.getChildren().add(letterTop);
        }

        for (int i = 0; i < 8; i++) {
            Label numLeft = labelFactory.apply(numbers[i]);
            numLeft.setAlignment(Pos.CENTER_RIGHT);
            tilePane.getChildren().add(numLeft);
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1)
                    tiles[i][j] = new Tile(board, i, j, darkColor);
                else
                    tiles[i][j] = new Tile(board, i, j, lightColor);
                tilePane.getChildren().add(tiles[i][j]);
            }
            Label numRight = labelFactory.apply(numbers[i]);
            numRight.setAlignment(Pos.CENTER_LEFT);
            tilePane.getChildren().add(numRight);
        }

        for (int j = 0; j < 10; j++) {
            Label letterBottom = labelFactory.apply(letters[j]);
            letterBottom.setAlignment(Pos.TOP_CENTER);
            tilePane.getChildren().add(letterBottom);
        }
    }

}

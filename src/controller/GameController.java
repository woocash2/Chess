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

import java.util.ArrayList;
import java.util.function.Function;

import model.*;


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
    Tile[][] tiles;
    ArrayList<PieceImg> pieces = new ArrayList<>();
    PieceImg selectedPiece = null;
    Piece.team turn = Piece.team.WHITE;

    Rectangle stroke;
    Color strokeColor = Color.BLACK;
    Color darkColor = Color.DARKCYAN;
    Color lightColor = Color.LIGHTGRAY;

    @FXML
    GridPane gridPane;
    TilePane tilePane;
    TilePane shadowTiles;
    GridPane piecesGrid;

    @FXML
    public void initialize() {
        stroke = (Rectangle) gridPane.getChildren().get(0); // according to game.fxml
        stroke.setFill(strokeColor);
        stroke.setStroke(strokeColor);
        tilePane = (TilePane) gridPane.getChildren().get(1); // according to game.fxml
        shadowTiles = (TilePane) gridPane.getChildren().get(2); // according to game.fxml
        piecesGrid = (GridPane) gridPane.getChildren().get(3); // according to game.fxml

        shadowTiles.setMouseTransparent(true);
        piecesGrid.setMouseTransparent(true);

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
                    tiles[i][j] = new Tile(board, i, j, darkColor, this);
                else
                    tiles[i][j] = new Tile(board, i, j, lightColor, this);
                tilePane.getChildren().add(tiles[i][j]);
                shadowTiles.getChildren().add(tiles[i][j].shadows);
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

        fillBoard();
    }

    public void fillBoard() { // add pieces into chessboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.isEmpty(i, j))
                    continue;
                Piece piece;
                piece = switch (board.get(i, j)) {
                    case 'k' -> new King(i, j, Piece.team.WHITE, board);
                    case 'K' -> new King(i, j, Piece.team.BLACK, board);
                    case 'q' -> new Queen(i, j, Piece.team.WHITE, board);
                    case 'Q' -> new Queen(i, j, Piece.team.BLACK, board);
                    case 'b' -> new Bishop(i, j, Piece.team.WHITE, board);
                    case 'B' -> new Bishop(i, j, Piece.team.BLACK, board);
                    case 'r' -> new Rook(i, j, Piece.team.WHITE, board);
                    case 'R' -> new Rook(i, j, Piece.team.BLACK, board);
                    case 'n' -> new Knight(i, j, Piece.team.WHITE, board);
                    case 'N' -> new Knight(i, j, Piece.team.BLACK, board);
                    case 'p' -> new Pawn(i, j, Piece.team.WHITE, board);
                    default -> new Pawn(i, j, Piece.team.BLACK, board);
                };

                PieceImg pieceImg = new PieceImg(piece, this);
                pieces.add(pieceImg);
                piecesGrid.add(pieceImg, j, i);
                tiles[i][j].putPieceOn(pieceImg);
            }
        }
    }

    public void notifyTurnMade() {
        if (turn == Piece.team.WHITE)
            turn = Piece.team.BLACK;
        else
            turn = Piece.team.WHITE;
    }
}

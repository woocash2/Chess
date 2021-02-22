package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.King;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.LabelProvider;
import com.github.woocash2.Chess.model.utils.PieceFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class BoardManager {

    public GameController gameController;
    public AnchorPane piecesPane;
    public TilePane tilesPane;
    public TilePane shadowsPane;


    Board board;
    Tile[][] tiles;
    ArrayList<PieceImg> pieces = new ArrayList<>();

    Color darkColor = Color.ROYALBLUE;
    Color lightColor = Color.LIGHTGRAY;


    public BoardManager(GameController controller) {
        gameController = controller;
        tilesPane = gameController.tilesPane;
        shadowsPane = gameController.shadowsPane;
        piecesPane = gameController.piecesPane;

        shadowsPane.setMouseTransparent(true);
        piecesPane.setMouseTransparent(false);

        tiles = new Tile[8][8];
        board = new Board();

        fillBoardWithTiles();
        fillBoardWithPieces();
    }

    public void fillBoardWithTiles() {
        String[] letters = {" ", "A", "B", "C", "D", "E", "F", "G", "H", " "};
        String[] numbers = {"8", "7", "6", "5", "4", "3", "2", "1"};

        for (int j = 0; j < 10; j++) {
            Label letterTop = LabelProvider.getLabel(letters[j]);
            letterTop.setAlignment(Pos.BOTTOM_CENTER);
            tilesPane.getChildren().add(letterTop);
        }

        for (int i = 0; i < 8; i++) {
            Label numLeft = LabelProvider.getLabel(numbers[i]);
            numLeft.setAlignment(Pos.CENTER_RIGHT);
            tilesPane.getChildren().add(numLeft);
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1)
                    tiles[i][j] = new Tile(board, i, j, darkColor, gameController);
                else
                    tiles[i][j] = new Tile(board, i, j, lightColor, gameController);
                tilesPane.getChildren().add(tiles[i][j]);
                shadowsPane.getChildren().add(tiles[i][j].shadows);
            }
            Label numRight = LabelProvider.getLabel(numbers[i]);
            numRight.setAlignment(Pos.CENTER_LEFT);
            tilesPane.getChildren().add(numRight);
        }

        for (int j = 0; j < 10; j++) {
            Label letterBottom = LabelProvider.getLabel(letters[j]);
            letterBottom.setAlignment(Pos.TOP_CENTER);
            tilesPane.getChildren().add(letterBottom);
        }
    }

    public void fillBoardWithPieces() { // add pieces into chessboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.isEmpty(i, j))
                    continue;
                Piece piece = PieceFactory.get(i, j, board);
                if (piece.onBoard == 'k') gameController.turnManager.whiteKing = (King) piece;
                if (piece.onBoard == 'K') gameController.turnManager.blackKing = (King) piece;

                PieceImg pieceImg = new PieceImg(piece, gameController);
                pieces.add(pieceImg);
                piecesPane.getChildren().add(pieceImg);
                tiles[i][j].putPieceOn(pieceImg);
                pieceImg.setX(tiles[i][j].getCenter().getKey());
                pieceImg.setY(tiles[i][j].getCenter().getValue());

                if (pieceImg.piece.onBoard == 'r') gameController.turnManager.whiteRooks.add(pieceImg);
                if (pieceImg.piece.onBoard == 'R') gameController.turnManager.blackRooks.add(pieceImg);
            }
        }
    }
}

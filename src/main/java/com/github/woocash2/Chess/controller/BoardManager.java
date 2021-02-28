package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Move;
import com.github.woocash2.Chess.model.utils.LabelProvider;
import com.github.woocash2.Chess.test.BoardTemplate;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class BoardManager {

    public GameController gameController;
    public AnchorPane piecesPane;
    public TilePane tilesPane;
    public TilePane shadowsPane;

    public Board board;
    public Tile[][] tiles;
    public ArrayList<PieceImg> pieces = new ArrayList<>();

    public Color darkColor = Color.ROYALBLUE;
    public Color lightColor = Color.LIGHTGRAY;


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

        if (gameController.turnManager.computerGame && gameController.turnManager.playerTeam == Board.Team.BLACK)
            flipTheBoard();

        if (gameController.turnManager.computerGame)
            gameController.turnManager.computer.addBoard(board);
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
                Board.Piece piece = board.pieces[i][j];
                Board.Team team = board.teams[i][j];

                PieceImg pieceImg = new PieceImg(board, piece, team, i, j, gameController);
                pieces.add(pieceImg);
                piecesPane.getChildren().add(pieceImg);
                tiles[i][j].putPieceOn(pieceImg);
                pieceImg.setX(tiles[i][j].getCenter().getKey());
                pieceImg.setY(tiles[i][j].getCenter().getValue());
            }
        }
    }

    public void flipTheBoard() {
        tilesPane.setScaleY(-1);
        tilesPane.setScaleX(-1);
        shadowsPane.setScaleY(-1);
        shadowsPane.setScaleX(-1);
        piecesPane.setScaleY(-1);
        piecesPane.setScaleX(-1);
        for (Node node : tilesPane.getChildren()) {
            node.setScaleY(-1);
            node.setScaleX(-1);
            if (node.getClass() == Label.class) {
                if(((Label)node).getAlignment().equals(Pos.BOTTOM_CENTER))
                    ((Label) node).setAlignment(Pos.TOP_CENTER);
                else if (((Label)node).getAlignment().equals(Pos.TOP_CENTER))
                    ((Label) node).setAlignment(Pos.BOTTOM_CENTER);
                else if (((Label)node).getAlignment().equals(Pos.CENTER_LEFT))
                    ((Label) node).setAlignment(Pos.CENTER_RIGHT);
                else if (((Label)node).getAlignment().equals(Pos.CENTER_RIGHT))
                    ((Label) node).setAlignment(Pos.CENTER_LEFT);
            }
        }
        for (PieceImg piece : pieces) {
            piece.setScaleY(-1);
            piece.setScaleX(-1);
        }
    }

    public void handleAdditional(Move move) {
        if (move == null)
            return;
        if (move.pre == Board.Piece.ROOK) {
            int fx = move.fromX;
            int fy = move.fromY;
            int tx = move.toX;
            int ty = move.toY;

            Tile from = tiles[fx][fy];
            Tile to = tiles[tx][ty];

            PieceImg pieceImg = from.pieceImg;
            pieceImg.x = tx;
            pieceImg.y = ty;
            to.putPieceOn(pieceImg);
            from.takePieceFrom();
            pieceImg.playTransition(to);
        }
        if (move.pre == Board.Piece.PAWNJ) {
            Tile tile = tiles[move.fromX][move.fromY];
            tile.pieceImg.die();
            tile.takePieceFrom();
        }
    }
}

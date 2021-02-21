package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.utils.CoordinateProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import javafx.stage.Stage;
import com.github.woocash2.Chess.model.*;
import com.github.woocash2.Chess.model.utils.PieceFactory;
import javafx.util.Pair;


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
    public PieceImg selectedPiece = null;
    public double selectedOriginX;
    public double selectedOriginY;
    public int releaseCnt = 0;
    Piece.team turn = Piece.team.BLACK;

    Color strokeColor = Color.BLACK;
    Color darkColor = Color.DARKCYAN;
    Color lightColor = Color.LIGHTGRAY;

    King whiteKing, blackKing;
    ArrayList<PieceImg> whiteRooks = new ArrayList<>(), blackRooks = new ArrayList<>(); // for castling purposes

    @FXML
    GridPane gridPane;
    @FXML
    TilePane tilePane;
    @FXML
    TilePane shadowTiles;
    @FXML
    TilePane promotionPane;
    @FXML
    Rectangle promotionBack;
    @FXML
    AnchorPane piecesAnchor;
    @FXML
    Label whiteTime, blackTime;
    @FXML
    Rectangle boardCover; // covers the board during pawn promotion
    @FXML
    Label resultLabel;
    @FXML
    Rectangle resultBox;
    @FXML
    Button resultOkButton;

    private int minutes;
    private Timer whiteTimer, blackTimer;

    private PromotionPanel promotionPanel;

    @FXML
    public void initialize() {
        gridPane.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        darkColor = MenuController.darkTileColor;

        shadowTiles.setMouseTransparent(true);
        piecesAnchor.setMouseTransparent(false);

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
        notifyTurnMade();
        minutes = MenuController.chosenTime;
        if (minutes != 0) {
            whiteTimer = new Timer(minutes, Piece.team.WHITE, whiteTime, this);
            blackTimer = new Timer(minutes, Piece.team.BLACK, blackTime, this);
            whiteTimer.setDaemon(true);
            blackTimer.setDaemon(true);
            whiteTimer.start();
            blackTimer.start();
        }
        promotionPanel = new PromotionPanel(promotionPane, promotionBack, boardCover);

        piecesAnchor.setOnMousePressed(e -> {
            Pair<Integer, Integer> coords = CoordinateProvider.tileCoordsFromMousePosition(e);
            int x = coords.getKey();
            int y = coords.getValue();
            tiles[x][y].mousePressBehavior(e);
        });

        piecesAnchor.setOnMouseReleased(e -> {
            Pair<Integer, Integer> coords = CoordinateProvider.tileCoordsFromMousePosition(e);
            int x = coords.getKey();
            int y = coords.getValue();
            if (x < 0 || y < 0 || x > 7 || y > 7) {
                if (selectedPiece != null)
                    restoreSelectedPosition();
            }
            else
                tiles[x][y].mouseReleaseBehavoiur(e);
        });

        piecesAnchor.setOnMouseDragged(e -> {
            if (selectedPiece != null) {
                selectedPiece.setX(e.getX() - 50);
                selectedPiece.setY(e.getY() - 50);
            }
        });
    }

    public void fillBoard() { // add pieces into chessboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.isEmpty(i, j))
                    continue;
                Piece piece = PieceFactory.get(i, j, board);
                if (piece.onBoard == 'k') whiteKing = (King) piece;
                if (piece.onBoard == 'K') blackKing = (King) piece;

                PieceImg pieceImg = new PieceImg(piece, this);
                pieces.add(pieceImg);
                piecesAnchor.getChildren().add(pieceImg);
                tiles[i][j].putPieceOn(pieceImg);
                pieceImg.setX(tiles[i][j].getCenter().getKey());
                pieceImg.setY(tiles[i][j].getCenter().getValue());

                if (pieceImg.piece.onBoard == 'r') whiteRooks.add(pieceImg);
                if (pieceImg.piece.onBoard == 'R') blackRooks.add(pieceImg);
            }
        }
    }

    public void repositionSelected(MouseEvent e) {
        selectedPiece.setX(e.getX() - 50);
        selectedPiece.setY(e.getY() - 50);
    }

    public void restoreSelectedPosition() {
        selectedPiece.setX(selectedOriginX);
        selectedPiece.setY(selectedOriginY);
    }

    public void putSelectedOnTop() {
        piecesAnchor.getChildren().remove(selectedPiece);
        piecesAnchor.getChildren().add(selectedPiece);
    }

    public void notifyTurnMade() {

        Piece.team newTurn;
        if (turn == Piece.team.WHITE)
            newTurn = Piece.team.BLACK;
        else
            newTurn = Piece.team.WHITE;

        for (PieceImg pieceImg : pieces) {
            if (pieceImg.piece.color == newTurn)
                pieceImg.piece.updatePositions();

            if (pieceImg.piece.getClass() == King.class) {
                if (pieceImg.piece.color == Piece.team.WHITE) {
                    ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(0).piece);
                    ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(1).piece);
                }
                else {
                    ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(0).piece);
                    ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(1).piece);
                }
            }

            if (pieceImg.piece.getClass() == Pawn.class) {
                int x = pieceImg.piece.x;
                int y = pieceImg.piece.y;
                if ((pieceImg.piece.color == Piece.team.WHITE && pieceImg.piece.x == 0) ||
                        (pieceImg.piece.color == Piece.team.BLACK && pieceImg.piece.x == 7)) {
                    promotionPanel.show(pieceImg);
                    return; // so clock is still ticking for promoting player and turn is unchanged.
                }
                Piece nextToUs;
                if (board.inBoardRange(x, y + 1) && tiles[x][y + 1].pieceImg != null) {
                    nextToUs = tiles[x][y + 1].pieceImg.piece;
                    if (nextToUs.getClass() == Pawn.class && pieceImg.piece.color != nextToUs.color)
                        ((Pawn) pieceImg.piece).considerEnPassant((Pawn) nextToUs);
                }
                if (board.inBoardRange(x, y - 1) && tiles[x][y - 1].pieceImg != null) {
                    nextToUs = tiles[x][y - 1].pieceImg.piece;
                    if (nextToUs.getClass() == Pawn.class && pieceImg.piece.color != nextToUs.color)
                        ((Pawn) pieceImg.piece).considerEnPassant((Pawn) nextToUs);
                }
            }
        }

        turn = newTurn;

        if (whiteTimer != null && blackTimer != null) {
            Platform.runLater(() -> {
                whiteTimer.interrupt();
                blackTimer.interrupt();
            });
        }

        if (isCheckMated(newTurn))
            endTheGame(Piece.oponnent(newTurn));

        if (isStaleMate(newTurn) || pieces.size() == 2) // size == 2 means only two kings left
            endTheGame(null);
    }

    public boolean isCheckMated(Piece.team team) {
        King king;
        if (team == Piece.team.WHITE) king = whiteKing;
        else king = blackKing;

        if (board.isAttacked(team, king.x, king.y)) {
            return numberOfPossibleMoves(team) == 0;
        }

        return false;
    }

    public boolean isStaleMate(Piece.team team) {
        King king;
        if (team == Piece.team.WHITE) king = whiteKing;
        else king = blackKing;

        if (board.isAttacked(team, king.x, king.y))
            return false;

        return numberOfPossibleMoves(team) == 0;
    }

    public int numberOfPossibleMoves(Piece.team team) {
        int possibleMoves = 0;
        for (PieceImg piece : pieces) {
            if (piece.piece.color != team)
                continue;
            possibleMoves += piece.piece.reachablePositions.size();
            possibleMoves += piece.piece.takeablePositions.size();
        }

        return possibleMoves;
    }

    public void endTheGame(Piece.team winner) {
        if (whiteTimer != null && blackTimer != null) { // can't be just one of them
            whiteTimer.halt();
            blackTimer.halt();
        }
        GameResult result = new GameResult(resultLabel, resultBox, resultOkButton);
        result.show(winner);
        boardCover.setVisible(true); // no more moves available
    }

    public void backToMenu() throws IOException {
        if (minutes != 0) {
            whiteTimer.halt();
            blackTimer.halt();
        }
        Stage stage = (Stage) gridPane.getScene().getWindow();
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        stage.getScene().setRoot(gameRoot);
    }
}

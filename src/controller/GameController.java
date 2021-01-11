package controller;

import javafx.application.Platform;
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
import model.utils.PieceFactory;


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
    Piece.team turn = Piece.team.BLACK;

    Rectangle stroke;
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
    GridPane piecesGrid;
    @FXML
    Label whiteTime, blackTime;
    @FXML
    Rectangle boardCover; // covers the board during pawn promotion

    private int minutes;
    private Timer whiteTimer, blackTimer;

    private PromotionPanel promotionPanel;

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
        notifyTurnMade();
        minutes = MenuController.chosenTime;
        whiteTimer = new Timer(minutes, Piece.team.WHITE, whiteTime, this);
        blackTimer = new Timer(minutes, Piece.team.BLACK, blackTime, this);
        whiteTimer.setDaemon(true);
        blackTimer.setDaemon(true);
        whiteTimer.start();
        blackTimer.start();

        promotionPanel = new PromotionPanel(promotionPane, promotionBack, boardCover);
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
                piecesGrid.add(pieceImg, j, i);
                tiles[i][j].putPieceOn(pieceImg);

                if (pieceImg.piece.onBoard == 'r') whiteRooks.add(pieceImg);
                if (pieceImg.piece.onBoard == 'R') blackRooks.add(pieceImg);
            }
        }
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
    }
}

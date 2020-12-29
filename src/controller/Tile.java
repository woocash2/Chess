package controller;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Board;

public class Tile extends Rectangle {

    private final Board board;
    private final int x, y;

    protected boolean reachable = false;
    protected boolean takeable = false;

    protected Circle reachShadow;
    protected Rectangle takeShadow;
    protected Group shadows;

    private final GameController gameController;

    PieceImg pieceImg;

    public Tile(Board board, int x, int y, Color color, GameController gameController) {
        super(100, 100, color);
        this.x = x;
        this.y = y;
        this.board = board;
        this.setX(x);
        this.setY(y);
        this.gameController = gameController;

        reachShadow = new Circle(15, Color.DARKGRAY);
        reachShadow.setMouseTransparent(true);
        reachShadow.setVisible(false);

        takeShadow = new Rectangle(90, 90, Color.TRANSPARENT);
        takeShadow.setStrokeWidth(10);
        takeShadow.setStroke(Color.DARKGRAY);
        takeShadow.setMouseTransparent(true);
        takeShadow.setVisible(false);

        shadows = new Group();
        shadows.getChildren().add(reachShadow);
        shadows.getChildren().add(takeShadow);
        shadows.setMouseTransparent(true);

        setOnMouseClicked(e -> {
            PieceImg selected = gameController.selectedPiece;

            if (selected == null && pieceImg != null) {
                // select our piece
                if (gameController.turn == pieceImg.piece.color) {
                    gameController.selectedPiece = pieceImg;
                    pieceImg.showReachableAndTakeable();
                }
            }
            else if (selected != null && pieceImg == selected) {
                // cancel our selection
                selected.hideReachableAndTakeable();
                gameController.selectedPiece = null;
            }
            else if (selected != null && pieceImg != null && pieceImg != selected) {
                // change selection to our piece
                System.out.println("OKOK");
                if (gameController.turn == pieceImg.piece.color) {
                    selected.hideReachableAndTakeable();
                    gameController.selectedPiece = pieceImg;
                    pieceImg.showReachableAndTakeable();
                }
                else if (takeable) { // take opponent's piece
                    System.out.println("nice");
                    pieceImg.die();
                    makeMoveToUs();
                }
            }
            else if (selected != null && pieceImg == null && reachable) { // move selected piece to us
                makeMoveToUs();
            }
        });
    }

    public void makeMoveToUs() {
        PieceImg selected = gameController.selectedPiece;
        int a = selected.piece.x;
        int b = selected.piece.y;
        selected.hideReachableAndTakeable();
        gameController.tiles[a][b].takePieceFrom();
        selected.move(x, y);
        putPieceOn(selected);
        gameController.selectedPiece = null;
        gameController.notifyTurnMade();
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

package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.utils.CoordinateProvider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class ActionManager {

    public GameController gameController;
    public AnchorPane piecesPane;
    public PromotionPanel promotionPanel;
    public int releaseCnt = 0;

    public PieceImg selectedPiece = null;
    public Tile selectedTile = null;
    public double selectedOriginX;
    public double selectedOriginY;

    public ActionManager(GameController controller) {
        gameController = controller;
        piecesPane = gameController.piecesPane;
        promotionPanel = new PromotionPanel(gameController.promotionPane, gameController.promotionBack, gameController.boardCover);
        setUpPiecesPaneActions();
    }

    public void setUpPiecesPaneActions() {
        piecesPane.setOnMousePressed(e -> {
            Pair<Integer, Integer> coords = CoordinateProvider.tileCoordsFromMousePosition(e);
            int x = coords.getKey();
            int y = coords.getValue();
            gameController.boardManager.tiles[x][y].mousePressBehavior(e);
        });

        piecesPane.setOnMouseReleased(e -> {
            Pair<Integer, Integer> coords = CoordinateProvider.tileCoordsFromMousePosition(e);
            int x = coords.getKey();
            int y = coords.getValue();
            if (x < 0 || y < 0 || x > 7 || y > 7) {
                if (selectedPiece != null)
                    restoreSelectedPosition();
            }
            else
                gameController.boardManager.tiles[x][y].mouseReleaseBehavoiur(e);
        });

        piecesPane.setOnMouseDragged(e -> {
            if (selectedPiece != null) {
                selectedPiece.setX(e.getX() - 50);
                selectedPiece.setY(e.getY() - 50);
            }
        });
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
        piecesPane.getChildren().remove(selectedPiece);
        piecesPane.getChildren().add(selectedPiece);
    }
}

package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.Board;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.utils.ImageCropper;

public class PromotionPanel {

    private final TilePane promotionPane;
    private final Rectangle paneBackground;
    private final Rectangle boardCover;
    private final Board.Piece[] promoTypes = {Board.Piece.QUEEN, Board.Piece.ROOK, Board.Piece.BISHOP, Board.Piece.KNIGHT};
    public GameController gameController;

    public PromotionPanel(GameController controller) {
        gameController = controller;
        promotionPane = gameController.promotionPane;
        paneBackground = gameController.promotionBack;
        boardCover = gameController.boardCover;
    }

    public void show(PieceImg promoPiece, Tile target, boolean took) {
        boardCover.setVisible(true); // disables pieces movements during promotion

        Board.Team team = promoPiece.team;
        paneBackground.setVisible(true);
        promotionPane.setVisible(true);

        for (Board.Piece type : promoTypes) {
            ImageView view = new ImageView(ImageCropper.getImageByTeamAndType(promoPiece.team, type));
            view.setFitHeight(100);
            view.setFitWidth(100);

            view.setOnMouseClicked(e -> {
                chooseType(promoPiece, type, target, took);
                hide();
            });

            promotionPane.getChildren().add(view);
        }
    }

    // for computer promotion purposes
    public void chooseType(PieceImg promoPiece, Board.Piece type, Tile target, boolean took) {
        promoPiece.transform(type);
        promoPiece.move(target, type, took);
    }

    public void hide() {
        paneBackground.setVisible(false);
        promotionPane.getChildren().clear();
        promotionPane.setVisible(false);
        boardCover.setVisible(false); // enables further pieces moves
    }
}

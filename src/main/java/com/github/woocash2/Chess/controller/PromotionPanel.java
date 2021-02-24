package com.github.woocash2.Chess.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.ImageCropper;
import com.github.woocash2.Chess.model.PieceFactory;

public class PromotionPanel {

    private final TilePane promotionPane;
    private final Rectangle paneBackground;
    private final Rectangle boardCover;
    private final Piece.Type[] promoTypes = {Piece.Type.QUEEN, Piece.Type.ROOK, Piece.Type.BISHOP, Piece.Type.KNIGHT};
    public GameController gameController;

    public PromotionPanel(GameController controller) {
        gameController = controller;
        promotionPane = gameController.promotionPane;
        paneBackground = gameController.promotionBack;
        boardCover = gameController.boardCover;
    }

    public void show(PieceImg promoPiece) {
        boardCover.setVisible(true); // disables pieces movements during promotion

        Piece.Team team = promoPiece.piece.team;
        paneBackground.setVisible(true);
        promotionPane.setVisible(true);

        for (Piece.Type type : promoTypes) {
            ImageView view = new ImageView(ImageCropper.getImageByTeamAndType(promoPiece.piece.team, type));
            view.setFitHeight(100);
            view.setFitWidth(100);

            view.setOnMouseClicked(e -> {
                chooseType(promoPiece, type);
                hide();
                gameController.turnManager.notifyTurnMade();
            });

            promotionPane.getChildren().add(view);
        }
    }

    // for computer promotion purposes
    public void chooseType(PieceImg promoPiece, Piece.Type type) {
        int x = promoPiece.piece.x;
        int y = promoPiece.piece.y;

        promoPiece.piece.transform(type);
        promoPiece.setImage(ImageCropper.getImage(promoPiece.piece));
    }

    public void hide() {
        paneBackground.setVisible(false);
        promotionPane.getChildren().clear();
        promotionPane.setVisible(false);
        boardCover.setVisible(false); // enables further pieces moves
    }
}

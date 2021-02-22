package com.github.woocash2.Chess.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.ImageCropper;
import com.github.woocash2.Chess.model.utils.PieceFactory;

public class PromotionPanel {

    private final TilePane promotionPane;
    private final Rectangle paneBackground;
    private final Rectangle boardCover;
    private final char[] whitePromotionNames = {'q', 'r', 'b', 'n'};
    private final char[] blackPromotionNames = {'Q', 'R', 'B', 'N'};
    public GameController gameController;

    public PromotionPanel(GameController controller) {
        gameController = controller;
        promotionPane = gameController.promotionPane;
        paneBackground = gameController.promotionBack;
        boardCover = gameController.boardCover;
    }

    public void show(PieceImg promoPiece) {
        boardCover.setVisible(true); // disables pieces movements during promotion

        Piece.team team = promoPiece.piece.color;
        paneBackground.setVisible(true);
        promotionPane.setVisible(true);

        char[] promotionNames;
        if (team == Piece.team.WHITE)
            promotionNames = whitePromotionNames;
        else
            promotionNames = blackPromotionNames;

        for (char c : promotionNames) {
            ImageView view = new ImageView(ImageCropper.getImageByName(c));
            view.setFitHeight(100);
            view.setFitWidth(100);

            view.setOnMouseClicked(e -> {
                int x = promoPiece.piece.x;
                int y = promoPiece.piece.y;
                promoPiece.piece = PieceFactory.putOnBoardAndGet(c, x, y, promoPiece.piece.board);
                promoPiece.setImage(ImageCropper.getImage(promoPiece.piece));
                hide();
                gameController.turnManager.notifyTurnMade();
            });

            promotionPane.getChildren().add(view);
        }
    }

    // for computer promotion purposes
    public void chooseQueen(PieceImg promoPiece) {
        int x = promoPiece.piece.x;
        int y = promoPiece.piece.y;
        char c = promoPiece.piece.color == Piece.team.WHITE ? 'q' : 'Q';
        promoPiece.piece = PieceFactory.putOnBoardAndGet(c, x, y, promoPiece.piece.board);
        promoPiece.setImage(ImageCropper.getImage(promoPiece.piece));
        gameController.turnManager.notifyTurnMade();
    }

    public void hide() {
        paneBackground.setVisible(false);
        promotionPane.getChildren().clear();
        promotionPane.setVisible(false);
        boardCover.setVisible(false); // enables further pieces moves
    }
}

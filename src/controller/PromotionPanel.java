package controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import model.Piece;
import model.utils.ImageCropper;
import model.utils.PieceFactory;

public class PromotionPanel {

    private final TilePane promotionPane;
    private final Rectangle paneBackground;
    private final Rectangle boardCover;
    private final char[] whitePromotionNames = {'q', 'r', 'b', 'n'};
    private final char[] blackPromotionNames = {'Q', 'R', 'B', 'N'};

    public PromotionPanel(TilePane pane, Rectangle back, Rectangle cover) {
        promotionPane = pane;
        paneBackground = back;
        boardCover = cover;
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
                promoPiece.gameController.notifyTurnMade();
                hide();
            });

            promotionPane.getChildren().add(view);
        }
    }

    public void hide() {
        paneBackground.setVisible(false);
        promotionPane.getChildren().clear();
        promotionPane.setVisible(false);
        boardCover.setVisible(false); // enables further pieces moves
    }
}

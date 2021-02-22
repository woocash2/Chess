package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.utils.CoordinateProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

import javafx.stage.Stage;
import com.github.woocash2.Chess.model.*;
import com.github.woocash2.Chess.model.utils.PieceFactory;
import javafx.util.Pair;


public class GameController {

    @FXML // whole area
    GridPane gridPane;
    @FXML // holds tiles
    TilePane tilesPane;
    @FXML // holds possible moves shadows
    TilePane shadowsPane;
    @FXML // displayed on the left side during the promotion phase
    TilePane promotionPane;
    @FXML // background of promotionPane
    Rectangle promotionBack;
    @FXML // holds pieces
    AnchorPane piecesPane;
    @FXML // timers
    Label whiteTime, blackTime;
    @FXML // covers the board during pawn promotion
    Rectangle boardCover;
    @FXML // dusplayed on the left side after the game
    Label resultLabel;
    @FXML // clicking it returns to main menu
    Label menuLabel;

    public TurnManager turnManager;
    public BoardManager boardManager;
    public ActionManager actionManager;

    // For playing sound effects after each move.
    public SoundPlayer soundPlayer = new SoundPlayer();

    @FXML
    public void initialize() {

        gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        menuLabel.setTextFill(resultLabel.getTextFill());
        menuLabel.setOnMouseEntered(e -> menuLabel.setTextFill(Color.RED));
        menuLabel.setOnMouseExited(e -> menuLabel.setTextFill(resultLabel.getTextFill()));

        turnManager = new TurnManager(this);
        boardManager = new BoardManager(this);
        actionManager = new ActionManager(this);

        turnManager.updatePositions(Piece.team.WHITE);
        turnManager.runTimers();
    }

    public void backToMenu() throws IOException {
        if (turnManager.minutes != 0) {
            turnManager.whiteTimer.halt();
            turnManager.blackTimer.halt();
        }
        Stage stage = (Stage) gridPane.getScene().getWindow();
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        stage.getScene().setRoot(gameRoot);
    }
}

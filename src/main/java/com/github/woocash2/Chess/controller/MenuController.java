package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.utils.TeamRandomizer;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Rectangle sideRect;

    @FXML
    private VBox mainVbox;
    @FXML
    private Label playLabel;
    @FXML
    private Label stylesLabel;
    @FXML
    private Label exitLabel;

    @FXML
    private VBox playVbox;
    @FXML
    private Label computerLabel;
    @FXML
    private Label local2pLabel;
    @FXML
    private Label backLabel;

    @FXML
    private VBox timeVbox;
    @FXML
    private Label min5Label;
    @FXML
    private Label min10Label;
    @FXML
    private Label min20Label;
    @FXML
    private Label min40Label;
    @FXML
    private Label noLimitLabel;
    @FXML
    private Label back1Label;

    @FXML
    private VBox teamVbox;
    @FXML
    private Label whiteLabel;
    @FXML
    private Label blackLabel;
    @FXML
    private Label randomLabel;
    @FXML
    private Label back2Label;

    public static int chosenTime = 10;
    public static boolean computerGame = false;
    public static Piece.team playerTeam = Piece.team.WHITE;

    Paint buttonColor;
    Paint highlightColor = Color.INDIANRED;

    private Label[] labels;
    private Label[] timeLabels;

    private double vboxX;
    private double vboxY;

    public void slideVboxIn(VBox box) {
        TranslateTransition transition = new TranslateTransition(new Duration(300), box);
        transition.setToX(vboxX - box.getLayoutX());
        transition.play();
    }

    public void slideVboxOut(VBox box) {
        TranslateTransition transition = new TranslateTransition(new Duration(300), box);
        transition.setToX(-500 - box.getLayoutX());
        transition.play();
    }

    public void prepareActions() {

        // highlighting
        for (Label label : labels) {
            label.setOnMouseEntered(e -> label.setTextFill(highlightColor));
            label.setOnMouseExited(e -> label.setTextFill(buttonColor));
        }

        // main menu
        playLabel.setOnMouseClicked(e -> {
            slideVboxOut(mainVbox);
            slideVboxIn(playVbox);
        });
        exitLabel.setOnMouseClicked(e -> {
            quitApp();
        });

        // back from play
        backLabel.setOnMouseClicked(e -> {
            slideVboxOut(playVbox);
            slideVboxIn(mainVbox);
        });

        // local 2p
        local2pLabel.setOnMouseClicked(e -> {
            computerGame = false;
            slideVboxOut(playVbox);
            slideVboxIn(timeVbox);
        });
        for (int m = 5, k = 0; m <= 80 && k < 5; m *= 2, k++) {
            int finalM = m;
            timeLabels[k].setOnMouseClicked(e -> {
                launchGame(finalM % 80);
            });
        }
        back1Label.setOnMouseClicked(e -> {
            slideVboxOut(timeVbox);
            slideVboxIn(playVbox);
        });

        // computer
        computerLabel.setOnMouseClicked(e -> {
            computerGame = true;
            slideVboxOut(playVbox);
            slideVboxIn(teamVbox);
        });
        whiteLabel.setOnMouseClicked(e -> {
            playerTeam = Piece.team.WHITE;
            launchGame(0);
        });
        blackLabel.setOnMouseClicked(e -> {
            playerTeam = Piece.team.BLACK;
            launchGame(0);
        });
        randomLabel.setOnMouseClicked(e -> {
            playerTeam = TeamRandomizer.getRandomTeam();
            launchGame(0);
        });
        back2Label.setOnMouseClicked(e -> {
            slideVboxOut(teamVbox);
            slideVboxIn(playVbox);
        });
    }

    @FXML
    public void initialize() {

        labels = new Label[] {playLabel, stylesLabel, exitLabel, computerLabel, local2pLabel, backLabel,
                             min5Label, min10Label, min20Label, min40Label, noLimitLabel, back1Label,
                             whiteLabel, blackLabel, randomLabel, back2Label};

        timeLabels = new Label[] {min5Label, min10Label, min20Label, min40Label, noLimitLabel};

        vboxX = mainVbox.getLayoutX();
        vboxY = mainVbox.getLayoutY();
        prepareActions();

        mainVbox.setLayoutX(-500);
        playVbox.setLayoutX(-500);
        timeVbox.setLayoutX(-500);
        teamVbox.setLayoutX(-500);
        slideVboxIn(mainVbox);

        buttonColor = playLabel.getTextFill();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        sideRect.setFill(Color.rgb(40, 40, 40));
    }

    public void quitApp() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    public void launchGame(int minutes) {
        try {
            chosenTime = minutes;
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/game.fxml"));
            stage.getScene().setRoot(gameRoot);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

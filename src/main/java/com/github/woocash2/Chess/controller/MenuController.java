package com.github.woocash2.Chess.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private Label styleLabel;
    @FXML
    private Rectangle darkCyan;
    @FXML
    private Rectangle darkRed;
    @FXML
    private Rectangle darkGray;

    public static int chosenTime = 10;
    public static Color darkTileColor = Color.ROYALBLUE;

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
        playLabel.setOnMouseClicked(e -> {
            slideVboxOut(mainVbox);
            slideVboxIn(playVbox);
        });
        exitLabel.setOnMouseClicked(e -> {
            quitApp();
        });
        backLabel.setOnMouseClicked(e -> {
            slideVboxOut(playVbox);
            slideVboxIn(mainVbox);
        });
        back1Label.setOnMouseClicked(e -> {
            slideVboxOut(timeVbox);
            slideVboxIn(playVbox);
        });

        for (Label label : labels) {
            label.setOnMouseEntered(e -> label.setTextFill(highlightColor));
            label.setOnMouseExited(e -> label.setTextFill(buttonColor));
        }

        local2pLabel.setOnMouseClicked(e -> {
            slideVboxOut(playVbox);
            slideVboxIn(timeVbox);
        });

        for (int m = 5, k = 0; m <= 80 && k < 5; m *= 2, k++) {
            int finalM = m;
            timeLabels[k].setOnMouseClicked(e -> {
                try {
                    launchGame(finalM % 80);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }

    @FXML
    public void initialize() {

        labels = new Label[] {playLabel, stylesLabel, exitLabel, computerLabel, local2pLabel, backLabel,
                             min5Label, min10Label, min20Label, min40Label, noLimitLabel, back1Label};

        timeLabels = new Label[] {min5Label, min10Label, min20Label, min40Label, noLimitLabel};

        vboxX = mainVbox.getLayoutX();
        vboxY = mainVbox.getLayoutY();
        prepareActions();

        mainVbox.setLayoutX(-500);
        playVbox.setLayoutX(-500);
        timeVbox.setLayoutX(-500);
        slideVboxIn(mainVbox);

        buttonColor = playLabel.getTextFill();
        System.out.println(playLabel.getText());
        anchorPane.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void goToOptions() {

        styleLabel.setVisible(true);
        darkCyan.setVisible(true);
        darkRed.setVisible(true);
        darkGray.setVisible(true);
    }

    public void backFromOptions() {

        styleLabel.setVisible(false);
        darkCyan.setVisible(false);
        darkRed.setVisible(false);
        darkGray.setVisible(false);
    }

    public void chooseDarkCyan() {
        darkTileColor = Color.ROYALBLUE;
    }

    public void chooseDarkRed() {
        darkTileColor = Color.INDIANRED;
    }

    public void chooseDarkGray() {
        darkTileColor = Color.LIGHTSLATEGRAY;
    }

    public void quitApp() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    public void launchNoLimitGame() throws IOException {
        launchGame(0);
    }

    public void launch10minGame() throws IOException {
        launchGame(10);
    }

    public void launch20minGame() throws IOException {
        launchGame(20);
    }

    public void launchGame(int minutes) throws IOException {
        chosenTime = minutes;
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/game.fxml"));
        stage.getScene().setRoot(gameRoot);
    }
}

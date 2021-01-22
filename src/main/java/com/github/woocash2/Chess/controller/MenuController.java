package com.github.woocash2.Chess.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Button playButton;
    @FXML
    private Button optsButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button time5m;
    @FXML
    private Button time10m;
    @FXML
    private Button time15m;
    @FXML
    private Button backButton;

    @FXML
    private Label styleLabel;
    @FXML
    private Rectangle darkCyan;
    @FXML
    private Rectangle darkRed;
    @FXML
    private Rectangle darkGray;

    public static int chosenTime = 10;
    public static Color darkTileColor = Color.DARKCYAN;

    @FXML
    public void initialize() {
        darkCyan.setFill(Color.DARKCYAN);
        darkRed.setFill(Color.INDIANRED);
        darkGray.setFill(Color.LIGHTSLATEGRAY);

        darkCyan.setOnMouseClicked(e -> {
            chooseDarkCyan();
            backFromOptions();
        });
        darkRed.setOnMouseClicked(e -> {
            chooseDarkRed();
            backFromOptions();
        });
        darkGray.setOnMouseClicked(e -> {
            chooseDarkGray();
            backFromOptions();
        });
    }

    public void choosePlayOpts() {
        playButton.setVisible(false);
        optsButton.setVisible(false);
        exitButton.setVisible(false);

        time5m.setVisible(true);
        time10m.setVisible(true);
        time15m.setVisible(true);
        backButton.setVisible(true);
    }

    public void backFromPlay() {
        playButton.setVisible(true);
        optsButton.setVisible(true);
        exitButton.setVisible(true);

        time5m.setVisible(false);
        time10m.setVisible(false);
        time15m.setVisible(false);
        backButton.setVisible(false);
    }

    public void goToOptions() {
        playButton.setVisible(false);
        optsButton.setVisible(false);
        exitButton.setVisible(false);

        styleLabel.setVisible(true);
        darkCyan.setVisible(true);
        darkRed.setVisible(true);
        darkGray.setVisible(true);
    }

    public void backFromOptions() {
        playButton.setVisible(true);
        optsButton.setVisible(true);
        exitButton.setVisible(true);

        styleLabel.setVisible(false);
        darkCyan.setVisible(false);
        darkRed.setVisible(false);
        darkGray.setVisible(false);
    }

    public void chooseDarkCyan() {
        darkTileColor = Color.DARKCYAN;
    }

    public void chooseDarkRed() {
        darkTileColor = Color.INDIANRED;
    }

    public void chooseDarkGray() {
        darkTileColor = Color.LIGHTSLATEGRAY;
    }

    public void quitApp() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
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
        Stage stage = (Stage) gridPane.getScene().getWindow();
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/game.fxml"));
        stage.getScene().setRoot(gameRoot);
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

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

    public void choosePlayOpts() {
        playButton.setVisible(false);
        optsButton.setVisible(false);
        exitButton.setVisible(false);

        time5m.setVisible(true);
        time10m.setVisible(true);
        time15m.setVisible(true);
        backButton.setVisible(true);
    }

    public void backToMenu() {
        playButton.setVisible(true);
        optsButton.setVisible(true);
        exitButton.setVisible(true);

        time5m.setVisible(false);
        time10m.setVisible(false);
        time15m.setVisible(false);
        backButton.setVisible(false);
    }

    public void quitApp() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    public void launchGame() throws IOException {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/view/game.fxml"));
        stage.setScene(new Scene(gameRoot, 1200, 800));
        stage.show();
    }
}

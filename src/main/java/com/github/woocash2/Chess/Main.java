package com.github.woocash2.Chess;

import com.github.woocash2.Chess.controller.SoundPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

    // Only one sound player for entire application ensures that there won't be too many clips.
    public static SoundPlayer soundPlayer = new SoundPlayer();

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        stage.setTitle("CHESS");
        stage.setScene(new Scene(mainMenu));
        stage.setMinWidth(1200);
        stage.setMinHeight(900);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

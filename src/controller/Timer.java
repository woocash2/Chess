package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import model.Piece;

import java.sql.Timestamp;

public class Timer extends Thread {

    private long mins, secs;
    private long millisToSecondPass;
    public Label time;
    private GameController gameController;
    Piece.team team;

    private boolean stop = false;

    public Timer(long m, Piece.team color, Label label, GameController game) {
        mins = m;
        secs = 0;
        millisToSecondPass = 1000;
        gameController = game;
        team = color;
        time = label;
        Platform.runLater(() -> time.setText(makeNewTime()));
    }

    @Override
    public void run() {
        measureTime();
    }

    public synchronized void measureTime() {
        while (true) {
            if (gameController.turn != team) {
                try {
                    wait();
                }
                catch (InterruptedException e) {}
            }

            long moment1 = System.currentTimeMillis();
            try {
                wait(millisToSecondPass);
            } catch (InterruptedException e) {}

            long moment2 = System.currentTimeMillis();
            millisToSecondPass -= moment2 - moment1;

            if (stop)
                return;

            while (millisToSecondPass <= 0) {
                secs--;
                if (secs < 0) {
                    mins--;
                    secs = 59;
                }
                Platform.runLater(() -> time.setText(makeNewTime()));
                millisToSecondPass += 1000;
            }
        }
    }

    public String makeNewTime() {
        String newTime = Long.toString(mins) + ":";
        if (Long.toString(secs).length() == 1)
            newTime += "0" + Long.toString(secs);
        else
            newTime += Long.toString(secs);
        return newTime;
    }

    public void halt() {
        stop = true;
    }
}

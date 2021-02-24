package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.*;
import com.github.woocash2.Chess.model.utils.TeamRandomizer;
import javafx.application.Platform;

import java.util.ArrayList;

public class TurnManager {

    public GameController gameController;

    // Determines pieces of which color are to make a move.
    public Piece.Team turn = Piece.Team.BLACK;

    public int minutes;
    public Timer whiteTimer, blackTimer;
    public boolean computerGame;
    public Piece.Team playerTeam;

    public Computer computer;
    public boolean gameFinished = false;
    public Piece.Type computerPromoType = null;

    public TurnManager(GameController controller) {
        gameController = controller;
        minutes = MenuController.chosenTime;

        // computer mode
        computerGame = MenuController.computerGame;
        if (computerGame) {
            playerTeam = MenuController.playerTeam;
            computer = new Computer(gameController, TeamRandomizer.getOpposite(playerTeam));
        }

        if (minutes != 0) {
            whiteTimer = new Timer(minutes, Piece.Team.WHITE, gameController.whiteTime, gameController);
            blackTimer = new Timer(minutes, Piece.Team.BLACK, gameController.blackTime, gameController);
            whiteTimer.setDaemon(true);
            blackTimer.setDaemon(true);
        }
        else {
            gameController.whiteTime.setVisible(false);
            gameController.blackTime.setVisible(false);
        }
    }

    public void runTimers() {
        if (minutes != 0) {
            whiteTimer.start();
            blackTimer.start();
        }
    }

    public void notifyTurnMade() {

        Piece.Team newTurn = TeamRandomizer.getOpposite(turn);

        // computer game
        if (computerGame)
            gameController.boardCover.setVisible(newTurn != playerTeam);
        else
            gameController.boardCover.setVisible(false);

        updatePositions(newTurn);

        turn = newTurn;

        if (whiteTimer != null && blackTimer != null) {
            Platform.runLater(() -> {
                whiteTimer.interrupt();
                blackTimer.interrupt();
            });
        }

        if (gameController.boardManager.board.isCheckMated(newTurn))
            endTheGame(TeamRandomizer.getOpposite(newTurn));

        if (gameController.boardManager.board.isStaleMate(newTurn) || gameController.boardManager.pieces.size() == 2) // size == 2 means only two kings left
            endTheGame(null);

        // computer game
        if (computerGame && !gameFinished && turn != playerTeam) {
            Move move = computer.findMove();
            computerPromoType = move.promoteTo;
            gameController.actionManager.piecesAnchorPressBehavior(move.fromXScreen(), move.fromYScreen());
            gameController.actionManager.piecesAnchorPressBehavior(move.toXScreen(), move.toYScreen());
        }
    }

    public void updatePositions(Piece.Team newTurn) {
        for (PieceImg pieceImg : gameController.boardManager.pieces) {
            if (pieceImg.piece.team == newTurn)
                pieceImg.piece.updatePositions();
        }
    }

    public void endTheGame(Piece.Team winner) {
        if (whiteTimer != null && blackTimer != null) { // can't be just one of them
            whiteTimer.halt();
            blackTimer.halt();
        }
        GameResult result = new GameResult(gameController.resultLabel);
        result.show(winner);
        gameController.boardCover.setVisible(true); // no more moves available
        gameController.soundPlayer.playNotify();
        gameFinished = true;
    }
}

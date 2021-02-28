package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.*;
import com.github.woocash2.Chess.model.utils.TeamRandomizer;
import javafx.application.Platform;

public class TurnManager {

    public GameController gameController;

    // Determines pieces of which color are to make a move.
    public Board.Team turn = Board.Team.BLACK;

    public int minutes;
    public Timer whiteTimer, blackTimer;
    public boolean computerGame;
    public Board.Team playerTeam;

    public Computer computer;
    public boolean gameFinished = false;
    public Board.Piece computerPromoType = null;

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
            whiteTimer = new Timer(minutes, Board.Team.WHITE, gameController.whiteTime, gameController);
            blackTimer = new Timer(minutes, Board.Team.BLACK, gameController.blackTime, gameController);
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

        Board.Team newTurn = TeamRandomizer.getOpposite(turn);

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
            computerPromoType = move.post;
            gameController.actionManager.piecesAnchorPressBehavior(fromXScreen(move), fromYScreen(move));
            gameController.actionManager.piecesAnchorPressBehavior(toXScreen(move), toYScreen(move));
        }
    }

    public void updatePositions(Board.Team newTurn) {
        Board board = gameController.boardManager.board;
        board.updateMoves(newTurn);

        for (Tile[] tiles : gameController.boardManager.tiles) {
            for (Tile tile : tiles) {
                tile.reachableTiles.clear();
                tile.takeableTiles.clear();
            }
        }

        for (Move move : board.moves) {
            int x = move.fromX, y = move.fromY;
            int a = move.toX, b = move.toY;
            if (board.pieces[a][b] != Board.Piece.EMPTY || (board.pieces[x][y] == Board.Piece.PAWNM && y != b))
                gameController.boardManager.tiles[x][y].takeableTiles.add(gameController.boardManager.tiles[a][b]);
            else
                gameController.boardManager.tiles[x][y].reachableTiles.add(gameController.boardManager.tiles[a][b]);
        }
    }

    public void endTheGame(Board.Team winner) {
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

    public double fromXScreen(Move move) {
        return move.fromY * 100.0 + 50;
    }
    public double fromYScreen(Move move) {
        return move.fromX * 100 + 50;
    }
    public double toXScreen(Move move) {
        return move.toY * 100 + 50;
    }
    public double toYScreen(Move move) {
        return move.toX * 100 + 50;
    }
}

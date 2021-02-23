package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.*;
import com.github.woocash2.Chess.model.utils.TeamRandomizer;
import javafx.application.Platform;

import java.util.ArrayList;

public class TurnManager {

    public GameController gameController;

    // Determines pieces of which color are to make a move.
    public Piece.team turn = Piece.team.BLACK;

    // For checking checkmate / stalemate conditions
    public King whiteKing, blackKing;

    // For castling purposes.
    public ArrayList<PieceImg> whiteRooks = new ArrayList<>();
    public ArrayList<PieceImg> blackRooks = new ArrayList<>();

    public int minutes;
    public Timer whiteTimer, blackTimer;
    public boolean computerGame;
    public Piece.team playerTeam;

    public Computer computer;
    public boolean gameFinished = false;

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
            whiteTimer = new Timer(minutes, Piece.team.WHITE, gameController.whiteTime, gameController);
            blackTimer = new Timer(minutes, Piece.team.BLACK, gameController.blackTime, gameController);
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

        Piece.team newTurn = TeamRandomizer.getOpposite(turn);

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

        if (isCheckMated(newTurn))
            endTheGame(Piece.oponnent(newTurn));

        if (isStaleMate(newTurn) || gameController.boardManager.pieces.size() == 2) // size == 2 means only two kings left
            endTheGame(null);

        // computer game
        if (computerGame && !gameFinished && turn != playerTeam) {
            Move move = computer.findMove();
            gameController.actionManager.piecesAnchorPressBehavior(move.fromXScreen(), move.fromYScreen());
            gameController.actionManager.piecesAnchorPressBehavior(move.toXScreen(), move.toYScreen());
        }
    }

    public void updatePositions(Piece.team newTurn) {
        for (PieceImg pieceImg : gameController.boardManager.pieces) {
            if (pieceImg.piece.color == newTurn)
                pieceImg.piece.updatePositions();

            if (pieceImg.piece.getClass() == King.class) {
                if (pieceImg.piece.color == Piece.team.WHITE) {
                    if (whiteRooks.size() > 0)
                        ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(0).piece);
                    if (whiteRooks.size() > 1)
                        ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(1).piece);
                }
                else {
                    if (blackRooks.size() > 0)
                        ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(0).piece);
                    if (blackRooks.size() > 1)
                        ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(1).piece);
                }
            }

            if (pieceImg.piece.getClass() == Pawn.class) {
                int x = pieceImg.piece.x;
                int y = pieceImg.piece.y;

                Piece nextToUs;
                if (gameController.boardManager.board.inBoardRange(x, y + 1) && gameController.boardManager.tiles[x][y + 1].pieceImg != null) {
                    nextToUs = gameController.boardManager.tiles[x][y + 1].pieceImg.piece;
                    if (nextToUs.getClass() == Pawn.class && pieceImg.piece.color != nextToUs.color)
                        ((Pawn) pieceImg.piece).considerEnPassant((Pawn) nextToUs);
                }
                if (gameController.boardManager.board.inBoardRange(x, y - 1) && gameController.boardManager.tiles[x][y - 1].pieceImg != null) {
                    nextToUs = gameController.boardManager.tiles[x][y - 1].pieceImg.piece;
                    if (nextToUs.getClass() == Pawn.class && pieceImg.piece.color != nextToUs.color)
                        ((Pawn) pieceImg.piece).considerEnPassant((Pawn) nextToUs);
                }
            }
        }
    }

    public boolean isCheckMated(Piece.team team) {
        King king;
        if (team == Piece.team.WHITE) king = whiteKing;
        else king = blackKing;

        if (gameController.boardManager.board.numOfAttackers(team, king.x, king.y) > 0) {
            return numberOfPossibleMoves(team) == 0;
        }

        return false;
    }

    public boolean isStaleMate(Piece.team team) {
        King king;
        if (team == Piece.team.WHITE) king = whiteKing;
        else king = blackKing;

        if (gameController.boardManager.board.numOfAttackers(team, king.x, king.y) > 0)
            return false;

        return numberOfPossibleMoves(team) == 0;
    }

    public int numberOfPossibleMoves(Piece.team team) {
        int possibleMoves = 0;
        for (PieceImg piece : gameController.boardManager.pieces) {
            if (piece.piece.color != team)
                continue;
            possibleMoves += piece.piece.reachablePositions.size();
            possibleMoves += piece.piece.takeablePositions.size();
        }

        return possibleMoves;
    }

    public void endTheGame(Piece.team winner) {
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

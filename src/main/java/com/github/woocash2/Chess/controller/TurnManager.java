package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.King;
import com.github.woocash2.Chess.model.Pawn;
import com.github.woocash2.Chess.model.Piece;
import com.github.woocash2.Chess.model.Rook;
import javafx.application.Platform;

import java.util.ArrayList;

public class TurnManager {

    public GameController gameController;

    // Determines pieces of which color are to make a move.
    public Piece.team turn = Piece.team.WHITE;

    // For checking checkmate / stalemate conditions
    public King whiteKing, blackKing;

    // For castling purposes.
    public ArrayList<PieceImg> whiteRooks = new ArrayList<>();
    public ArrayList<PieceImg> blackRooks = new ArrayList<>();

    public int minutes;
    public Timer whiteTimer, blackTimer;

    public TurnManager(GameController controller) {
        gameController = controller;
        minutes = MenuController.chosenTime;

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

        Piece.team newTurn;
        if (turn == Piece.team.WHITE)
            newTurn = Piece.team.BLACK;
        else
            newTurn = Piece.team.WHITE;

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
    }

    public void updatePositions(Piece.team newTurn) {
        for (PieceImg pieceImg : gameController.boardManager.pieces) {
            if (pieceImg.piece.color == newTurn)
                pieceImg.piece.updatePositions();

            if (pieceImg.piece.getClass() == King.class) {
                if (pieceImg.piece.color == Piece.team.WHITE) {
                    ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(0).piece);
                    ((King) pieceImg.piece).considerCastling((Rook) whiteRooks.get(1).piece);
                }
                else {
                    ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(0).piece);
                    ((King) pieceImg.piece).considerCastling((Rook) blackRooks.get(1).piece);
                }
            }

            if (pieceImg.piece.getClass() == Pawn.class) {
                int x = pieceImg.piece.x;
                int y = pieceImg.piece.y;
                if ((pieceImg.piece.color == Piece.team.WHITE && pieceImg.piece.x == 0) ||
                        (pieceImg.piece.color == Piece.team.BLACK && pieceImg.piece.x == 7)) {
                    gameController.actionManager.promotionPanel.show(pieceImg);
                    return; // so clock is still ticking for promoting player and turn is unchanged.
                }
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

        if (gameController.boardManager.board.isAttacked(team, king.x, king.y)) {
            return numberOfPossibleMoves(team) == 0;
        }

        return false;
    }

    public boolean isStaleMate(Piece.team team) {
        King king;
        if (team == Piece.team.WHITE) king = whiteKing;
        else king = blackKing;

        if (gameController.boardManager.board.isAttacked(team, king.x, king.y))
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
    }
}

package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.function.Function;

public interface Pawn {

    public static boolean isPawn(Board.Piece piece) {
        return piece == Board.Piece.PAWN || piece == Board.Piece.PAWNJ || piece == Board.Piece.PAWNM;
    }
    
    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f, Board board, int x, int y, Board.Team color) {
        if (color == Board.Team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            f.apply(new Pair<>(x - 1, y));
        if (color == Board.Team.BLACK && x + 1 < 8 && board.isEmpty(x + 1, y))
            f.apply(new Pair<>(x + 1, y));

        for (int j = -1; j <= 1; j += 2) {
            if (color == Board.Team.WHITE && board.inBoardRange(x - 1, y + j) && !board.isEmpty(x - 1, y + j) && board.teams[x - 1][y + j] == Board.Team.BLACK)
                f.apply(new Pair<>(x - 1, y + j));
            if (color == Board.Team.BLACK && board.inBoardRange(x + 1, y + j) && !board.isEmpty(x + 1, y + j) && board.teams[x + 1][y + j] == Board.Team.WHITE)
                f.apply(new Pair<>(x + 1, y + j));
        }

        if ((color == Board.Team.WHITE && x == 6) || (color == Board.Team.BLACK && x == 1)) {
            if (color == Board.Team.WHITE && board.inBoardRange(x - 2, y) && board.isEmpty(x - 2, y) && board.isEmpty(x - 1, y))
                f.apply(new Pair<>(x - 2, y));
            if (color == Board.Team.BLACK && board.inBoardRange(x + 2, y) && board.isEmpty(x + 2, y) && board.isEmpty(x + 1, y))
                f.apply(new Pair<>(x + 2, y));
        }
    }

    public static void updatePositions(Board board, int x, int y) {
        boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y, board.teams[x][y]);
        considerEnPassant(board, x, y);
    }

    public static void considerEnPassant(Board board, int x, int y) { // assumes enemyPawn next to us
        Board.Piece toLeft = null;
        Board.Piece toRight = null;
        Board.Team team = board.teams[x][y];

        if (board.inBoardRange(x, y - 1) && board.teams[x][y - 1] != team && board.pieces[x][y - 1] == Board.Piece.PAWNJ)
            toLeft = board.pieces[x][y - 1];
        if (board.inBoardRange(x, y + 1) && board.teams[x][y + 1] != team && board.pieces[x][y + 1] == Board.Piece.PAWNJ)
            toRight = board.pieces[x][y + 1];

        if (toLeft != null) {
            if (team == Board.Team.WHITE && x == 3)
                PositionUpdater.addMoveIfLegal(board, x, y).apply(new Pair<>(x - 1, y - 1));
            if (team == Board.Team.BLACK && x == 4)
                PositionUpdater.addMoveIfLegal(board, x, y).apply(new Pair<>(x + 1, y - 1));
        }
        if (toRight != null) {
            if (team == Board.Team.WHITE && x == 3)
                PositionUpdater.addMoveIfLegal(board, x, y).apply(new Pair<>(x - 1, y + 1));
            if (team == Board.Team.BLACK && x == 4)
                PositionUpdater.addMoveIfLegal(board, x, y).apply(new Pair<>(x + 1, y + 1));
        }
    }
}

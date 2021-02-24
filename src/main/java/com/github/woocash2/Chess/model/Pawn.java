package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.function.Function;

public interface Pawn {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f1, Function<Pair<Integer, Integer>, E> f2, Board board, int x, int y, Piece.Team color) {
        if (color == Piece.Team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            f1.apply(new Pair<>(x - 1, y));
        if (color == Piece.Team.BLACK && x + 1 < 8 && board.isEmpty(x + 1, y))
            f1.apply(new Pair<>(x + 1, y));

        for (int j = -1; j <= 1; j += 2) {
            if (color == Piece.Team.WHITE && board.inBoardRange(x - 1, y + j) && !board.isEmpty(x - 1, y + j) && Character.isUpperCase(board.get(x - 1, y + j).identifier))
                f2.apply(new Pair<>(x - 1, y + j));
            if (color == Piece.Team.BLACK && board.inBoardRange(x + 1, y + j) && !board.isEmpty(x + 1, y + j) && Character.isLowerCase(board.get(x + 1, y + j).identifier))
                f2.apply(new Pair<>(x + 1, y + j));
        }

        if ((color == Piece.Team.WHITE && x == 6) || (color == Piece.Team.BLACK && x == 1)) {
            if (color == Piece.Team.WHITE && board.inBoardRange(x - 2, y) && board.isEmpty(x - 2, y) && board.isEmpty(x - 1, y))
                f1.apply(new Pair<>(x - 2, y));
            if (color == Piece.Team.BLACK && board.inBoardRange(x + 2, y) && board.isEmpty(x + 2, y) && board.isEmpty(x + 1, y))
                f1.apply(new Pair<>(x + 2, y));
        }
    }

    public static void updatePositions(Piece piece) {
        piece.reachablePositions.clear();
        piece.takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(piece), PositionUpdater.addToTakeableFunction(piece), piece.board, piece.x, piece.y, piece.team);
        considerEnPassant(piece);
    }

    public static void considerEnPassant(Piece piece) { // assumes enemyPawn next to us
        Piece toLeft = null;
        Piece toRight = null;
        Board board = piece.board;
        Piece.Team team = piece.team;
        int x = piece.x;
        int y = piece.y;

        if (board.inBoardRange(x, y - 1))
            toLeft = board.pieces[x][y - 1];
        if (board.inBoardRange(x, y + 1))
            toRight = board.pieces[x][y + 1];

        if (toLeft != null && toLeft.type == Piece.Type.PAWN && toLeft.team != team && toLeft.justMoved) {
            if (team == Piece.Team.WHITE && x == 3)
                PositionUpdater.addToTakeableEnPassantFunction(piece, toLeft).apply(new Pair<>(x - 1, y - 1));
            if (team == Piece.Team.BLACK && x == 4)
                PositionUpdater.addToTakeableEnPassantFunction(piece, toLeft).apply(new Pair<>(x + 1, y - 1));
        }
        if (toRight != null && toRight.type == Piece.Type.PAWN && toRight.team != team && toRight.justMoved) {
            if (team == Piece.Team.WHITE && x == 3)
                PositionUpdater.addToTakeableEnPassantFunction(piece, toRight).apply(new Pair<>(x - 1, y + 1));
            if (team == Piece.Team.BLACK && x == 4)
                PositionUpdater.addToTakeableEnPassantFunction(piece, toRight).apply(new Pair<>(x + 1, y + 1));
        }
    }
}

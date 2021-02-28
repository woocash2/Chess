package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.Iterator;
import java.util.function.Function;

public interface King {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f, Board board, int x, int y) {
        Iterator<Pair<Integer, Integer>> kingIterator = new Iterator<Pair<Integer, Integer>>() {
            Pair<Integer, Integer> current = new Pair<>(x - 1, y - 1);

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Pair<Integer, Integer> next() {
                int a = current.getKey();
                int b = current.getValue();
                b++;
                if (b > y + 1) {
                    b = y - 1;
                    a++;
                    if (a > x + 1)
                        a = x - 1;
                }
                if (a == x && b == y)
                    b++;

                current = new Pair<>(a, b);
                return current;
            }
        };

        for (int i = 0; i < 8; i++) {
            Pair<Integer, Integer> field = kingIterator.next();
            int a = field.getKey(), b = field.getValue();
            if (board.inBoardRange(a, b))
                f.apply(field);
        }
    }

    public static void updatePositions(Board board, int x, int y) {
        boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y);
        considerCastling(board, x, y);
    }

    public static void considerCastling(Board board, int x, int y) { // assumes same color rook
        Board.Piece piece = board.pieces[x][y];
        if (piece != Board.Piece.KING)
            return;

        Board.Team team = board.teams[x][y];
        Board.Piece leftRook = board.pieces[x][0];
        Board.Piece rightRook = board.pieces[x][7];

        if (leftRook == Board.Piece.ROOK) {
            boolean leftSuccess = true;
            for (int j = 0; j <= y; j++) {
                if (j >= y - 2 && board.numOfAttackers(team, x, j) > 0)
                    leftSuccess = false;
                if (j > 0 && j < y && !board.isEmpty(x, j))
                    leftSuccess = false;
            }
            if (leftSuccess)
                board.moves.add(new Move(x, y, x, y - 2, Board.Piece.KING, Board.Piece.KINGM, team, Board.Piece.EMPTY));
        }

        if (rightRook == Board.Piece.ROOK) {
            boolean rightSuccess = true;
            for (int j = y; j <= 7; j++) {
                if (j <= y + 2 && board.numOfAttackers(team, x, j) > 0)
                    rightSuccess = false;
                if (j > y && j < 7 && !board.isEmpty(x, j))
                    rightSuccess = false;
            }
            if (rightSuccess)
                board.moves.add(new Move(x, y, x, y + 2, Board.Piece.KING, Board.Piece.KINGM, team, Board.Piece.EMPTY));
        }
    }
}

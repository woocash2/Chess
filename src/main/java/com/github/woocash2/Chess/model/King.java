package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.Iterator;
import java.util.function.Function;

public interface King {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f1, Function<Pair<Integer, Integer>, E> f2, Board board, int x, int y) {
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
            if (board.inBoardRange(a, b) && board.isEmpty(a, b))
                f1.apply(field);
            else if (board.inBoardRange(a, b))
                f2.apply(field);
        }
    }

    public static void updatePositions(Piece piece) {
        piece.reachablePositions.clear();
        piece.takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(piece), PositionUpdater.addToTakeableFunction(piece), piece.board, piece.x, piece.y);
        considerCastling(piece);
    }

    public static void considerCastling(Piece piece) { // assumes same color rook

        if (piece.type != Piece.Type.KING || piece.moved)
            return;

        int x = piece.x;
        int y = piece.y;

        Board board = piece.board;
        Piece.Team team = piece.team;
        Piece leftRook = board.pieces[x][0];
        Piece rightRook = board.pieces[x][7];

        if (leftRook != null && leftRook.type == Piece.Type.ROOK && !leftRook.moved) {
            boolean leftSuccess = true;
            for (int j = 0; j <= y; j++) {
                if (j >= y - 2 && board.numOfAttackers(team, x, j) > 0)
                    leftSuccess = false;
                if (j > 0 && j < y && !board.isEmpty(x, j))
                    leftSuccess = false;
            }
            if (leftSuccess)
                piece.reachablePositions.add(new Pair<>(x, y - 2));
        }

        if (rightRook != null && rightRook.type == Piece.Type.ROOK && !rightRook.moved) {
            boolean rightSuccess = true;
            for (int j = y; j <= 7; j++) {
                if (j <= y + 2 && board.numOfAttackers(team, x, j) > 0)
                    rightSuccess = false;
                if (j > y && j < 7 && !board.isEmpty(x, j))
                    rightSuccess = false;
            }
            if (rightSuccess)
                piece.reachablePositions.add(new Pair<>(x, y + 2));
        }
    }
}

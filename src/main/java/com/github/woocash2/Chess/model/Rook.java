package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.function.Function;

public interface Rook {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f1, Function<Pair<Integer, Integer>, E> f2, Board board, int x, int y) {
        for (int i = x + 1; i < 8; i++) {
            if (board.isEmpty(i, y))
                f1.apply(new Pair<>(i, y));
            else {
                f2.apply(new Pair<>(i, y));
                break;
            }
        }
        for (int i = x - 1; i >= 0; i--) {
            if (board.isEmpty(i, y))
                f1.apply(new Pair<>(i, y));
            else {
                f2.apply(new Pair<>(i, y));
                break;
            }
        }
        for (int j = y + 1; j < 8; j++) {
            if (board.isEmpty(x, j))
                f1.apply(new Pair<>(x, j));
            else {
                f2.apply(new Pair<>(x, j));
                break;
            }
        }
        for (int j = y - 1; j >= 0; j--) {
            if (board.isEmpty(x, j))
                f1.apply(new Pair<>(x, j));
            else {
                f2.apply(new Pair<>(x, j));
                break;
            }
        }
    }

    public static void updatePositions(Piece piece) {
        piece.reachablePositions.clear();
        piece.takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(piece), PositionUpdater.addToTakeableFunction(piece), piece.board, piece.x, piece.y);
    }
}

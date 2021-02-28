package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;
import java.util.function.Function;

public interface Bishop {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f, Board board, int x, int y) {
        for (int i = x + 1, j = y + 1; board.inBoardRange(i, j); i++, j++) {
            if (board.isEmpty(i, j))
                f.apply(new Pair<>(i, j));
            else {
                f.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x - 1, j = y - 1; board.inBoardRange(i, j); i--, j--) {
            if (board.isEmpty(i, j))
                f.apply(new Pair<>(i, j));
            else {
                f.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x + 1, j = y - 1; board.inBoardRange(i, j); i++, j--) {
            if (board.isEmpty(i, j))
                f.apply(new Pair<>(i, j));
            else {
                f.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x - 1, j = y + 1; board.inBoardRange(i, j); i--, j++) {
            if (board.isEmpty(i, j))
                f.apply(new Pair<>(i, j));
            else {
                f.apply(new Pair<>(i, j));
                break;
            }
        }
    }

    public static void updatePositions(Board board, int x, int y) {
        boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y);
    }
}

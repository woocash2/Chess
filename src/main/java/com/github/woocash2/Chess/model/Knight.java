package com.github.woocash2.Chess.model;

import javafx.util.Pair;
import com.github.woocash2.Chess.model.utils.PositionUpdater;

import java.util.Iterator;
import java.util.function.Function;

public interface Knight {

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f, Board board, int x, int y) {
        Iterator<Pair<Integer, Integer>> knightIterator = new Iterator<Pair<Integer, Integer>>() {

            Pair<Integer, Integer> current = new Pair<>(x + 1, y + 2);

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Pair<Integer, Integer> next() {
                int a = current.getKey();
                int b = current.getValue();

                if (a == x + 1)
                    current = b == y + 2 ? new Pair<>(x + 2, y + 1) : new Pair<>(x - 1, y - 2);
                else if (a == x + 2)
                    current = b == y + 1 ? new Pair<>(x + 2, y - 1) : new Pair<>(x + 1, y - 2);
                else if (a == x - 1)
                    current = b == y + 2 ? new Pair<>(x + 1, y + 2) : new Pair<>(x - 2, y - 1);
                else
                    current = b == y + 1 ? new Pair<>(x - 1, y + 2) : new Pair<>(x - 2, y + 1);
                return current;
            }
        };

        for (int i = 0; i < 8; i++) {
            Pair<Integer, Integer> field = knightIterator.next();
            int a = field.getKey(), b = field.getValue();
            if (board.inBoardRange(a, b))
                f.apply(field);
        }
    }

    public static void updatePositions(Board board, int x, int y) {
        boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y);
    }
}

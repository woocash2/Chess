package model;

import javafx.util.Pair;
import model.utils.PositionUpdater;

import java.util.Iterator;
import java.util.function.Function;

public class King extends Piece {
    public King(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'k';
        else
            onBoard = 'K';
    }

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
            if (board.inBoardRange(a, b))
                f2.apply(field);
        }
    }

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this), board, x, y);
    }

    public void considerCastling(Rook rook) { // assumes same color rook
        if (!moved && !rook.moved) {
            int a = Math.min(y, rook.y);
            int b = Math.max(y, rook.y);

            for (int i = a + 1; i < b; i++) {
                if (!board.isEmpty(x, i))
                    return;
            }

            for (int i = a; i <= b; i++) {
                if (board.isAttacked(color, x, i))
                    return;
            }

            if (rook.y == 0)
                reachablePositions.add(new Pair<>(x, y - 2));
            else
                reachablePositions.add(new Pair<>(x, y + 2));
        }
    }
}

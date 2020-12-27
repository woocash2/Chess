package model;

import javafx.util.Pair;

import java.util.function.Function;

public class Bishop extends Piece {

    public Bishop(int x, int y, team col) {
        super(x, y, col);
        if (col == team.WHITE)
            onBoard = 'b';
        else
            onBoard = 'B';
    }

    public static <T> void boardIteration(Function<Pair<Integer, Integer>, T> function, Board board, int x, int y) {
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if (board.isEmpty(i, j))
                function.apply(new Pair<>(i, j));
            else
                break;
        }
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (board.isEmpty(i, j))
                function.apply(new Pair<>(i, j));
            else
                break;
        }
        for (int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if (board.isEmpty(i, j))
                function.apply(new Pair<>(i, j));
            else
                break;
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if (board.isEmpty(i, j))
                function.apply(new Pair<>(i, j));
            else
                break;
        }
    }

    @Override
    public void updateReachablePositions() {
        reachablePositions.clear();
        boardIteration(p -> reachablePositions.add(p), board, x, y);
    }
}

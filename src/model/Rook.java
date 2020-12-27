package model;

import javafx.util.Pair;

import java.util.function.Function;

public class Rook extends Piece {

    public Rook(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'r';
        else
            onBoard = 'R';
    }

    public static <T> void boardIteration(Function<Pair<Integer, Integer>, T> function, Board board, int x, int y) {
        for (int i = x + 1; i < 8; i++) {
            if (board.isEmpty(i, y))
                function.apply(new Pair<>(i, y));
            else
                break;
        }
        for (int i = x - 1; i >= 0; i--) {
            if (board.isEmpty(i, y))
                function.apply(new Pair<>(i, y));
            else
                break;
        }
        for (int j = y + 1; j < 8; j++) {
            if (board.isEmpty(x, j))
                function.apply(new Pair<>(x, j));
            else
                break;
        }
        for (int j = y - 1; j >= 0; j--) {
            if (board.isEmpty(x, j))
                function.apply(new Pair<>(x, j));
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

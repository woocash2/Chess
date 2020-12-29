package model;

import javafx.util.Pair;
import model.utils.PositionUpdater;
import java.util.function.Function;

public class Bishop extends Piece {

    public Bishop(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'b';
        else
            onBoard = 'B';
    }

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f1, Function<Pair<Integer, Integer>, E> f2, Board board, int x, int y) {
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if (board.isEmpty(i, j))
                f1.apply(new Pair<>(i, j));
            else {
                f2.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (board.isEmpty(i, j))
                f1.apply(new Pair<>(i, j));
            else {
                f2.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if (board.isEmpty(i, j))
                f1.apply(new Pair<>(i, j));
            else {
                f2.apply(new Pair<>(i, j));
                break;
            }
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if (board.isEmpty(i, j))
                f1.apply(new Pair<>(i, j));
            else {
                f2.apply(new Pair<>(i, j));
                break;
            }
        }
    }

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this), board, x, y);
    }
}

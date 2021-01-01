package model;

import javafx.util.Pair;
import model.utils.PositionUpdater;

import java.util.function.Function;

public class Rook extends Piece {

    public Rook(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'r';
        else
            onBoard = 'R';
    }

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

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this), board, x, y);
    }
}

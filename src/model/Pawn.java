package model;

import javafx.util.Pair;
import model.utils.PositionUpdater;

import java.util.function.Function;

public class Pawn extends Piece {

    public Pawn(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'p';
        else
            onBoard = 'P';
    }

    public static <T, E> void boardIteration(Function<Pair<Integer, Integer>, T> f1, Function<Pair<Integer, Integer>, E> f2, Board board, int x, int y, team color) {
        if (color == team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            f1.apply(new Pair<>(x - 1, y));
        if (color == team.BLACK && x + 1 < 8 && board.isEmpty(x + 1, y))
            f1.apply(new Pair<>(x + 1, y));

        for (int j = -1; j <= 1; j += 2) {
            if (color == team.WHITE && board.inBoardRange(x - 1, y + j) && Character.isUpperCase(board.get(x - 1, y + j)))
                f2.apply(new Pair<>(x - 1, y + j));
            if (color == team.BLACK && board.inBoardRange(x + 1, y + j) && Character.isLowerCase(board.get(x + 1, y + j)))
                f2.apply(new Pair<>(x + 1, y + j));
        }

        if ((color == team.WHITE && x == 6) || (color == team.BLACK && x == 1)) {
            if (color == team.WHITE && board.inBoardRange(x - 2, y) && board.isEmpty(x - 2, y))
                f1.apply(new Pair<>(x - 2, y));
            if (color == team.BLACK && board.inBoardRange(x + 2, y) && board.isEmpty(x + 2, y))
                f1.apply(new Pair<>(x + 2, y));
        }
    }

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this), board, x, y, color);
    }
}

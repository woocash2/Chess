package model;

import javafx.util.Pair;

public class Pawn extends Piece {
    public Pawn(int x, int y, team col) {
        super(x, y, col);
        if (col == team.WHITE)
            onBoard = 'p';
        else
            onBoard = 'P';
    }

    @Override
    public void updateReachablePositions() {
        reachablePositions.clear();
        if (color == team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            reachablePositions.add(new Pair<>(x - 1, y));
        if (color == team.BLACK && x + 1 >= 0 && board.isEmpty(x + 1, y))
            reachablePositions.add(new Pair<>(x + 1, y));
    }
}

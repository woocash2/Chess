package model;

import javafx.util.Pair;

public class Pawn extends Piece {
    public Pawn(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'p';
        else
            onBoard = 'P';
    }

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        if (color == team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            reachablePositions.add(new Pair<>(x - 1, y));
        if (color == team.BLACK && x + 1 < 8 && board.isEmpty(x + 1, y))
            reachablePositions.add(new Pair<>(x + 1, y));

        for (int j = -1; j <= 1; j += 2) {
            if (color == team.WHITE && x - 1 >= 0 && y + j >= 0 && y + j < 8 && Character.isUpperCase(board.get(x - 1, y + j)))
                takeablePositions.add(new Pair<>(x - 1, y + j));
            if (color == team.BLACK && x + 1 < 8 && y + j >= 0 && y + j < 8 && Character.isLowerCase(board.get(x - 1, y + j)))
                takeablePositions.add(new Pair<>(x + 1, y + j));
        }
    }
}

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
        takeablePositions.clear();
        
        if (color == team.WHITE && x - 1 >= 0 && board.isEmpty(x - 1, y))
            reachablePositions.add(new Pair<>(x - 1, y));
        if (color == team.BLACK && x + 1 < 8 && board.isEmpty(x + 1, y))
            reachablePositions.add(new Pair<>(x + 1, y));

        for (int j = -1; j <= 1; j += 2) {
            if (color == team.WHITE && board.inBoardRange(x - 1, y + j) && Character.isUpperCase(board.get(x - 1, y + j)))
                takeablePositions.add(new Pair<>(x - 1, y + j));
            if (color == team.BLACK && board.inBoardRange(x + 1, y + j) && Character.isLowerCase(board.get(x + 1, y + j)))
                takeablePositions.add(new Pair<>(x + 1, y + j));
        }
    }
}

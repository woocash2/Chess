package model;

import javafx.util.Pair;

public class King extends Piece {
    public King(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'k';
        else
            onBoard = 'K';
    }

    @Override
    public void updateReachablePositions() {
        reachablePositions.clear();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((x + i == x && y + j == y) || x + i < 0 || x + j < 0 || x + i >= 8 || x + j >= 8 || !board.isEmpty(x + i, y + j))
                    continue;
                else
                    reachablePositions.add(new Pair<>(x + i, y + j));
            }
        }
    }
}

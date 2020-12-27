package model;

import javafx.util.Pair;

public class King extends Piece {
    public King(int x, int y, team col) {
        super(x, y, col);
        if (col == team.WHITE)
            onBoard = 'k';
        else
            onBoard = 'K';
    }

    @Override
    public void updateReachablePositions() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i == x && j == y) || i < 0 || j < 0 || i >= 8 || j >= 8 || !board.isEmpty(i, j))
                    continue;
                else
                    reachablePositions.add(new Pair<>(i, j));
            }
        }
    }
}

package model;

import javafx.util.Pair;

public class Knight extends Piece {

    public Knight(int x, int y, team col) {
        super(x, y, col);
        if (col == team.WHITE)
            onBoard = 'n';
        else
            onBoard = 'N';
    }

    @Override
    public void updateReachablePositions() {
        reachablePositions.clear();
        int a = x + 2, b = y + 1;
        if (a < 8 && b < 8)
            reachablePositions.add(new Pair<>(a, b));
        if (-a >= 0 && -b >= 0)
            reachablePositions.add(new Pair<>(-a, -b));
        a = x + 1; b = y + 2;
        if (a < 8 && b < 8)
            reachablePositions.add(new Pair<>(a, b));
        if (-a >= 0 && -b >= 0)
            reachablePositions.add(new Pair<>(-a, -b));
        a = x - 2; b = y + 1;
        if (a >= 0 && b < 8)
            reachablePositions.add(new Pair<>(a, b));
        if (-a < 8 && -b >= 0)
            reachablePositions.add(new Pair<>(-a, -b));
        a = x - 1; b = y + 2;
        if (a >= 0 && b < 8)
            reachablePositions.add(new Pair<>(a, b));
        if (-a < 8 && -b >= 0)
            reachablePositions.add(new Pair<>(-a, -b));
    }
}

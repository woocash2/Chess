package model;

import javafx.util.Pair;

import java.util.Iterator;

public class Knight extends Piece {

    public Knight(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'n';
        else
            onBoard = 'N';
    }

    Iterator<Pair<Integer, Integer>> knightIterator = new Iterator<Pair<Integer, Integer>>() {

        Pair<Integer, Integer> current = new Pair<>(x + 1, y + 2);

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Pair<Integer, Integer> next() {
            int a = current.getKey();
            int b = current.getValue();

            if (a == x + 1)
                current = b == y + 2 ? new Pair<>(x + 2, y + 1) : new Pair<>(x - 1, y - 2);
            else if (a == x + 2)
                current = b == y + 1 ? new Pair<>(x + 2, y - 1) : new Pair<>(x + 1, y - 2);
            else if (a == x - 1)
                current = b == y + 2 ? new Pair<>(x + 1, y + 2) : new Pair<>(x - 2, y - 1);
            else
                current = b == y + 1 ? new Pair<>(x - 1, y + 2) : new Pair<>(x - 2, y + 1);
            return current;
        }
    };

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        for (int i = 0; i < 8; i++) {
            Pair<Integer, Integer> field = knightIterator.next();
            int a = field.getKey(), b = field.getValue();
            if (board.inBoardRange(a, b) && board.isEmpty(a, b))
                reachablePositions.add(field);
            if (board.inBoardRange(a, b) && board.opponentStaysOn(a, b, color))
                takeablePositions.add(field);
        }
    }
}

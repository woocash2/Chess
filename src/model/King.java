package model;

import javafx.util.Pair;

import java.util.Iterator;

public class King extends Piece {
    public King(int x, int y, team col, Board board) {
        super(x, y, col, board);
        if (col == team.WHITE)
            onBoard = 'k';
        else
            onBoard = 'K';
    }

    Iterator<Pair<Integer, Integer>> kingIterator = new Iterator<Pair<Integer, Integer>>() {

        Pair<Integer, Integer> current = new Pair<>(x - 1, y - 1);

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Pair<Integer, Integer> next() {
            int a = current.getKey();
            int b = current.getValue();
            b++;
            if (b > y + 1) {
                b = y - 1;
                a++;
                if (a > x + 1)
                    a = x - 1;
            }
            if (a == x && b == y)
                b++;

            current = new Pair<>(a, b);
            return current;
        }
    } ;

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        for (int i = 0; i < 8; i++) {
            Pair<Integer, Integer> field = kingIterator.next();
            int a = field.getKey(), b = field.getValue();
            if (board.inBoardRange(a, b) && board.isEmpty(a, b))
                reachablePositions.add(field);
            if (board.inBoardRange(a, b) && board.opponentStaysOn(a, b, color))
                takeablePositions.add(field);
        }
    }
}

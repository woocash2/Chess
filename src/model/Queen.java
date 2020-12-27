package model;

public class Queen extends Piece {

    public Queen(int x, int y, Piece.team col, Board board) {
        super(x, y, col, board);
        if (col == Piece.team.WHITE)
            onBoard = 'q';
        else
            onBoard = 'Q';
    }

    @Override
    public void updateReachablePositions() {
        reachablePositions.clear();
        Rook.boardIteration(p -> reachablePositions.add(p), board, x, y);
        Bishop.boardIteration(p -> reachablePositions.add(p), board, x, y);
    }
}

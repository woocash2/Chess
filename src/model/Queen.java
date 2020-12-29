package model;

import model.utils.PositionUpdater;

public class Queen extends Piece {

    public Queen(int x, int y, Piece.team col, Board board) {
        super(x, y, col, board);
        if (col == Piece.team.WHITE)
            onBoard = 'q';
        else
            onBoard = 'Q';
    }

    @Override
    public void updatePositions() {
        reachablePositions.clear();
        takeablePositions.clear();
        Bishop.boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this), board, x, y);
        Rook.boardIteration(PositionUpdater.addToReachableFunction(this), PositionUpdater.addToTakeableFunction(this) ,board, x, y);
    }
}

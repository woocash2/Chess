package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.model.utils.PositionUpdater;

public interface Queen {

    public static void updatePositions(Board board, int x, int y) {
        Bishop.boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y);
        Rook.boardIteration(PositionUpdater.addMoveIfLegal(board, x, y), board, x, y);
    }
}
